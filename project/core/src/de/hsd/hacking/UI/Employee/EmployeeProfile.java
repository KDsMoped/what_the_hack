package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.Collection;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Assets.AudioManager;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.EmployeeSpecial;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.UI.General.DoubleLabelElement;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.Provider.EmployeeProvider;
import de.hsd.hacking.Utils.Provider.StringProvider;

/**
 * A popup that displays the values of an {@link Employee}.
 *
 * @author Hendrik
 */
public class EmployeeProfile extends Popup {

    private static final int TABLE_SPACING = 20;

    private Label title;
    private Table informationContainer = new Table();


    private EmployeeProvider employee;

    public EmployeeProfile(EmployeeProvider employee) {
        super(40);

        this.employee = employee;
        mainTable.setTouchable(Touchable.enabled);
        mainTable.setDebug(true);

        initTable();
}

    private void initTable() {

        Table contentTable = new Table();
        contentTable.align(Align.top);
        contentTable.setTouchable(Touchable.enabled);
//        contentTable.setDebug(true);

        contentTable.add(initLeftColumn()).left().top().padLeft(20).maxWidth(120).maxHeight(162);
        contentTable.add(initRightColumn()).right().top().padRight(20).maxWidth(310).prefWidth(310).maxHeight(162);

        addMainContent(contentTable);
    }

    private Table initLeftColumn(){
        Table leftColumn = new Table();
//        leftColumn.setDebug(true);


        EmployeeIcon icon = new EmployeeIcon(employee);
//        icon.setPosition(leftUILine, topUILine);
//        icon.pad(10);
//        icon.padBottom(10);

        leftColumn.add(new Label("", Constants.LabelStyle())).padBottom(15);
        leftColumn.row();
        leftColumn.add(icon).padBottom(10).center().row();

        TextButton dismissButton = new TextButton("Dismiss", Constants.TextButtonStyle());
        dismissButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onDismissButton();
                AudioManager.instance().playUIButtonSound();
            }
        });
//        dismissButton.setBounds(leftUILine, topUILine - 40, 80, 20);
        dismissButton.setSize(120, 20);
//        dismissButton.padTop(15);

        leftColumn.add(dismissButton).center();

        return leftColumn;
    }

    private Table initRightColumn() {
        Table rightColumn = new Table();
        rightColumn.align(Align.top);
        rightColumn.setTouchable(Touchable.enabled);
//        content.setDebug(true);

        title = new Label("Name of Employee", Constants.LabelStyle());
        title.setFontScale(1.0f);

        Table viewport = new Table();
        viewport.setBackground(Assets.instance().table_border_patch);

        ScrollPane informationScroller = new ScrollPane(informationContainer);
        informationScroller.setStyle(Constants.ScrollPaneStyleWin32());
        informationScroller.setFadeScrollBars(false);
        informationContainer.pad(2);

        rightColumn.add(title).expandX().fillX().padTop(5).padBottom(10).padLeft(10);
        rightColumn.row();
        rightColumn.add(viewport).prefWidth(300)/*.maxHeight(130).width(300)*/.padLeft(10).expand().fill();
        viewport.add(informationScroller).expand().fill().right();
//        mainTable.add(content);
        return rightColumn;
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
        Collection<EmployeeSpecial> specials = employee.get().getSpecials(false);

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
