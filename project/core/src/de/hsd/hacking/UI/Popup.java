package de.hsd.hacking.UI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
public abstract class Popup extends Actor {
    private final int POPUP_MARGIN = 20;

    private Assets assets;
    private Table content = new Table();

    private boolean isActive = false;

    private TextButton.TextButtonStyle buttonStyle;
    private TextButton closeButton;

    /**
     * We need the ui assets to display a beautiful popup window.
     * @param assets Assets that contain the ui style.
     */
    public Popup(Assets assets) {
        this.assets = assets;

        content.align(Align.bottom);
        // We want a margin around the popup window
        content.setHeight(GameStage.VIEWPORT_HEIGHT - 2 * POPUP_MARGIN);
        content.setWidth(GameStage.VIEWPORT_WIDTH - 2 * POPUP_MARGIN);

        // And we want to center the popup on the screen
        content.setPosition(POPUP_MARGIN, POPUP_MARGIN);
        content.setBackground(assets.terminal_patch);

        // Setup Button Style
        Skin uiSkin = new Skin(assets.ui_atlas);
        buttonStyle = new TextButton.TextButtonStyle(uiSkin.getDrawable("Button_9_Patch_normal"), uiSkin.getDrawable("Button_9_Patch_pressed"),
                null, assets.status_bar_font);
        buttonStyle.pressedOffsetY = -5f;

        // Setup close button
        closeButton = new TextButton("OK", buttonStyle);
        closeButton.addListener(new ChangeListener() {
                               @Override
                               public void changed(ChangeEvent event, Actor actor) {
                                   Close();
                               }
                           }
        );
        closeButton.setWidth(50);
        closeButton.setHeight(20);

        // Table layout
        content.row();
        content.add(closeButton).padBottom(4f);
    }

    @Override
    public void act(float delta) {
        if (!isActive) {
            return;
        }

        super.act(delta);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isActive) {
            return;
        }

        super.draw(batch, parentAlpha);

        content.draw(batch, parentAlpha);
    }

    /**
     * Enables act and draw for the popup window.
     */
    public void Show() {
        isActive = true;
    }

    /**
     * Disables act and draw for the popup window.
     */
    public void Close() {
        isActive = false;
    }

    public boolean getActive() {
        return isActive;
    }
}
