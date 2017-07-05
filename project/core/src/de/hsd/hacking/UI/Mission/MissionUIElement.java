package de.hsd.hacking.UI.Mission;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.sun.org.apache.xpath.internal.operations.Bool;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Missions.Mission;
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

    private TextButton acceptButton;

    public MissionUIElement(Mission mission, Boolean compactView) {
        this.mission = mission;

        initTable(compactView);
    }

    private void initTable(Boolean compactView) {
        this.setTouchable(Touchable.enabled);
        this.align(Align.top);
        this.setBackground(Assets.instance().table_border_patch);
        this.pad(4f);

        name = new Label(mission.getName(), Constants.LabelStyle());
        name.setFontScale(1.05f);
        time = new Label(Integer.toString(mission.getDuration()), Constants.LabelStyle());

        money = new Label("1.322", Constants.LabelStyle());
        Label dollar = new Label("$", Constants.LabelStyle());
        skills = new Label("", Constants.LabelStyle());

        for (Skill s:mission.getSkill()) {
            skills.setText(skills.getText() + s.getType().name() + ": " + s.getDisplayValue(false) + " \n");
        }
        skills.setWrap(true);

        acceptButton = new TextButton("Accept", Constants.TextButtonStyle());
        Image calendar = new Image(Assets.instance().ui_calendar);

        this.add(name).expandX().fillX().left();

        if (!compactView) {
            description = new Label(mission.getDescription(), Constants.LabelStyle());
            description.setWrap(true);
            this.row().padTop(10f);
            this.add(description).left().expand().fill();
        }

        // setup helper table
        Table infoTable = new Table();
        Table moneyTimeTable = new Table();
        infoTable.align(Align.left);
        moneyTimeTable.align(Align.topRight);
        this.row().padTop(10f);
        this.add(infoTable).expand().fill();

        infoTable.add(skills).left().expand().fill();
        infoTable.add(moneyTimeTable).expandY().fillY().padTop(1f);

        moneyTimeTable.add(calendar).right().padTop(-2);
        moneyTimeTable.add(time).right().padLeft(3).padTop(1f);
        moneyTimeTable.row().padTop(2f);
        moneyTimeTable.add(money).right();
        moneyTimeTable.add(dollar).right();
        moneyTimeTable.row().padTop(2f);
        moneyTimeTable.add(acceptButton);
    }

    public Mission getMission(){
        return mission;
    }
}
