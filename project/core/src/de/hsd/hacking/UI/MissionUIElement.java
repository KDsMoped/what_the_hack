package de.hsd.hacking.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Mission;
import de.hsd.hacking.Entities.Employees.Skill;

/**
 * Created by ju on 22.06.17.
 */

public class MissionUIElement extends Table {
    private Assets assets;
    private Mission mission;

    private Label.LabelStyle labelStyle;

    private Label name;
    private Label time;
    private Label description, skills;
    private Label money;

    public MissionUIElement(Assets assets, Mission mission) {
        this.assets = assets;
        this.mission = mission;

        InitTable();
    }

    private void InitTable() {
        this.setTouchable(Touchable.enabled);
        this.align(Align.top);

        labelStyle = new Label.LabelStyle();
        labelStyle.font = assets.status_bar_font;
        labelStyle.fontColor = Color.BLACK;

        name = new Label(mission.getName(), labelStyle);
        time = new Label(Integer.toString(mission.getDuration()), labelStyle);
        description = new Label(mission.getDescription(), labelStyle);
        description.setWrap(true);
        money = new Label("$$", labelStyle);
        skills = new Label("", labelStyle);

        for (Skill s:mission.getSkill()) {
            skills.setText(skills.getText() + s.getType().toString() + ": " + s.getValue() + " ");
        }

        this.add(name).expandX().fillX().left();
        this.add(time).right().padLeft(10);
        this.row();
        this.add(description).left().expand().fill();
        this.add(money).right().padLeft(5);
        this.row();
        this.add(skills).expandX().fillX();
    }
}
