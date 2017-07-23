package de.hsd.hacking.Assets;

/**
 * Created by domin on 22.07.2017.
 */

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

    public void playMusicLoop() {

    }



}
