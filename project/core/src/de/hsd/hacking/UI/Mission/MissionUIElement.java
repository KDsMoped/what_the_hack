package de.hsd.hacking.UI.Mission;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Entities.Employees.Skill;

import java.util.List;

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

    private TextButton actionButton;

    private String buttonText;
    private Boolean compactView;

    private EventListener buttonListener;

    /**
     * Constructor.
     * @param mission Mission that shall be displayed.
     * @param compactView compactView hides the mission description.
     * @param buttonText Button text.
     * @param buttonListener Button callback.
     */
    public MissionUIElement(Mission mission, Boolean compactView, String buttonText, EventListener buttonListener) {
        this.mission = mission;
        this.compactView = compactView;
        this.buttonText = buttonText;
        this.buttonListener = buttonListener;

        initTable();
    }

    private void initTable() {
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
        List<Skill> skill = mission.getSkill();

        for (int i = 0; i < skill.size(); i++) {
            Skill s = skill.get(i);
            if(i== 0) skills.setText(s.getType().name() + " " + s.getDisplayValue(false));
            else skills.setText(skills.getText() + " \n" + s.getType().name() + " " + s.getDisplayValue(false));
        }
        skills.setWrap(true);

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


        if (buttonText != null && !buttonText.equals("")) {
            actionButton = new TextButton(buttonText, Constants.TextButtonStyle());
            actionButton.addListener(buttonListener);
            moneyTimeTable.add(actionButton);
        }
    }

    /**
     * Get the mission object this object represents.
     * @return represented mission object.
     */
    public Mission getMission(){
        return mission;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public TextButton getActionButton() {
        return actionButton;
    }

    public void setActionButton(TextButton actionButton) {
        this.actionButton = actionButton;
    }

    public Boolean getCompactView() {
        return compactView;
    }

    public void setCompactView(Boolean compactView) {
        this.compactView = compactView;
    }

    public EventListener getButtonListener() {
        return buttonListener;
    }

    public void setButtonListener(EventListener buttonListener) {
        this.buttonListener = buttonListener;
    }
}
