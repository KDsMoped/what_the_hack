package de.hsd.hacking.Assets;

import com.badlogic.gdx.utils.Timer;
import com.sun.net.httpserver.Authenticator;

import de.hsd.hacking.Entities.Employees.EmojiBubbleFactory;


/**
 * The AudioManager manages the games Sounds and Music.
 * @author Dominik
 */

public class AudioManager {

    private static AudioManager instance = null;

    private Assets assets;

    private float globalVolume = 1;

    public AudioManager() {
        assets = Assets.instance();
    }

    public static AudioManager instance() {
        if(instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playUIButtonSound() {
        assets.uiButtonSound.play(globalVolume);
    }

    public void playBuyButtonSound() {
        assets.buyButtonSound.play(globalVolume);
    }

    public void playMenuButtonSound() {
        assets.menuButtonSound.play(globalVolume);
    }


    /**
     * Play the Background Music in a loop.
     */
    public void playMusic() {
        assets.gameMusic.play();
        assets.gameMusic.setVolume(globalVolume);
        assets.gameMusic.setLooping(true);
    }

    public void stopMusic() {
        assets.gameMusic.stop();
    }

    /**
     * Plays the Sound for the associated Emoji type.
     * @param type the Emoji type to play the Sound of.
     */
    public void playEmojiSound(EmojiBubbleFactory.EmojiType type) {
        switch(type) {
            case SUCCESS:
                assets.emojiSuccessSound.play(globalVolume);
                break;
            case FAILURE:
                assets.emojiFailureSound.play(globalVolume);
                break;
            case OK:
                assets.emojiOkSound.play(globalVolume);
                break;
            case NO:
                assets.emojiNoSound.play(globalVolume);
                break;
            case SPEAKING:
                assets.emojiSpeakingSound.play(globalVolume);
                break;
            case LEVELUP:
                assets.emojiLevelUpSound.play(globalVolume);
                break;
        }
    }

    public void playNotificationSound() {
        assets.notificationSound.play(globalVolume);
    }

    /**
     * Set the global volume as float value between 0 and 1. Exceeding param values will be discarded.
     * @param globalVolume
     */
    public void setGlobalVolume(float globalVolume) {
        if(0.f <= globalVolume && globalVolume <= 1.f ) {
            this.globalVolume = globalVolume;
        }
    }

    /**
     * Set the global volume as float value between 0 and 1.
     * @return globalVolume
     */
    public float getGlobalVolume() {
        return globalVolume;
    }


}
