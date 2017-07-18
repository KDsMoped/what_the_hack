package de.hsd.hacking.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.hsd.hacking.Assets.Assets;

/**
 * Created by Cuddl3s on 21.04.2017.
 */

public class Constants {

    public static final String TAG = "HackingGame";
    public static final float TIME_STEPS_PER_DAY = 9;

    public static boolean DEBUG = false;

    public static final int APP_WIDTH = 1024;
    public static final int APP_HEIGHT = 576;

    public static final int TILE_WIDTH = 32;
    public static final int TILES_PER_SIDE = 13;

    public static final int MAX_EMPLOYEE_COUNT = 4;
    public static final int STARTING_TEAM_SIZE = 2;
    public static final int STARTING_MONEY = 15000;

    //UI

    private static Skin uiSkin;
    private static TextButton.TextButtonStyle textButtonStyle, tabButtonStyle;
    private static Label.LabelStyle labelStyle;
    private static Label.LabelStyle tinyLabelStyle;
    private static Label.LabelStyle terminalLabelStyle;

    /**
     * The date format we want for the ui.
     */
    public static final DateFormat dateFormat = new SimpleDateFormat("dd MMM");

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

    public static TextButton.TextButtonStyle TabButtonStyle() {
        if (tabButtonStyle == null) {
            tabButtonStyle = new TextButton.TextButtonStyle(UiSkin().getDrawable("win32_tabs_normal")
                    , null, UiSkin().getDrawable("win32_tabs_checked"), Assets.instance().status_bar_font);

            tabButtonStyle.fontColor = Color.BLACK;
        }

        return tabButtonStyle;
    }

    public static Label.LabelStyle LabelStyle() {
        if (labelStyle == null) {
            labelStyle = new Label.LabelStyle();
            labelStyle.font = Assets.instance().status_bar_font;
            labelStyle.fontColor = Color.BLACK;
        }

        return labelStyle;
    }

    public static Label.LabelStyle TinyLabelStyle() {
        if (tinyLabelStyle == null) {
            tinyLabelStyle = new Label.LabelStyle();
            tinyLabelStyle.font = Assets.instance().tiny_label_font;
            tinyLabelStyle.fontColor = Color.BLACK;
        }

        return tinyLabelStyle;
    }

    public static Label.LabelStyle TerminalLabelStyle() {
        if (terminalLabelStyle == null) {
            terminalLabelStyle = new Label.LabelStyle();
            terminalLabelStyle.font = Assets.instance().status_bar_font;
            terminalLabelStyle.fontColor = new Color(41f/255f, 230f/255f, 41f/255f, 1f);
        }

        return terminalLabelStyle;
    }
}
