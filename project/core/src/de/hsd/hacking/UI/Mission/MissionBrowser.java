package de.hsd.hacking.UI.Mission;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.UI.General.TabbedView;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 22.06.17.
 */

public class MissionBrowser extends Popup {
    private static final int SCROLLER_WIDTH = 400;
    private static final int SCROLLER_HEIGHT = 172;
    private static final int SCROLLER_ELEMENT_PADDING = 5;

    private Table openMissions, activeMissions;

    private Label title;

    private Table openMissionContainer, activeMissionsContainer;
    private ScrollPane openMissionScroller, activeMissionScroller;


    public MissionBrowser() {
        super();

        initTable();
    }

    @Override
    public void act(float delta) {
        if (!isActive()) {
            return;
        }

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isActive()) {
            return;
        }

        super.draw(batch, parentAlpha);
    }

    private void initTable() {
        // Setup open Missions table
        openMissions = new Table();
        openMissions.setName("Open");
        openMissions.align(Align.top);
        openMissions.setTouchable(Touchable.enabled);
        openMissions.setBackground(Assets.instance().tab_view_border_patch);

        // Setup running missions table
        activeMissions = new Table();
        activeMissions.setName("Active");
        activeMissions.align(Align.top);
        activeMissions.setTouchable(Touchable.enabled);
        activeMissions.setBackground(Assets.instance().tab_view_border_patch);

        // Add everything to the tables
        openMissionContainer = new Table();
        openMissionScroller = new ScrollPane(openMissionContainer);

        for (final Mission mission : MissionManager.instance().getOpenMissions()) {
            openMissionContainer.add(new MissionUIElement(mission)).expandX().fillX().padTop(SCROLLER_ELEMENT_PADDING).padBottom(SCROLLER_ELEMENT_PADDING).row();
        }

        openMissions.add(openMissionScroller).expand().fill().maxHeight(SCROLLER_HEIGHT).prefWidth(SCROLLER_WIDTH).maxWidth(SCROLLER_WIDTH).pad(SCROLLER_ELEMENT_PADDING);

        activeMissionsContainer = new Table();
        activeMissionScroller = new ScrollPane(activeMissionsContainer);

        for (final Mission mission : MissionManager.instance().getActiveMissions()) {
            activeMissionsContainer.add(new MissionUIElement(mission)).expandX().fillX().padTop(SCROLLER_ELEMENT_PADDING).padBottom(SCROLLER_ELEMENT_PADDING).row();
        }

        activeMissions.add(activeMissionScroller).expand().fill().maxHeight(SCROLLER_HEIGHT).prefWidth(SCROLLER_WIDTH).maxWidth(SCROLLER_WIDTH).pad(SCROLLER_ELEMENT_PADDING);

        // Setup tabbed view
        ArrayList<Actor> views = new ArrayList<Actor>();
        views.add(activeMissions);
        views.add(openMissions);
        TabbedView tabbedView = new TabbedView(views);

        // Set tabbed view as main view
        this.addMainContent(tabbedView);
    }
}
