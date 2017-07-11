package de.hsd.hacking.UI.Mission;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.MissionWorker;
import de.hsd.hacking.Entities.Employees.MissionSkillRequirement;
import de.hsd.hacking.Entities.Employees.SkillType;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 12.07.2017.
 */

public class MissionStatusOverlay extends Group {

    private Label missionNameLabel;
    private MissionWorker missionWorker;
    private Table skillTable;

    public MissionStatusOverlay(){
        super();

        int width = 96;
        int height = 64;
        int posX = 0;
        int posY = 0;

        Table table = new Table();
        table.setDebug(true);
//        table.setBackground(Assets.instance().win32_patch);
        table.setBounds(posX, posY, width, height);
        addActor(table);

        missionNameLabel = new Label("MissionXYZ", Constants.LabelStyle());
        missionNameLabel.setAlignment(Align.center);

        table.add(missionNameLabel).expand().pad(2).center().row();

        skillTable = new Table();
        table.add(skillTable).expand().fillX();

    }

    public void setMissionWorker(final MissionWorker missionWorker1) {
        this.missionWorker = missionWorker1;
        skillTable.clearChildren();
        if (missionWorker != null) {
            for (MissionSkillRequirement req
                    : missionWorker1.getSkillRequirements()) {
                Image icon = new Image(Assets.instance().getSkillIcon(req.getSkillType()));
                icon.setScaling(Scaling.none);
                skillTable.add(icon).height(8).expandX().center().space(2f, 0 , 2f, 0).pad(2f, 0, 2f, 0); //TODO richtiges Icon holen
                Image bar = new Image(Assets.instance().loading_bar.first());
                bar.setScaling(Scaling.none);
                skillTable.add(bar).height(8).expandX().center().space(2f, 0 , 2f, 0).pad(2f, 0, 2f, 0); //TODO status übergeben
                skillTable.row();
            }

        }
    }

    private void refreshTable() {
        //TODO
    }

    @Override
    public void act(float delta) {
        if (!Team.instance().isEmployeeSelected()) return;
        refreshTable();
        super.act(delta);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!Team.instance().isEmployeeSelected()) return;

        super.draw(batch, parentAlpha);
    }

    public void setPosition(final Vector2 position) {
        setPosition(position.x, position.y);
    }
}
