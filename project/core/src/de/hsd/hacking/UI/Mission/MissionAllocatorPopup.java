package de.hsd.hacking.UI.Mission;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Callback.MissionCallback;
import de.hsd.hacking.Utils.Constants;

public class MissionAllocatorPopup extends Popup {

    MissionCallback onSelectMission;
    Callback onCancel;

    public MissionAllocatorPopup(MissionCallback onSelectMission, Callback onCancel) {
        super(60);

        this.onSelectMission = onSelectMission;
        this.onCancel = onCancel;

        closeButton.setText("Cancel");
//        closeButton.setWidth(120);
        mainTable.removeActor(closeButton);
        mainTable.add(closeButton).padBottom(4f).width(100).height(23).center();
        mainTable.setVisible(true);

        InitMissionTable();
    }

    private void InitMissionTable() {
        Table content = new Table();
        content.align(Align.top);
        content.setTouchable(Touchable.enabled);

        Label title = new Label("Choose a mission to work on", Constants.LabelStyle());
        title.setFontScale(1.0f);

        Table missionContainer = new Table();
        ScrollPane missionScroller = new ScrollPane(missionContainer);

        for (final Mission mission : MissionManager.instance().getActiveMissions()) {
//            MissionUIElement mission = new MissionUIElement(MissionFactory.CreateRandomMission());
//            mission.addListener(new ChangeListener() {
//                @Override
//                public void changed(ChangeEvent event, Actor actor) {
//                    selectMission(mission);
//                }
//            });
//
//            missionContainer.add(mission).expandX().fillX().padTop(5).padBottom(5).row();

            TextButton button = new TextButton(mission.getName(), Constants.TextButtonStyle());
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    selectMission(mission);
                }
            });
            missionContainer.add(button).expandX().fillX().padBottom(3).row();
        }


//        for (int i = 0; i < 6; i++) {
//
//            final Mission mission = MissionFactory.CreateRandomMission();
//
//            TextButton button = new TextButton(mission.getName(), Constants.TextButtonStyle());
//            button.addListener(new ChangeListener() {
//                @Override
//                public void changed(ChangeEvent event, Actor actor) {
//                    selectMission(mission);
//                }
//            });
//            missionContainer.add(button).expandX().fillX().padBottom(3).row();
//        }


//        }

        this.addMainContent(content);
        content.add(title).expandX().fillX().padTop(5).center();
        content.row();
        content.add(missionScroller).expand().fill().maxHeight(100);
    }

    public void selectMission(Mission mission) {
        Gdx.app.log(Constants.TAG, "selected mission: " + mission.getName() + ": " + mission.getDescription());
        onSelectMission.callback(mission);
        remove();
    }

    @Override
    public void close() {
        onCancel.callback();
        super.close();
        remove();
    }

}
