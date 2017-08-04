package de.hsd.hacking.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.hsd.hacking.Assets.Assets;

/**
 * Class holding constant values.
 * @author Florian, Hendrik
 */
public class Constants {

    public static final String TAG = "HackingGame";
    public static final float TIME_STEPS_PER_DAY = 9;

    public static boolean DEBUG = false;

    public static final float VIEWPORT_WIDTH = 512f;
    public static final float VIEWPORT_HEIGHT = (Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / VIEWPORT_WIDTH));

    public static final int APP_WIDTH = 1024;
    public static final int APP_HEIGHT = 576;

    public static final int TILE_WIDTH = 32;
    public static final int TILES_PER_SIDE = 13;

    public static final int MAX_EMPLOYEE_COUNT = 4;
    public static final int STARTING_TEAM_SIZE = 2;
    public static final int STARTING_MONEY = 8000;

    //UI

    private static Skin uiSkin;
    private static TextButton.TextButtonStyle textButtonStyle, tabButtonStyle, terminalButtonStyle;
    private static ScrollPane.ScrollPaneStyle scrollPaneStyleWin32, scrollPaneStyleTerminal;
    private static Label.LabelStyle labelStyle;
    private static Label.LabelStyle tinyLabelStyle;
    private static Label.LabelStyle terminalLabelStyle;

    /**
     * The date format we want for the ui.
     */
    public static final DateFormat dateFormatBar = new SimpleDateFormat("dd MMM");
    public static final DateFormat dateFormatMessage = new SimpleDateFormat("dd.MM");

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

    public static TextButton.TextButtonStyle TerminalButtonStyle() {
        if (terminalButtonStyle == null) {
            terminalButtonStyle = new TextButton.TextButtonStyle(UiSkin().getDrawable("terminal_button_normal"), UiSkin().getDrawable("terminal_button_pressed"),
                    null, Assets.instance().status_bar_font);

            terminalButtonStyle.fontColor = Color.BLACK;
            terminalButtonStyle.pressedOffsetY = -1f;
            terminalButtonStyle.pressedOffsetX = 1f;
        }

        return terminalButtonStyle;
    }

    public static ScrollPane.ScrollPaneStyle ScrollPaneStyleWin32() {
        if (scrollPaneStyleWin32 == null) {
            scrollPaneStyleWin32 = new ScrollPane.ScrollPaneStyle();

            scrollPaneStyleWin32.vScrollKnob = UiSkin().getDrawable("win32_button_9_patch_normal");
            scrollPaneStyleWin32.hScrollKnob = UiSkin().getDrawable("win32_button_9_patch_normal");
            scrollPaneStyleWin32.vScroll = UiSkin().getDrawable("win32_scoll_background");
            scrollPaneStyleWin32.hScroll = UiSkin().getDrawable("win32_scoll_background");
        }

        return scrollPaneStyleWin32;
    }

    public static ScrollPane.ScrollPaneStyle ScrollPaneStyleTerminal() {
        if (scrollPaneStyleTerminal == null) {
            scrollPaneStyleTerminal = new ScrollPane.ScrollPaneStyle();

            scrollPaneStyleTerminal.vScrollKnob = UiSkin().getDrawable("terminal_scroll_button");
            scrollPaneStyleTerminal.hScrollKnob = UiSkin().getDrawable("terminal_scroll_button");
            scrollPaneStyleTerminal.vScroll = UiSkin().getDrawable("terminal_scroll_background");
            scrollPaneStyleTerminal.hScroll = UiSkin().getDrawable("terminal_scroll_background");
        }

        return scrollPaneStyleTerminal;
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
