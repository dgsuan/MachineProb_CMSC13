# SYSTEMBOUND audio

Original synthesized music and effects created for this project. No external
recordings, third-party samples, or downloaded music are used. The source is
`tools/generate_audio.py` (Python standard library). Run it to regenerate all WAVs.

The design follows section 3.2 of Group10_DeypalanSuan_A1:

| Proposal event | Asset | Character |
| --- | --- | --- |
| Splash | splash.wav | Soft electronic identity motif |
| Menu | menu.wav | Calm, sparse variation |
| Exploration | exploration.wav | Subtle pads and bell accents |
| Gate interaction | gate.wav | Digital activation arpeggio |
| New question | dialogue.wav | Brief Kernel chirp |
| Correct answer | correct.wav | Rising confirmation chime |
| Incorrect answer | incorrect.wav | Gentle descending error tone |
| Helper activated | helper.wav | Friendly assistance chime |
| Trial complete | complete.wav | Ascending achievement flourish |
| Trial failed | failure.wav | Soft descending system response |
| Core gate/trial | core.wav | Faster, more dramatic identity motif |
| Final success | victory.wav | Bright major-key celebration |

Additional feedback: click.wav for buttons, jump.wav for successful jumps,
respawn.wav for pitfall respawns. Effects play on events, never on every frame.
Question chirps only play for a new question, not when rebuilding its screen.

All files are mono, 22,050 Hz, signed 16-bit PCM WAV. Music has eight-bar loops
with wrapped note/delay tails. Effects have short envelopes and tail fades.
Music crossfades over 650 ms and is quieter during questions. F7 mutes both
music and effects; the setting is stored with the player's existing progress.
Audio playback is released when the application stops. Missing or unavailable
audio is logged without blocking the game.
