package de.hsd.hacking.UI.General;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 14.06.17.
 */

/**
 * Abstract class for a general purpose popup window.
 */
// TODO Transparent unclickable background
public abstract class Popup extends Group {
    private final int POPUP_MARGIN_DEFAULT = 20;

    protected Table mainTable = new Table();
    protected TextButton closeButton;

    private VerticalGroup content = new VerticalGroup();

    //private TextButton.TextButtonStyle buttonStyle;
    //private Label.LabelStyle labelStyle;

    public Popup(int popupMargin) {
        init(popupMargin);
    }

    /**
     * We need the ui assets to display a beautiful popup window.
     */
    public Popup() {
        init(POPUP_MARGIN_DEFAULT);
    }

    private void init(int popupMargin) {
        Assets assets = Assets.instance();

        mainTable.align(Align.top);
        // We want a margin around the popup window
        mainTable.setHeight(GameStage.VIEWPORT_HEIGHT - 2 * popupMargin);
        mainTable.setWidth(GameStage.VIEWPORT_WIDTH - 2 * popupMargin);

        // And we want to center the popup on the screen
        mainTable.setPosition(popupMargin, popupMargin);
        mainTable.setBackground(assets.win32_patch);
        mainTable.setTouchable(Touchable.enabled);
        mainTable.setVisible(false);

        // Setup close button
        closeButton = new TextButton("OK", Constants.TextButtonStyle());
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });

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
    protected void show() {
        mainTable.setVisible(true);
    }

    /**
     * Disables act and draw for the popup window.
     */
    protected void close() {
        mainTable.setVisible(false);
    }

    public void toggleView() {
        if (mainTable.isVisible()) close();
        else show();
    }

    public void addMainContent(Actor content) {
        this.content.addActor(content);
    }

    public boolean isActive() {
        return mainTable.isVisible();
    }

    protected Table getMainTable() {
        return this.mainTable;
    }


    public VerticalGroup getContent() {
        return content;
    }
}
