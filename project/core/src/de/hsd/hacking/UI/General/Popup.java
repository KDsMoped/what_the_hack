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

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 14.06.17.
 */

/**
 * Abstract class for a general purpose popup window.
 */
public abstract class Popup extends Group {
    private final int POPUP_MARGIN_DEFAULT = 20;
    protected static final int SCROLLER_WIDTH = 420;
    protected static final int SCROLLER_HEIGHT = 195;
    protected static final int SCROLLER_ELEMENT_PADDING = 5;

    protected Table mainTable = new Table();
    protected Table noBackgroundClick = new Table();
    protected TextButton closeButton;

    private VerticalGroup content = new VerticalGroup();

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
        closeButton = new TextButton("X", Constants.TextButtonStyle());
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });
        closeButton.align(Align.center);

        // Content container setup
        content.setTouchable(Touchable.enabled);
        content.align(Align.top);

        // Table layout
        mainTable.add(content).expand().fill().padRight(-20);
        mainTable.add(closeButton).top().right().pad(1f).padTop(-6f).padRight(2f).width(20).height(20);

        // No Background Click
        noBackgroundClick.setVisible(false);
        noBackgroundClick.setHeight(GameStage.VIEWPORT_HEIGHT);
        noBackgroundClick.setWidth(GameStage.VIEWPORT_WIDTH);
        noBackgroundClick.setPosition(0, 0);
        noBackgroundClick.setTouchable(Touchable.enabled);
        noBackgroundClick.setBackground(assets.table_dimm_patch);

        addActor(noBackgroundClick);
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
        noBackgroundClick.setVisible(true);
    }

    /**
     * Disables act and draw for the popup window.
     */
    public void close() {
        mainTable.setVisible(false);
        noBackgroundClick.setVisible(false);
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
