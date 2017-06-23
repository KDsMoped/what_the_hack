package de.hsd.hacking.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by ju on 14.06.17.
 */

/**
 * Abstract class for a general purpose popup window.
 */
public abstract class Popup extends Group {
    private final int POPUP_MARGIN = 20;

    private Assets assets;
    private Table mainTable = new Table();

    private VerticalGroup content = new VerticalGroup();

    private TextButton.TextButtonStyle buttonStyle;
    private Skin uiSkin;
    private Label.LabelStyle labelStyle;

    private TextButton closeButton;


    /**
     * We need the ui assets to display a beautiful popup window.
     * @param assets Assets that contain the ui style.
     */
    public Popup(Assets assets) {
        this.assets = assets;

        mainTable.align(Align.top);
        // We want a margin around the popup window
        mainTable.setHeight(GameStage.VIEWPORT_HEIGHT - 2 * POPUP_MARGIN);
        mainTable.setWidth(GameStage.VIEWPORT_WIDTH - 2 * POPUP_MARGIN);

        // And we want to center the popup on the screen
        mainTable.setPosition(POPUP_MARGIN, POPUP_MARGIN);
        mainTable.setBackground(assets.win32_patch);
        mainTable.setTouchable(Touchable.enabled);
        mainTable.setVisible(false);

        // Setup Button Style
        uiSkin = new Skin(assets.ui_atlas);
        buttonStyle = new TextButton.TextButtonStyle(uiSkin.getDrawable("win32_button_9_patch_normal"), uiSkin.getDrawable("win32_button_9_patch_pressed"),
                null, assets.status_bar_font);
        buttonStyle.pressedOffsetY = -1f;
        buttonStyle.pressedOffsetX = 1f;
        buttonStyle.fontColor = Color.BLACK;

        // Setup label style
        labelStyle = new Label.LabelStyle();
        labelStyle.font = assets.status_bar_font;
        labelStyle.fontColor = Color.BLACK;

        // Setup close button
        closeButton = new TextButton("OK", buttonStyle);
        closeButton.addListener(new ChangeListener() {
               @Override
               public void changed(ChangeEvent event, Actor actor) {
                   Close();
               }
           }
        );

        // Content container setup
        content.setTouchable(Touchable.enabled);
        content.align(Align.top);

        // Table layout
        mainTable.add(content).expand().fill();
        mainTable.row();
        mainTable.add(closeButton).padBottom(4f).width(50).height(23);
        addActor(mainTable);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    /**
     * Enables act and draw for the popup window.
     */
    public void Show() {
        mainTable.setVisible(true);
    }

    /**
     * Disables act and draw for the popup window.
     */
    public void Close() {
        mainTable.setVisible(false);
    }

    public boolean isActive() {
        return mainTable.isVisible();
    }

    public Table getMainTable() {
        return this.mainTable;
    }

    public TextButton.TextButtonStyle getButtonStyle() {
        return buttonStyle;
    }

    public Skin getUiSkin() {
        return uiSkin;
    }

    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }

    public VerticalGroup getContent() {
        return content;
    }

    public Assets getAssets() {
        return assets;
    }
}
