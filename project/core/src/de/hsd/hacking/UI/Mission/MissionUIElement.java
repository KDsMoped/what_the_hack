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

/**
 * UI element to display a mission.
 */
    public class MissionUIElement extends Table {
    private Mission mission;

    private Label name;
    private Label time;
    private Label description, skills;
    private Label money;

    private TextButton acceptButton;

    /**
     * Constructor.
     * @param mission Mission that shall be displayed.
     * @param compactView compactView hides the mission description.
     */
    public MissionUIElement(Mission mission, Boolean compactView) {
        this.mission = mission;

        initTable(compactView);
    }

    private void initTable(Boolean compactView) {
        // setup own table
        this.setTouchable(Touchable.enabled);
        this.align(Align.top);
        this.setBackground(Assets.instance().table_border_patch);
        this.pad(4f);

        // create and setup all ui elements
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

        // Add mission name to main table
        this.add(name).expandX().fillX().left();

        if (!compactView) {
            description = new Label(mission.getDescription(), Constants.LabelStyle());
            description.setWrap(true);
            this.row().padTop(10f);
            this.add(description).left().expand().fill();
        }

        // setup helper tables
        // this table contains all information except name and description because of formatting
        Table infoTable = new Table();
        infoTable.align(Align.left);

        // this table contains everything that is not description, name or skills
        Table moneyTimeTable = new Table();
        moneyTimeTable.align(Align.topRight);

        // add the helper table to the main table
        this.row().padTop(10f);
        this.add(infoTable).expand().fill();

        // add all the infos to the info table
        infoTable.add(skills).left().expand().fill();
        infoTable.add(moneyTimeTable).expandY().fillY().padTop(1f);

        // setup moneyTimeTable
        moneyTimeTable.add(calendar).right().padTop(-2);
        moneyTimeTable.add(time).right().padLeft(3).padTop(1f);
        moneyTimeTable.row().padTop(2f);
        moneyTimeTable.add(money).right();
        moneyTimeTable.add(dollar).right();
        moneyTimeTable.row().padTop(2f);
        moneyTimeTable.add(acceptButton);
    }

    /**
     * Get the mission object this object represents.
     * @return represented mission object.
     */
    public Mission getMission(){
        return mission;
    }
}
