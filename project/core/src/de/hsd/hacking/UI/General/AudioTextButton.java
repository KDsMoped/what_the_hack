package de.hsd.hacking.UI.General;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.hsd.hacking.Assets.AudioManager;

import static java.awt.SystemColor.text;

/**
 * A {@link TextButton} that plays a click sound when pressed
 * @author Dominik
 */

public class AudioTextButton extends TextButton {
    private boolean buyButton = false;

    public AudioTextButton(String text, Skin skin) {
        super(text, skin);
        addListenerWithSound();
    }

    public AudioTextButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        addListenerWithSound();
    }

    public AudioTextButton(String text, TextButtonStyle style) {
        super(text, style);
        addListenerWithSound();
    }

    private void addListenerWithSound(){
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (buyButton) {
                    AudioManager.instance().playBuyButtonSound();
                }
                else {
                    AudioManager.instance().playUIButtonSound();
                }
            }
        });
    }

    public void setBuyButton(boolean buyButton) {
        this.buyButton = buyButton;
    }
}
