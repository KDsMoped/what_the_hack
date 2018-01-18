package de.hsd.hacking.UI.Mission;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
 * Popup mission browser for open and active missions.
 *
 * @author Julian
 */
public class MissionBrowser extends Popup {

    private Label title;

    private Table openMissionContainer, activeMissionsContainer, completedMissionsContainer;


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
        Table openMissions = new Table();
        initSubTable(openMissions);
        openMissions.setName("Open");


        // Setup running missions table
        Table activeMissions = new Table();
        initSubTable(activeMissions);
        activeMissions.setName("Active");

        // Setup completed missions table
        Table completedMissions = new Table();
        initSubTable(completedMissions);
        completedMissions.setName("Completed");


        // Add everything to the helper tables
        openMissionContainer = new Table();
        ScrollPane openMissionScroller = new ScrollPane(openMissionContainer);
        openMissionScroller.setStyle(Constants.ScrollPaneStyleWin32());
        openMissionScroller.setFadeScrollBars(false);
        openMissions.add(openMissionScroller).expand().fill().maxHeight(SCROLLER_HEIGHT).prefWidth(SCROLLER_WIDTH).maxWidth(SCROLLER_WIDTH).pad(SCROLLER_ELEMENT_PADDING);

        activeMissionsContainer = new Table();
        ScrollPane activeMissionScroller = new ScrollPane(activeMissionsContainer);
        activeMissionScroller.setStyle(Constants.ScrollPaneStyleWin32());
        activeMissionScroller.setFadeScrollBars(false);
        activeMissions.add(activeMissionScroller).expand().fill().prefHeight(SCROLLER_HEIGHT).prefWidth(SCROLLER_WIDTH).maxWidth(SCROLLER_WIDTH).pad(SCROLLER_ELEMENT_PADDING);

        completedMissionsContainer = new Table();
        ScrollPane completedMissionsScroller = new ScrollPane(completedMissionsContainer);
        completedMissionsScroller.setStyle(Constants.ScrollPaneStyleWin32());
        completedMissionsScroller.setFadeScrollBars(false);
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
        completedMissionsContainer.clearChildren();

        for (final Mission mission : MissionManager.instance().getOpenMissions()) {
            openMissionContainer.add(new MissionUIElement(mission, true, false, false, "Accept", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    MissionManager.instance().startMission(mission);
                }
            })).expandX().fillX().padTop(SCROLLER_ELEMENT_PADDING).padBottom(SCROLLER_ELEMENT_PADDING).padRight(4).row();
        }

        for (final Mission mission : MissionManager.instance().getActiveMissions()) {
            activeMissionsContainer.add(new MissionUIElement(mission, true, false, true, "Abort", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    MissionManager.instance().abortMission(mission);
                }
            })).expandX().fillX().padTop(SCROLLER_ELEMENT_PADDING).padBottom(SCROLLER_ELEMENT_PADDING).padRight(4).row();
        }

        for (final Mission mission : MissionManager.instance().getCompletedMissions()) {
            completedMissionsContainer.add(new MissionUIElement(mission, true, true, false,"", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                }
            })).expandX().fillX().padTop(SCROLLER_ELEMENT_PADDING).padBottom(SCROLLER_ELEMENT_PADDING).padRight(4).row();
        }
    }
}
