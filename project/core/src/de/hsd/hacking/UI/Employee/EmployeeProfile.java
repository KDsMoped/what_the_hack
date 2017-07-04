package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.EmployeeSpecial;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.UI.General.DoubleLabelElement;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.Provider.EmployeeProvider;
import de.hsd.hacking.Utils.Provider.StringProvider;

import java.util.Collection;

/**
 * A popup that displays the values of an employee.
 */
public class EmployeeProfile extends Popup {

    private static final int TABLE_SPACING = 20;

    private VerticalGroup contentContainer;

    private Label title;
    private Table informationContainer = new Table();

    private EmployeeProvider employee;

    public EmployeeProfile(EmployeeProvider employee) {
        super(40);

        Team team = Team.instance();
        this.employee = employee;

        contentContainer = this.getContent();

        initControls();
        initSheet();
    }

    private void initSheet() {

        Table content = new Table();
        content.align(Align.top);
        content.setTouchable(Touchable.enabled);
//        content.setDebug(true);

        title = new Label("Name of Employee", Constants.LabelStyle());
        title.setFontScale(1.0f);

        Table viewport = new Table();
        viewport.setBackground(Assets.instance().table_border_patch);

        ScrollPane informationScroller = new ScrollPane(informationContainer, new ScrollPane.ScrollPaneStyle());
        informationContainer.pad(2);

        contentContainer.addActor(content);
        content.add(title).expandX().fillX().padTop(5).padBottom(10).padLeft(100);
        content.row();
        content.add(viewport).maxHeight(130).width(300).padLeft(100).expand().fill();
        viewport.add(informationScroller).expand().fill();
    }

    private void initControls() {

//        leftUILine = (int) contentContainer.getX() + 40;
//        topUILine = (int) contentContainer.getY() + 165;

        VerticalGroup controls = new VerticalGroup();
        addActor(controls);

//        controls.align(Align.topLeft);
//        controls.setPosition(0, 0);
        controls.setPosition(contentContainer.getX() + 40, contentContainer.getY() + 106);
        controls.setWidth(120);
        controls.setHeight(100);
        controls.space(10);
//        controls.setDebug(true);


        EmployeeIcon icon = new EmployeeIcon(employee);
//        icon.setPosition(leftUILine, topUILine);
//        icon.pad(10);
//        icon.padBottom(10);
        controls.addActor(icon);

        TextButton dismissButton = new TextButton("Dismiss", Constants.TextButtonStyle());
        dismissButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onDismissButton();
            }
        });
//        dismissButton.setBounds(leftUILine, topUILine - 40, 80, 20);
        dismissButton.setSize(120, 20);
//        dismissButton.padTop(15);

        controls.addActor(dismissButton);
    }

    private void fillInformationContainer() {
        informationContainer.clearChildren();

        //Salary
        addInformationElement(new DoubleLabelElement("Salary", new StringProvider() {
            @Override
            public String get() {
                return employee.get().getSalaryText() + " per week";
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
//        addInformationElement(new Label("Skills:", Constants.LabelStyle()), 2);

        for (final Skill skill : employee.get().getSkillset()) {

            addInformationElement(new DoubleLabelElement(skill.getType().getDisplayName(), new StringProvider() {
                @Override
                public String get() {
                    return skill.getDisplayValue(true);
                }
            }));
        }

        //Specials

        Collection<EmployeeSpecial> specials = employee.get().getSpecials();

        if (specials.size() > 0) {

//            addInformationElement(new Label("Special:", Constants.LabelStyle()), 2);
            addInformationElement(new Label("", Constants.LabelStyle()), 0);

            for (final EmployeeSpecial special : specials) {

                if (!special.isHidden())
                    addInformationElement(new Label(special.getDisplayName(), Constants.LabelStyle()));
            }
        }
//        informationContainer.hit(0, 0, false);
    }

    private void addInformationElement(Actor element) {

        addInformationElement(element, 0);
    }

    private void addInformationElement(Actor element, float padTop) {

        informationContainer.add(element).height(TABLE_SPACING).padTop(padTop).expandX().fillX().row();
    }

    private void onDismissButton() {

        Employee empl = employee.get();

        if (empl == null) {
            Gdx.app.error(Constants.TAG, "Error: Dismiss button clicked, but employee is null!");
            return;
        }

        EmployeeManager.instance().dismiss(empl);
        close();
    }

    @Override
    public void show() {
        super.show();

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
    }
}
