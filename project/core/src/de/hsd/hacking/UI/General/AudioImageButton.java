package de.hsd.hacking.UI.General;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

import de.hsd.hacking.Assets.AudioManager;

/**
 * Created by domin on 04.08.2017.
 */

public class AudioImageButton extends ImageButton {

    public AudioImageButton (Skin skin) {
        super(skin);
        addListenerWithSound();
    }

    public AudioImageButton (Skin skin, String styleName) {
        super(skin, styleName);
        addListenerWithSound();
    }

    public AudioImageButton (ImageButtonStyle style) {
        super(style);
        addListenerWithSound();
    }

    public AudioImageButton (Drawable imageUp) {
       super(imageUp);
        addListenerWithSound();
    }

    public AudioImageButton (Drawable imageUp, Drawable imageDown) {
        super(imageUp, imageDown);
        addListenerWithSound();
    }

    public AudioImageButton (Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
        super(imageUp, imageDown, imageChecked);
        addListenerWithSound();
    }

    private void addListenerWithSound(){
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.instance().playUIButtonSound();
            }
        });
    }
}
