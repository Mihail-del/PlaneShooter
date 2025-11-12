import javax.sound.sampled.*;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Simple sound manager for WAV (PCM) clips. Non-deprecated, Java 9+ friendly. */
public final class SoundManager {
    // Cache of loaded sounds: name -> Clip
    private static final Map<String, Clip> clips =
            Collections.synchronizedMap(new HashMap<>());

    private SoundManager() {}

    /** Load a WAV from classpath (e.g. "/sounds/explosion.wav"). */
    public static void loadFromResource(String name, String resourcePath) {
        try {
            URL url = SoundManager.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("[SoundManager] Resource not found: " + resourcePath);
                return;
            }
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(url)) {
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clips.put(name, clip);
            }
        } catch (Exception e) {
            System.err.println("[SoundManager] Failed to load: " + resourcePath);
            e.printStackTrace();
        }
    }

    /** Play from start (non-blocking). If already playing - restarts. */
    public static void play(String name) {
        Clip c = clips.get(name);
        if (c == null) {
            System.err.println("[SoundManager] Not loaded: " + name);
            return;
        }
        c.setFramePosition(0);
        c.start();
    }

    /** Loop continuously from start. */
    public static void loop(String name) {
        Clip c = clips.get(name);
        if (c == null) {
            System.err.println("[SoundManager] Not loaded: " + name);
            return;
        }
        c.stop();
        c.setFramePosition(0);
        c.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /** Stop if playing. */
    public static void stop(String name) {
        Clip c = clips.get(name);
        if (c != null) c.stop();
    }

    /** Set volume in decibels for a single clip (typical range: -80..+6 dB). */
    public static void setVolumeDb(String name, float dB) {
        Clip c = clips.get(name);
        if (c == null) return;
        if (c.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            ((FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN)).setValue(dB);
        }
    }

    /** Convenience: set volume in [0..1] linear. 1.0 = 0 dB, 0.5 ≈ -6 dB, etc. */
    public static void setVolumeLinear(String name, double vol01) {
        vol01 = Math.max(0.0001, Math.min(1.0, vol01));
        float dB = (float) (20.0 * Math.log10(vol01)); // convert to dB
        setVolumeDb(name, dB);
    }
}
