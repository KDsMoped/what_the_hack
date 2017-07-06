package de.hsd.hacking.UI.Mission;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.UI.General.TabbedView;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 22.06.17.
 */

/**
 * Popup mission browser for open and active missions.
 */
public class MissionBrowser extends Popup {
    private static final int SCROLLER_WIDTH = 400;
    private static final int SCROLLER_HEIGHT = 172;
    private static final int SCROLLER_ELEMENT_PADDING = 5;

    private Table openMissions, activeMissions, completedMissions;

    private Label title;

    private Table openMissionContainer, activeMissionsContainer, completedMissionsContainer;
    private ScrollPane openMissionScroller, activeMissionScroller, completedMissionsScroller;


    public MissionBrowser() {
        super();

        initTable();

        MissionManager.instance().addRefreshMissionListener(new Callback() {
            @Override
            public void callback() {
                refreshList();
            }
        });
    }

    private void initTable() {
        // Setup open Missions table
        openMissions = new Table();
        initSubTable(openMissions);
        openMissions.setName("Open");


        // Setup running missions table
        activeMissions = new Table();
        initSubTable(activeMissions);
        activeMissions.setName("Active");

        // Setup completed missions table
        completedMissions = new Table();
        initSubTable(completedMissions);
        completedMissions.setName("Completed");


        // Add everything to the helper tables
        openMissionContainer = new Table();
        openMissionScroller = new ScrollPane(openMissionContainer);
        openMissions.add(openMissionScroller).expand().fill().maxHeight(SCROLLER_HEIGHT).prefWidth(SCROLLER_WIDTH).maxWidth(SCROLLER_WIDTH).pad(SCROLLER_ELEMENT_PADDING);

        activeMissionsContainer = new Table();
        activeMissionScroller = new ScrollPane(activeMissionsContainer);
        activeMissions.add(activeMissionScroller).expand().fill().prefHeight(SCROLLER_HEIGHT).prefWidth(SCROLLER_WIDTH).maxWidth(SCROLLER_WIDTH).pad(SCROLLER_ELEMENT_PADDING);

        completedMissionsContainer = new Table();
        completedMissionsScroller = new ScrollPane(completedMissionsContainer);
        completedMissions.add(completedMissionsScroller).expand().fill().prefHeight(SCROLLER_HEIGHT).prefWidth(SCROLLER_WIDTH).maxWidth(SCROLLER_WIDTH).pad(SCROLLER_ELEMENT_PADDING);

        refreshList();

        // Setup tabbed view
        ArrayList<Actor> views = new ArrayList<Actor>();
        views.add(activeMissions);
        views.add(openMissions);
        views.add(completedMissions);
        TabbedView tabbedView = new TabbedView(views);

        // Set tabbed view as main view
        this.addMainContent(tabbedView);
    }

    private static void initSubTable(Table table) {
        table.align(Align.top);
        table.setTouchable(Touchable.enabled);
        table.setBackground(Assets.instance().tab_view_border_patch);
    }

    private void refreshList() {
        openMissionContainer.clearChildren();
        activeMissionsContainer.clearChildren();

        for (final Mission mission : MissionManager.instance().getOpenMissions()) {
            openMissionContainer.add(new MissionUIElement(mission, false, "Accept", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    MissionManager.instance().startMission(mission);
                }
            })).expandX().fillX().padTop(SCROLLER_ELEMENT_PADDING).padBottom(SCROLLER_ELEMENT_PADDING).row();
        }

        for (final Mission mission : MissionManager.instance().getActiveMissions()) {
            activeMissionsContainer.add(new MissionUIElement(mission, true, "Abort", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    MissionManager.instance().abortMission(mission);
                }
            })).expandX().fillX().padTop(SCROLLER_ELEMENT_PADDING).padBottom(SCROLLER_ELEMENT_PADDING).row();
        }

        for (final Mission mission : MissionManager.instance().getCompletedMissions()) {
            completedMissionsContainer.add(new MissionUIElement(mission, true, "", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                }
            })).expandX().fillX().padTop(SCROLLER_ELEMENT_PADDING).padBottom(SCROLLER_ELEMENT_PADDING).row();
        }
    }
}
