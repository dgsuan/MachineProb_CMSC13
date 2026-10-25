"""Original SYSTEMBOUND synthesizer. Python standard library only; no downloaded samples.
Run from any directory: python tools/generate_audio.py
"""
import math
import random
import struct
import wave
from pathlib import Path

RATE = 22050
OUT = Path(__file__).resolve().parents[1] / 'src/resources/Audio'
TAU = 2 * math.pi


def hz(note):
    return 440 * 2 ** ((note - 69) / 12)


def tone(buffer, start, duration, note, gain=0.2, kind='bell', end=None, wrap=False):
    phase = 0.0
    count = int(duration * RATE)
    for i in range(count):
        t = i / RATE
        u = i / max(1, count - 1)
        freq = hz(note) if end is None else hz(note) * (hz(end) / hz(note)) ** u
        phase += TAU * freq / RATE
        attack = min(1, t / (0.18 if kind == 'pad' else 0.008))
        release = min(1, (duration - t) / (0.3 if kind == 'pad' else 0.035))
        if kind == 'pad':
            value = (math.sin(phase) + 0.22 * math.sin(phase * 2)
                     + 0.15 * math.sin(phase + math.sin(TAU * 0.7 * t))) / 1.37
            env = attack * release * math.sin(math.pi * u) ** 0.4
        elif kind == 'bass':
            value = math.sin(phase) + 0.15 * math.sin(2 * phase)
            env = attack * release * math.exp(-3 * u)
        else:
            value = math.sin(phase) + 0.22 * math.sin(2 * phase) * math.exp(-8 * u)
            env = attack * release * math.exp(-4 * u)
        index = int(start * RATE) + i
        if wrap:
            index %= len(buffer)
        if index < len(buffer):
            buffer[index] += value * env * gain


def finish(name, buffer, loop=False):
    # Quiet delay gives the digital tones some space. Circular delay preserves loop tails.
    dry = buffer[:]
    for seconds, amount in [(0.137, 0.17), (0.271, 0.09)]:
        delay = int(seconds * RATE)
        for i in range(len(buffer)):
            if loop or i >= delay:
                buffer[i] += dry[(i - delay) % len(buffer)] * amount
    peak = max(abs(v) for v in buffer) or 1
    scale = min(1, 0.78 / peak)
    if not loop:
        for i in range(min(220, len(buffer))):
            buffer[i] *= i / 220
            buffer[-1-i] *= i / 220
    with wave.open(str(OUT / (name + '.wav')), 'wb') as f:
        f.setparams((1, 2, RATE, 0, 'NONE', 'not compressed'))
        f.writeframes(b''.join(struct.pack('<h', round(v * scale * 32767)) for v in buffer))


def effect(name, notes, step=0.11, duration=0.34, gain=0.3):
    buffer = [0.0] * int((len(notes) * step + duration + 0.35) * RATE)
    for i, note in enumerate(notes):
        tone(buffer, i * step, duration, note, gain)
    finish(name, buffer)


def music(name, bpm, chords, melody, energy):
    beat = 60 / bpm
    length = 32 * beat  # Eight bars; progression returns naturally to its first chord.
    buffer = [0.0] * round(length * RATE)
    rng = random.Random(13)
    for bar in range(8):
        chord = chords[bar % len(chords)]
        start = bar * 4 * beat
        for note in chord:
            tone(buffer, start, 4.6 * beat, note, 0.055, 'pad', wrap=True)
        for b in range(4):
            tone(buffer, start + b * beat, beat * 0.8, chord[0] - 24,
                 0.10 * energy, 'bass', wrap=True)
        for eighth in range(8):
            note = chord[eighth % len(chord)] + 12
            tone(buffer, start + eighth * beat / 2, beat * 0.8, note,
                 0.035 * energy, wrap=True)
        for b, offset in enumerate(melody[bar % len(melody)]):
            if offset is not None:
                tone(buffer, start + b * beat, beat * 1.4, offset,
                     0.10 * energy, wrap=True)
        # Very soft high-passed noise ticks, rather than harsh square-wave percussion.
        for b in range(4):
            previous = 0.0
            for i in range(int(RATE * 0.055)):
                noise = rng.uniform(-1, 1)
                value = (noise - previous) * math.exp(-i / (RATE * 0.012))
                previous = noise
                index = int((start + (b + 0.5) * beat) * RATE) + i
                buffer[index % len(buffer)] += value * 0.009 * energy
    finish(name, buffer, loop=True)


if __name__ == '__main__':
    OUT.mkdir(parents=True, exist_ok=True)
    effect('gate', [48, 60, 67, 72], 0.07, 0.35)
    effect('dialogue', [76, 79], 0.055, 0.11, 0.16)
    effect('correct', [72, 76, 79, 84], 0.095, 0.42)
    effect('incorrect', [55, 51], 0.14, 0.28, 0.22)
    effect('helper', [67, 74, 79], 0.085, 0.38, 0.23)
    effect('complete', [60, 64, 67, 72, 76, 79], 0.13, 0.65)
    effect('failure', [60, 55, 51, 48], 0.18, 0.48, 0.20)
    effect('click', [81], 0.01, 0.07, 0.12)
    for name, start, end in [('jump', 55, 79), ('respawn', 72, 43)]:
        buffer = [0.0] * int(0.65 * RATE)
        tone(buffer, 0, 0.23, start, 0.2, end=end)
        finish(name, buffer)
    warm = [(60, 64, 67, 71), (57, 60, 64, 67), (53, 57, 60, 64), (55, 59, 62, 67)]
    motif = [(72, None, 76, 79), (76, None, 72, None), (69, 72, None, 76), (74, None, 71, 67)]
    music('splash', 100, warm, motif, 0.85)
    music('menu', 80, warm, [(72, None, None, 76), (None, 71, None, None)], 0.48)
    music('exploration', 72, [(57, 60, 64, 67), (53, 57, 60, 64), (60, 64, 67, 71), (55, 59, 62, 69)],
          [(None, 76, None, None), (None, None, 79, None)], 0.28)
    music('core', 112, [(57, 60, 64, 71), (53, 57, 60, 64), (50, 57, 62, 65), (55, 59, 62, 69)], motif, 1.1)
    music('victory', 116, warm, [(72, 76, 79, 84), (83, None, 79, 76), (77, 81, 84, None), (79, 74, 71, 72)], 1.0)
    print('Created 10 effects and 5 original music loops in', OUT)
