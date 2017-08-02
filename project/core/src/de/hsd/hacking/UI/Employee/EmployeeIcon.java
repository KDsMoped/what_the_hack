package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Utils.Provider.EmployeeProvider;

/**
 * This UI element displays an {@link Employee} like an icon.
 *
 * @author Hendrik
 */
public class EmployeeIcon extends Container<Actor>{

    private EmployeeProvider employee;

    public EmployeeIcon(final Employee employee){

        this.employee = new EmployeeProvider() {
            @Override
            public Employee get() {
                return employee;
            }
        };
//        setDebug(true);

        init();
    }

    public EmployeeIcon(EmployeeProvider employeeProvider){

        employee = employeeProvider;

        init();
    }

    private void init(){
        setBackground(Assets.instance().table_border_patch);
        size(32, 52);
//        Gdx.app.log("begin ", "size of icon: " + getPrefWidth() + ", " + getPrefHeight());

    }

    @Override

    public void act(float delta) {

        Employee e = employee.get();
        if (e.getMovementProvider() != null) {
            e.animAct(delta);
        }

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (employee.get() == null) {
            return;
        }

        super.draw(batch, parentAlpha);

//        Gdx.app.log("", "size of icon: " + getPrefWidth() + ", " + getPrefHeight());

        employee.get().drawAt(batch, new Vector2(getX() + 2, getY() + 1));
    }
}
