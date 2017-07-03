package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Utils.Callback.EmployeeCallback;
import de.hsd.hacking.Utils.Constants;

import java.util.Collection;

public class EmployeeUIElement extends Table {

    private final Employee employee;
    private final EmployeeCallback onEmploy;

    public EmployeeUIElement(Employee employee, EmployeeCallback onEmploy) {
        this.employee = employee;
        this.onEmploy = onEmploy;

        initTable();
    }

    private void initTable() {
        setTouchable(Touchable.enabled);
        align(Align.top);
        setBackground(Assets.instance().table_border_patch);
        pad(4f);

        Table secondColumn = new Table();
//        secondColumn.setDebug(true);

        Label name = new Label(employee.getName(), Constants.LabelStyle());
        name.setFontScale(1.05f);

        Label money = new Label("" + employee.getSalaryText(), Constants.LabelStyle());

        Table skillsTable = initSkillsTable(employee.getSkillset());

        TextButton employButton = new TextButton("Employ", Constants.TextButtonStyle());
        employButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onEmploy.callback(employee);
            }
        });

        add(new EmployeeIcon(employee)).left().top().padRight(5);
        add(secondColumn).expand().fill();

        secondColumn.add(name).expandX().fillX().left().top();
        secondColumn.add(money).right().padLeft(5);
        secondColumn.row();
        secondColumn.add(skillsTable).expand().fill().left().top().padTop(5);
        secondColumn.add(employButton).right().bottom().padTop(5).prefWidth(70);
    }

    private static Table initSkillsTable(Collection<Skill> skillset) {
        Table skillsTable = new Table();
//        skillsTable.setDebug(true);

        for (final Skill s : skillset) {

            skillsTable.add(new Label(s.getDisplayType(), Constants.LabelStyle())).left().expandX();
            skillsTable.add(new Label(s.getDisplayValue(true), Constants.LabelStyle())).right().expandX().padBottom(3).padRight(15);
            skillsTable.row();
        }

        return skillsTable;
    }
}
