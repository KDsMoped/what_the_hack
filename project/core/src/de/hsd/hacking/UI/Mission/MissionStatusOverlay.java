package de.hsd.hacking.UI.Mission;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.MissionWorker;
import de.hsd.hacking.Data.TimeChangedListener;
import de.hsd.hacking.Entities.Employees.MissionSkillRequirement;
import de.hsd.hacking.Entities.Team.TeamManager;
import de.hsd.hacking.UI.General.LoadingBar;
import de.hsd.hacking.Utils.Constants;

/**
 * Overlay that shows current mission status.
 * @author Florian
 */
public class MissionStatusOverlay extends Group implements TimeChangedListener {

    private final Label dayLabel;
    private Label missionNameLabel;
    private MissionWorker missionWorker;
    private Table skillTable;
    List<LoadingBar> bars;

    public MissionStatusOverlay(){
        super();

        int width = 144;
        int height = 112;
        int posX = 0;
        int posY = 0;

        Table table = new Table();
//        table.setDebug(true);
        table.setBackground(Assets.instance().win32_patch);
        table.setBounds(posX, posY, width, height);
        table.pad(8f);
        addActor(table);

        missionNameLabel = new Label("MissionXYZ", Constants.TinyLabelStyle());
        missionNameLabel.setAlignment(Align.center);

        table.add(missionNameLabel).expand().pad(2).center().colspan(2);
        table.row();
        table.add(new Label("Remaining", Constants.TinyLabelStyle())).expand().pad(2).center();
        dayLabel = new Label("x/y", Constants.TinyLabelStyle());
        table.add(dayLabel).width(30).pad(2).center();
        table.row();

        skillTable = new Table();
        table.add(skillTable).colspan(2).expand().fillX();

    }

    public void setMissionWorker(final MissionWorker missionWorker1) {
        this.missionWorker = missionWorker1;
        skillTable.clearChildren();
        bars = new ArrayList<LoadingBar>();
        if (missionWorker != null) {
            missionNameLabel.setText(missionWorker.getMission().getShortenedName(12));
            for (MissionSkillRequirement req
                    : missionWorker1.getSkillRequirements()) {
                Image icon = new Image(Assets.instance().getSkillIcon(req.getSkillType().skillType));
                icon.setScaling(Scaling.none);
                skillTable.add(icon).height(13).expandX().center().space(2f, 0, 2f, 0).pad(2f, 0, 2f, 0); //TODO richtiges Icon holen
                //skillTable.add(bar).height(8).expandX().center().space(2f, 0 , 2f, 0).pad(2f, 0, 2f, 0); //TODO status übergeben
                LoadingBar bar = new LoadingBar();
                bars.add(bar);
                skillTable.add(bar).expandX().center().space(2f, 0, 2f, 0).pad(2f, 0, 2f, 0);
                skillTable.row();
            }
            refreshTable();
        }

    }


    private void refreshTable() {
        if (missionWorker != null) {
            dayLabel.setText(missionWorker.getRemainingMissionDays() + "/" + missionWorker.getMissionDays());
            for (int i = 0; i < missionWorker.getSkillRequirements().size(); i++) {
                MissionSkillRequirement req = missionWorker.getSkillRequirements().get(i);
                bars.get(i).set(req.getCurrentValue(), req.getValueRequired());
            }
        }
    }

    @Override
    public void act(float delta) {
        if (!TeamManager.instance().isEmployeeSelected() || TeamManager.instance().getSelectedEmployee().getCurrentMission() == null) return;
        super.act(delta);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!TeamManager.instance().isEmployeeSelected() || TeamManager.instance().getSelectedEmployee().getCurrentMission() == null) return;

        super.draw(batch, parentAlpha);
    }

    public void setPosition(final Vector2 position) {
        setPosition(position.x, position.y);
    }

    @Override
    public void timeChanged(float time) {

    }

    @Override
    public void timeStepChanged(int step) {
        if (!TeamManager.instance().isEmployeeSelected() || TeamManager.instance().getSelectedEmployee().getCurrentMission() == null) return;
        refreshTable();
    }

    @Override
    public void dayChanged(int days) {

    }

    @Override
    public void weekChanged(int week) {

    }
}
