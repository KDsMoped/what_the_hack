package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Employees.SkillType;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.UI.*;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 14.06.17.
 */

public class EmployeeProfile extends Popup {

    private static final int TABLE_SPACING = 20;

    private VerticalGroup contentContainer;
    private Table content;

    private Label title;
    private Table informationContainer = new Table();
    private ScrollPane informationScroller;

    private Label nameLabel;
    private Label jobLabel;

    private int leftUILine;
    private int topUILine;

    //references
    private Team team;

    public EmployeeProfile() {
        super();

        team = Team.getInstance();

        contentContainer = this.getContent();


        InitSheet();
        InitControls();
    }

    private void InitControls(){

        leftUILine = (int) contentContainer.getX() + 50;
        topUILine = (int) contentContainer.getY() + 165;

        TextButton dismissButton = new TextButton("Dismiss", Constants.TextButtonStyle());
        dismissButton.addListener(new ChangeListener() {
                                      @Override
                                      public void changed(ChangeEvent event, Actor actor) {
                                          GetSelected().removeFromDrawingTile();
                                          team.removeEmployee(GetSelected());
                                          team.deselectEmployee();
                                          Close();
                                      }
                                  }
        );
        dismissButton.setBounds(leftUILine, topUILine - 40, 80, 20);

        addActor(dismissButton);
    }

    private void InitSheet() {

//        nameLabel = new Label("Name", Constants.LabelStyle());
//        nameLabel.setBounds(originX, originY - 20, width, 20);
//        addActor(nameLabel);
//
//        jobLabel = new Label("Current Job", Constants.LabelStyle());
//        jobLabel.setBounds(originX, originY - 40, width, 20);
//        addActor(jobLabel);


        content = new Table();
        content.align(Align.top);
        content.setTouchable(Touchable.enabled);
//        content.setDebug(true);

        title = new Label("Employee Sheet", Constants.LabelStyle());


        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        //scrollPaneStyle.vScrollKnob = new

        informationScroller = new ScrollPane(informationContainer, scrollPaneStyle);

        //fillInformationContainer();


        contentContainer.addActor(content);
        content.add(title).expandX().fillX().padTop(5).padBottom(15).padLeft(110);
        content.row();
        content.add(informationScroller).expand().fill().maxHeight(165).width(300).padLeft(110);
    }

    private void fillInformationContainer(){
        informationContainer.clearChildren();

        addInformationElement(new DoubleLabelElement("Name", new DoubleLabelElement.StringProvider() {
            @Override
            public String get() {
                return GetSelected().getName();
            }
        }));

        addInformationElement(new DoubleLabelElement("Current Job", new DoubleLabelElement.StringProvider() {
            @Override
            public String get() {
                return GetSelected().getState().getDisplayName();
            }
        }));

        addInformationElement(new DoubleLabelElement("Salary", new DoubleLabelElement.StringProvider() {
            @Override
            public String get() {
                return GetSelected().getSalary();
            }
        }));

        addInformationElement(new DoubleLabelElement("Skills", ""));

        for(final Skill skill : GetSelected().getSkillset() ){

            addInformationElement(new DoubleLabelElement(skill.getType().name(), new DoubleLabelElement.StringProvider() {
                @Override
                public String get() {
                    return skill.getDisplayValue(true);
                }
            }));
        }
    }

    private void addInformationElement(Actor element){

        informationContainer.add(element).height(TABLE_SPACING).expandX().fillX().row();
    }

    @Override public void Show(){
        super.Show();

        fillInformationContainer();
    }

    @Override
    public void act(float delta) {
        if (!isActive()) {
            return;
        }

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isActive()) {
            return;
        }

        if (GetSelected() == null) {

            return;
        }

        super.draw(batch, parentAlpha);

        GetSelected().drawAt(batch, new Vector2(leftUILine + 25, topUILine));
    }

    private Employee GetSelected() {
        return team.getSelectedEmployee();
    }
}
