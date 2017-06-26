package de.hsd.hacking.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import de.hsd.hacking.Assets.Assets;

/**
 * Created by Cuddl3s on 21.04.2017.
 */

public class Constants {

    public static final String TAG = "HackingGame";

    public static boolean DEBUG = false;

    public static final int APP_WIDTH = 1024;
    public static final int APP_HEIGHT = 576;

    public static final int TILE_WIDTH = 32;
    public static final int TILES_PER_SIDE = 13;

    //UI

//    private static Assets assets;
    private static Skin uiSkin;
    private static TextButton.TextButtonStyle textButtonStyle;
    private static Label.LabelStyle labelStyle;
//    private static Label.LabelStyle tinyLabelStyle;

//    public static void SetAssets(Assets assets) {
//        if (Constants.assets == null) Constants.assets = assets;
//    }

//    public static Assets GetAssets(){ return assets;}

    public static Skin UiSkin() {
        if (uiSkin == null) {
            if (Assets.instance() == null) {
                Gdx.app.log(Constants.TAG, "Error: Assets null!");
            }
            uiSkin = new Skin(Assets.instance().ui_atlas);
        }

        return uiSkin;
    }

    public static TextButton.TextButtonStyle TextButtonStyle() {
        if (textButtonStyle == null) {
            textButtonStyle = new TextButton.TextButtonStyle(UiSkin().getDrawable("win32_button_9_patch_normal"), UiSkin().getDrawable("win32_button_9_patch_pressed"),
                    null, Assets.instance().status_bar_font);

            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.pressedOffsetY = -1f;
            textButtonStyle.pressedOffsetX = 1f;
        }

        return textButtonStyle;
    }

    public static Label.LabelStyle LabelStyle() {
        if (labelStyle == null) {
            labelStyle = new Label.LabelStyle();
            labelStyle.font = Assets.instance().status_bar_font;
            labelStyle.fontColor = Color.BLACK;
        }

        return labelStyle;
    }

//    public static Label.LabelStyle TinyLabelStyle() {
//        if (tinyLabelStyle == null) {
//            tinyLabelStyle = new Label.LabelStyle();
//            tinyLabelStyle.font = Assets.instance().status_bar_font;
//            tinyLabelStyle.fontColor = Color.BLACK;
//        }
//
//        return tinyLabelStyle;
//    }
}
