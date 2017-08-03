package de.hsd.hacking.Assets;

import com.badlogic.gdx.utils.Timer;
import com.sun.net.httpserver.Authenticator;

import de.hsd.hacking.Entities.Employees.EmojiBubbleFactory;

public class AudioManager {

    private static AudioManager instance = null;

    private Assets assets;

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
        assets.uiButtonSound.play();
    }

    public void playMenuButtonSound() {
        assets.menuButtonSound.play();
    }


    public void playMusic() {
        assets.gameMusic.play();
        assets.gameMusic.setLooping(true);
    }

    public void stopMusic() {
        assets.gameMusic.stop();
    }

    public void playEmojiSound(EmojiBubbleFactory.EmojiType type) {
        switch(type) {
            case SUCCESS:
                assets.emojiSuccessSound.play();
                break;
            case FAILURE:
                assets.emojiFailureSound.play();
                break;
            case OK:
                assets.emojiOkSound.play();
                break;
            case NO:
                assets.emojiNoSound.play();
                break;
            case SPEAKING:
                assets.emojiSpeakingSound.play();
                break;
            case LEVELUP:
                assets.emojiLevelUpSound.play();
                break;
        }
    }

    public void playNotificationSound() {
        assets.notificationSound.play();
    }


}
