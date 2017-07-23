package de.hsd.hacking.UI.Mission;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Assets.AudioManager;
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
    private Label description, outcomeDescription;
    private Label money;
    private Table skills;

    private TextButton actionButton;

    private String buttonText;
    private Boolean showDescription;
    private Boolean showOutcome;

    private EventListener buttonListener;

    /**
     * Constructor.
     *
     * @param mission        Mission that shall be displayed.
     * @param showDescription    showDescription hides the mission description.
     * @param buttonText     Button text.
     * @param buttonListener Button callback.
     */
    public MissionUIElement(Mission mission, boolean showDescription, boolean showOutcome, String buttonText, EventListener buttonListener) {
        this.mission = mission;
        this.showDescription = showDescription;
        this.showOutcome = showOutcome;
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

        money = new Label("" + mission.getOutcome().rewardMoney, Constants.LabelStyle());
        Label dollar = new Label("$", Constants.LabelStyle());

//        skills = new Label("", Constants.LabelStyle());
        List<Skill> skill = mission.getSkill();
//        skills.setWrap(true);

//        for (int i = 0; i < skill.size(); i++) {
//            Skill s = skill.get(i);
//            if(i== 0) skills.setText(s.getType().name() + " " + s.getDisplayValue(false));
//            else skills.setText(skills.getText() + " \n" + s.getType().name() + " " + s.getDisplayValue(false));
//        }

        skills = new Table();
//        skills.setDebug(true);
        skills.align(Align.left);

        for (int i = 0; i < skill.size(); i++) {
            Skill s = skill.get(i);

            Image icon = new Image(Assets.instance().getSkillIcon(s.getType()));
            Label text = new Label(s.getDisplayValue(false), Constants.LabelStyle());

            skills.add(icon).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13);
            skills.add(text).left().padLeft(3).padRight(28);
        }

        Image calendar = new Image(Assets.instance().ui_calendar);

        // Add mission name to main table
        add(name).expandX().fillX().left();

        if (showDescription) {
            description = new Label(mission.getDescription(), Constants.LabelStyle());
            description.setWrap(true);
            row().padTop(10f);
            add(description).left().expand().fill();
        }

        if (showOutcome) {
            outcomeDescription = new Label(mission.getSuccessText(), Constants.LabelStyle());
            outcomeDescription.setWrap(true);
            row().padTop(10f);
            add(outcomeDescription).left().expand().fill();
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
        this.add(infoTable).expand().fill().left();

        // add all the infos to the info table
        infoTable.add(skills).left().expand().fill().pad(5);
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
            actionButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    AudioManager.instance().playUIButtonSound();
                }
            });
            moneyTimeTable.add(actionButton).width(70).right().colspan(2);
        }
    }

    /**
     * Get the mission object this object represents.
     *
     * @return represented mission object.
     */
    public Mission getMission() {
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

    public Boolean getShowDescription() {
        return showDescription;
    }

    public void setShowDescription(Boolean showDescription) {
        this.showDescription = showDescription;
    }

    public EventListener getButtonListener() {
        return buttonListener;
    }

    public void setButtonListener(EventListener buttonListener) {
        this.buttonListener = buttonListener;
    }
}
