package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Proto;

/**
 * Created by Cuddl3s on 03.08.2017.
 */

public abstract class SpecialEmployee extends Employee {

    private SpecialEmployeeType type;

    public SpecialEmployee() {
        super();
    }

    public SpecialEmployee(Proto.Employee.Builder data) {
        super(data);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void drawAt(Batch batch, Vector2 pos) {
        super.drawAt(batch, pos);
    }
}
