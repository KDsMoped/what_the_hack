package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.UI.General.DoubleLabelElement;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.Provider.EmployeeProvider;
import de.hsd.hacking.Utils.Provider.StringProvider;

/**
 * A popup that displays the values of an employee.
 */
public class EmployeeProfile extends de.hsd.hacking.UI.General.Popup {

    private static final int TABLE_SPACING = 20;

    private VerticalGroup contentContainer;

    private Label title;
    private Table informationContainer = new Table();

    private int leftUILine;
    private int topUILine;

    //references
    private Team team;
    private EmployeeProvider employee;

    public EmployeeProfile(EmployeeProvider employee) {
        super();

        team = Team.instance();
        this.employee = employee;

        contentContainer = this.getContent();

        InitSheet();
        InitControls();
    }

    private void InitSheet() {

        Table content = new Table();
        content.align(Align.top);
        content.setTouchable(Touchable.enabled);
//        content.setDebug(true);

        title = new Label("Name of Employee", Constants.LabelStyle());

        Table viewport = new Table();
        viewport.setBackground(Assets.instance().table_border_patch);

        ScrollPane informationScroller = new ScrollPane(informationContainer, new ScrollPane.ScrollPaneStyle());

        contentContainer.addActor(content);
        content.add(title).expandX().fillX().padTop(5).padBottom(15).padLeft(110);
        content.row();
        content.add(viewport).expand().fill().maxHeight(165).width(300).padLeft(110);
        viewport.add(informationScroller).expand().fill();
    }

    private void InitControls(){

        leftUILine = (int) contentContainer.getX() + 50;
        topUILine = (int) contentContainer.getY() + 165;

        TextButton dismissButton = new TextButton("Dismiss", Constants.TextButtonStyle());
        dismissButton.addListener(new ChangeListener() {
                                      @Override
                                      public void changed(ChangeEvent event, Actor actor) {  OnDismissButton();     }
                                  });
        dismissButton.setBounds(leftUILine, topUILine - 40, 80, 20);

        addActor(dismissButton);
    }

    private void fillInformationContainer(){
        informationContainer.clearChildren();

        //Salary
        addInformationElement(new DoubleLabelElement("Salary", new StringProvider() {
            @Override
            public String get() {
                return employee.get().getSalaryText();
            }
        }));

        //Current Job
        addInformationElement(new DoubleLabelElement("Current Job", new StringProvider() {
            @Override
            public String get() {
                return employee.get().getState().getDisplayName();
            }
        }));

        //Skills
        addInformationElement(new Label("Skills", Constants.LabelStyle()));

        for(final Skill skill : employee.get().getSkillset() ){

            addInformationElement(new DoubleLabelElement(skill.getType().displayName(), new StringProvider() {
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

    private void OnDismissButton(){

        Employee empl = employee.get();

        if(empl == null){
            Gdx.app.error(Constants.TAG, "Error: Dismiss button clicked, but employe is null!");
            return;
        }

//        team.removeEmployee(empl);
        EmployeeManager.instance().dismiss(empl);
        Close();
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

        title.setText(employee.get().getName());

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isActive()) {
            return;
        }

        if (employee.get() == null) {
            return;
        }

        super.draw(batch, parentAlpha);

        employee.get().drawAt(batch, new Vector2(leftUILine + 25, topUILine));
    }
}
