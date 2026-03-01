package com.aqeel.tomatoguess.game;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.resources.AppAudios;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Managing audio playback for the TomatoGuess application, including
 * background music and button sound effects. This class initializes and
 * controls the background music and button sound effects of the application.
 *
 * <p>
 * The class uses paths to the audio files specified in the
 * {@link com.aqeel.tomatoguess.resources.AppAudios} class.
 * </p>
 *
 * <p>
 * The class follows the Singleton design pattern to ensure a single instane is
 * used throughout the game.
 * </p>
 *
 * @author Aqeel Jabir
 *
 */
public class AudioManager {

    private static AudioManager instance = null;

    /**
     * Returns the singleton instance of AudioManager. If the instance is a null, a
     * new instance is created.
     *
     * @return sigleton instance of AudioManager.
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    private Clip backgroundMusic, soundEffects;
    private boolean musicStatus, soundStatus;

    /**
     * Constructs an AudioManger and initializes Background Music and Button Sound
     * Effects. Calls the {@link #initBackgroundMusic()} and
     * {@link #initSoundEffect()} methods.
     */
    public AudioManager() {
        initBackgroundMusic();
        initSoundEffect();
    }

    /**
     * Initializes the background music by loading the audio file and creating Clip
     * object for playback.
     * *
     * <p>
     * This method uses the path to the background music specified in the
     * {@link com.aqeel.tomatoguess.resources.AppAudios} class to create an {@code AudioInputStream} and
     * then opens a {@code Clip} object for the background music.
     * </p>
     *
     * <p>
     * If there is an issue with the audio file (unsupported format, I/O error, or
     * line unavailable), the exception is caught, and the stack trace is printed.
     * </p>
     */
    private void initBackgroundMusic() {
        try {
            // Get the AudioInputStream from the specified background music file.
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                    Objects.requireNonNull(App.class.getResourceAsStream(AppAudios.BACKGROUND_MUSIC)));

            // Create a Clip object from the background music.
            backgroundMusic = AudioSystem.getClip();

            // Open the background music using the obtain AudioInputStream.
            backgroundMusic.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Print the stack trace if there is an issue with the audio file.
            e.printStackTrace();
        }
    }

    /**
     * Initializes the button click sound effects by loading the audio file and
     * creating a Clip object for playback.
     *
     * <p>
     * This method uses the path to the button sound effects specified in the
     * {@link com.aqeel.tomatoguess.resources.AppAudios} class to create an {@code AudioInputStream} and
     * then opens a {@code Clip} object for the sound effects.
     * </p>
     *
     * <p>
     * If there is an issue with the audio file (unsupported format, I/O error, or
     * line unavailable), the exception is caught, and the stack trace is printed.
     * </p>
     */
    private void initSoundEffect() {
        try {
            // Get the AudioIputStream from the specified button sound effect file.
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                    Objects.requireNonNull(App.class.getResourceAsStream(AppAudios.BUTTON_CLICK)));

            // Create a Clip object for the button sound effects.
            soundEffects = AudioSystem.getClip();

            // Open the button sound effects using the obtained AudioInputStream.
            soundEffects.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Print the stack trace if there is an issue with the audio file.
            e.printStackTrace();
        }
    }

    /**
     * Plays the background music if it's not already playing. Background music is
     * set to play continuously in a loop.
     *
     * <p>
     * Checks if the {@link backgroundMusic} Clip is not null and is not currently
     * running. If these conditions are met, the background music is started in a
     * continuous loop using the {@code loop} method, and the volume is set using
     * the {@link #setVolume(Clip, float)} method.
     * </p>
     */
    public void playBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            // Starts playing the background music in a continuous loop.
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

            // Set the volume for the background music
            setVolume(backgroundMusic, -10.0f);
        }
    }

    /**
     * Stop background music if it's currently playing.
     *
     * <p>
     * Checks if the {@link backgroundMusic} Clip is not null and is currently
     * running. If these conditions are met, the background music is stopped using
     * the {@code stop} method.
     * </p>
     */
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            // Stop the currently playing background music.
            backgroundMusic.stop();
        }
    }

    /**
     * Plays the button click sound effect if its enabled. Resets the frame position
     * to the beginning before playing.
     *
     * <p>
     * Checks if sound effects are enabled ({@link soundStatus} is true). If enabled
     * and the {@link soundEffects} Clip is not null, it stops the sound, resets the
     * frame position to the beginning, and starts playing the sound effect.
     * </p>
     */
    public void playButtonSoundEffects() {
        if (soundStatus) {
            if (soundEffects != null) {
                // Stops the button sound effects if its currently running.
                soundEffects.stop();

                // Reset the frame position to the beginning.
                soundEffects.setFramePosition(0);

                // Start playing the button sound effect.
                soundEffects.start();
            }
        }
    }

    /**
     * Toggles the background music on or off based on the current status.
     *
     * <p>
     * If the background music is currently enabled ({@link #isMusicStatus()}
     * returns true),
     * it calls the {@link #playBackgroundMusic()} method to start playing the
     * background music. If the background music is disabled, it calls the
     * {@link #stopBackgroundMusic()} method to stop the background music.
     * </p>
     */
    public void toggleBackgroundMusic() {
        if (isMusicStatus()) {
            playBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }

    /**
     * Sets the volume for the background music and button sound effects. Adjusts
     * the volume using FloatControl to set the gain.
     *
     * @param clip   for which the volume is set.
     * @param volume level to be set.
     */
    public void setVolume(Clip clip, float volume) {
        if (clip != null) {
            // Get the FloatControl for adjusting the volume.
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Set the volume level.
            gainControl.setValue(volume);
        }
    }

    /**
     * Returns the current status of the background music.
     *
     * @return true if background music is playing else false.
     */
    public boolean isMusicStatus() {
        return musicStatus;
    }

    /**
     * Sets the status of the background music.
     *
     * @param musicStatus true to play background music else false to stop it.
     */
    public void setMusicStatus(boolean musicStatus) {
        this.musicStatus = musicStatus;
    }

    /**
     * Returns the current status of the button sound effects.
     *
     * @return true if button sound effects are enabled else false.
     */
    public boolean isSoundStatus() {
        return soundStatus;
    }

    /**
     * Sets the status of the button sound effects.
     *
     * @param soundStatus true to enable button sound effects else false to disable
     *                    them.
     */
    public void setSoundStatus(boolean soundStatus) {
        this.soundStatus = soundStatus;
    }
}
