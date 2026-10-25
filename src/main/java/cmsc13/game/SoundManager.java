package cmsc13.game;

import java.net.URL;
import java.util.EnumMap;
import java.util.EnumSet;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/** Owns audio playback; missing assets or unavailable audio never interrupt gameplay. */
public final class SoundManager {
    public enum Effect { GATE, DIALOGUE, CORRECT, INCORRECT, HELPER, COMPLETE, FAILURE, CLICK, JUMP, RESPAWN }
    public enum Music { SPLASH, MENU, EXPLORATION, CORE, VICTORY }

    private final EnumMap<Effect, AudioClip> clips = new EnumMap<>(Effect.class);
    private final EnumMap<Effect, Long> lastPlayed = new EnumMap<>(Effect.class);
    private final EnumSet<Effect> unavailable = EnumSet.noneOf(Effect.class);
    private MediaPlayer musicPlayer;
    private MediaPlayer retiringPlayer;
    private Timeline fade;
    private Music currentMusic;
    private double currentVolume = -1;
    private boolean muted;

    public static String resource(Enum<?> sound) {
        return "/Audio/" + sound.name().toLowerCase(java.util.Locale.ROOT) + ".wav";
    }

    static Music musicFor(GameState state, boolean coreTrial) {
        switch (state) {
            case LOADING: return Music.SPLASH;
            case VICTORY: return Music.VICTORY;
            case WORLD: return Music.EXPLORATION;
            case TRIAL_CONFIRM:
            case TRIAL:
            case RESULT: return coreTrial ? Music.CORE : Music.EXPLORATION;
            default: return Music.MENU;
        }
    }

    /** Called as state changes; unchanged tracks continue without restarting. */
    public void updateMusic(GameState state, boolean coreTrial) {
        Music next = musicFor(state, coreTrial);
        boolean dialogue = state == GameState.TRIAL || state == GameState.TRIAL_CONFIRM;
        double volume = dialogue ? 0.22 : 0.38;
        if (next == currentMusic && volume == currentVolume) return;
        currentVolume = volume;
        if (next == currentMusic) {
            if (musicPlayer != null) musicPlayer.setVolume(volume);
            return;
        }
        currentMusic = next;
        if (fade != null) fade.stop();
        if (retiringPlayer != null) retiringPlayer.dispose();
        retiringPlayer = musicPlayer;
        musicPlayer = null;
        try {
            URL url = SoundManager.class.getResource(resource(next));
            if (url == null) throw new IllegalStateException("Missing " + resource(next));
            MediaPlayer player = new MediaPlayer(new Media(url.toExternalForm()));
            musicPlayer = player;
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setMute(muted);
            player.setVolume(0);
            player.setOnError(() -> {
                System.err.println("Music unavailable: " + next + ": " + player.getError());
                player.dispose();
                if (musicPlayer == player) musicPlayer = null;
            });
            player.setOnReady(() -> {
                if (musicPlayer != player) return;
                player.play();
                fade = new Timeline(new KeyFrame(Duration.millis(650),
                    new KeyValue(player.volumeProperty(), currentVolume)));
                if (retiringPlayer != null) {
                    fade.getKeyFrames().add(new KeyFrame(Duration.millis(650),
                        new KeyValue(retiringPlayer.volumeProperty(), 0)));
                }
                fade.setOnFinished(event -> {
                    if (retiringPlayer != null) { retiringPlayer.dispose(); retiringPlayer = null; }
                });
                fade.play();
            });
        } catch (RuntimeException exception) {
            System.err.println("Music unavailable: " + next + ": " + exception.getMessage());
            if (musicPlayer != null) { musicPlayer.dispose(); musicPlayer = null; }
            if (retiringPlayer != null) { retiringPlayer.dispose(); retiringPlayer = null; }
        }
    }

    public void play(Effect effect) {
        if (muted || unavailable.contains(effect)) return;
        long now = System.nanoTime();
        if (now - lastPlayed.getOrDefault(effect, 0L) < 80_000_000L) return;
        lastPlayed.put(effect, now);
        try {
            AudioClip clip = clips.get(effect);
            if (clip == null) {
                URL url = SoundManager.class.getResource(resource(effect));
                if (url == null) throw new IllegalStateException("Missing " + resource(effect));
                clip = new AudioClip(url.toExternalForm());
                clips.put(effect, clip);
            }
            clip.stop(); // Rapid repeated actions cannot pile up the same effect.
            clip.play(effect == Effect.CLICK || effect == Effect.DIALOGUE ? 0.42 : 0.65);
        } catch (RuntimeException exception) {
            unavailable.add(effect);
            System.err.println("Sound unavailable: " + effect + ": " + exception.getMessage());
        }
    }

    public boolean isMuted() { return muted; }

    public void setMuted(boolean value) {
        muted = value;
        if (musicPlayer != null) musicPlayer.setMute(value);
        if (retiringPlayer != null) retiringPlayer.setMute(value);
        if (value) clips.values().forEach(AudioClip::stop);
    }

    public void stop() {
        if (fade != null) fade.stop();
        clips.values().forEach(AudioClip::stop);
        if (musicPlayer != null) { musicPlayer.dispose(); musicPlayer = null; }
        if (retiringPlayer != null) { retiringPlayer.dispose(); retiringPlayer = null; }
        currentMusic = null;
    }
}
