package de.hsd.hacking.UI.Mission;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Mission;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Entities.Employees.Skill;

/**
 * Created by ju on 22.06.17.
 */
// TODO Employee icons
public class MissionUIElement extends Table {
    private Mission mission;

    private Label name;
    private Label time;
    private Label description, skills;
    private Label money;

    public MissionUIElement(Mission mission) {
        this.mission = mission;

        InitTable();
    }

    private void InitTable() {
        this.setTouchable(Touchable.enabled);
        this.align(Align.top);
        this.setBackground(Assets.instance().table_border_patch);
        this.pad(4f);

        name = new Label(mission.getName(), Constants.LabelStyle());
        name.setFontScale(1.05f);
        time = new Label(Integer.toString(mission.getDuration()), Constants.LabelStyle());
        description = new Label(mission.getDescription(), Constants.LabelStyle());
        description.setWrap(true);
        money = new Label("$$", Constants.LabelStyle());
        skills = new Label("", Constants.LabelStyle());

        for (Skill s:mission.getSkill()) {
            skills.setText(skills.getText() + s.getType().toString() + ": " + s.getValue() + " ");
        }

        Image calendar = new Image(Assets.instance().ui_calendar);

        this.add(name).expandX().fillX().left();
        this.add(calendar).right().padLeft(10).padTop(-2);
        this.add(time).right().padLeft(3);
        this.row().padTop(10f);
        this.add(description).left().expand().fill();
        this.add(money).right().padLeft(5);
        this.row().padTop(10f);
        this.add(skills).expandX().fillX();
    }

    public Mission getMission(){
        return mission;
    }
}
