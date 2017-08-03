package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Assets.AudioManager;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.EmployeeSpecial;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Utils.Constants;

import java.util.Collection;
import java.util.Iterator;

/**
 * This UI element shows all details about an {@link Employee}. Differs based on the kind of employee. Hired employees
 * can be dismissed. Available employees can be hired.
 *
 * @author Hendrik
 */
public class EmployeeUIElement extends Table {

    private final Employee employee;

    private Label money;

    public EmployeeUIElement(Employee employee) {
        this.employee = employee;

        initTable();
    }

    private void initTable() {
        setTouchable(Touchable.enabled);
        align(Align.top);
        setBackground(Assets.instance().table_border_patch);
        pad(4f);
//        setDebug(true);

        add(new EmployeeIcon(employee)).left().top().padRight(10).width(32).minWidth(32).maxWidth(32);
        add(getSecondColumn()).expand().fillX().top().prefWidth(280);
        add(getThirdColumn()).expand().fill().right().top().padLeft(10).minWidth(70).prefHeight(100);
    }

    private Table getSecondColumn(){
        Table secondColumn = new Table();
//        secondColumn.setDebug(true);

        Label name = new Label(employee.getName(), Constants.LabelStyle());
        name.setFontScale(1.05f);

        Table skillsTable = initSkillsTable(employee.getSkillset());

        secondColumn.add(name).expandX().fillX().left().top();
        secondColumn.row();
        secondColumn.add(skillsTable).expand().fill().left().top().padTop(5);

        return secondColumn;
    }

    private Table getThirdColumn(){
        if(employee.isEmployed()) return getThirdColumnTeam();
        else return getThirdColumnEmploy();
    }

    private Table getThirdColumnEmploy(){
        Table thirdColumn = new Table();
//        thirdColumn.setDebug(true);

        money = new Label(employee.getSalaryText(), Constants.LabelStyle());
        money.setAlignment(Align.topRight);

        TextButton employButton = new TextButton("Employ", Constants.TextButtonStyle());
        employButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EmployeeManager.instance().employ(employee, true, false);
                AudioManager.instance().playUIButtonSound();
            }
        });

        thirdColumn.add(money).right().top();
        thirdColumn.row();
        thirdColumn.add().prefHeight(0).minHeight(0).expandY().fillY();
        thirdColumn.row();
        thirdColumn.add(employButton).right().bottom().padTop(5).prefWidth(70);

        return thirdColumn;
    }

    private Table getThirdColumnTeam(){
        Table thirdColumn = new Table();
//        thirdColumn.setDebug(true);

        money = new Label(employee.getSalaryText(), Constants.LabelStyle());
        money.setAlignment(Align.topRight);

        TextButton employButton = new TextButton("Dismiss", Constants.TextButtonStyle());
        employButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            EmployeeManager.instance().dismiss(employee);
            }
        });

        thirdColumn.add(money).right().top();
        thirdColumn.row();
        thirdColumn.add().prefHeight(0).minHeight(0).expandY().fillY();
        thirdColumn.row();
        thirdColumn.add(employButton).right().bottom().padTop(5).prefWidth(70);

        return thirdColumn;
    }

    private Table initSkillsTable(Collection<Skill> skillset) {
        Table skillsTable = new Table();
//        skillsTable.setDebug(true);

        for (Iterator<Skill> iterator = skillset.iterator(); iterator.hasNext(); ) {
            skillsElement(skillsTable, iterator.next());

            if(iterator.hasNext()) skillsTable.row();
        }

        //Specials
        Collection<EmployeeSpecial> specials = employee.getSpecials(false);

        if (specials.size() > 0) {

            skillsTable.row();
            skillsTable.add().height(7);

            for (final EmployeeSpecial special : specials) {

                skillsTable.row();
                skillsTable.add(new Label(special.getDisplayName(), Constants.LabelStyle())).left().expandX().colspan(3);
            }
        }
        return skillsTable;
    }

    private void skillsElement(Table skillsTable, Skill s){
        Image icon = new Image(Assets.instance().getSkillIcon(s.getType().skillType));
        Label skillText = new Label(s.getDisplayType(), Constants.LabelStyle());
        skillText.setAlignment(Align.left);
        Label skillNum = new Label(s.getDisplayValue(true), Constants.LabelStyle());
        skillNum.setAlignment(Align.right);

//            tableElements(skillsTable, icon, skillText, skillNum);

//        Gdx.app.log("", "img width " + icon.getImageWidth());

        skillsTable.add(icon).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13);
        skillsTable.add(skillText).expandX().left().padLeft(5);
        skillsTable.add(skillNum).right().expandX().padRight(6);
    }

    @Override
    public void act(float delta) {

        if(employee.isEmployed()) {
            money.setText(
                    employee.getSalaryText() + "\n" +
                            "a week");
        }else{
            money.setText(
                    employee.getSalaryText() + "\n" +
                            "a week\n" +
                            employee.getHiringCostText() + "\n" +
                            "now");
        }

//        if (Team.instance().getMoney() < employee.getHiringCost()) {
//            money.setColor(Color.RED);
//            employButton.setDisabled(true);
//
//        } else {
//            money.setColor(Color.WHITE);
//            employButton.setDisabled(false);
//        }

        super.act(delta);
    }
}
