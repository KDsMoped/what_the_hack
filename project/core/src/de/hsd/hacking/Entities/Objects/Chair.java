package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeState;
import de.hsd.hacking.Entities.Employees.WorkingState;
import de.hsd.hacking.Entities.Objects.Equipment.Computer;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 20.06.2017.
 */

public class Chair extends InteractableObject {
    private Array<TextureRegion> regions;
    private Computer computer;

    public Chair(Assets assets) {
        super(assets.chair, false, false, true, Direction.DOWN, 0, Direction.DOWN);
    }

    @Override
    public EmployeeState interact(Employee e) {
        this.setBlocking(true);

        return new WorkingState(e, getPosition(), this.computer);
    }

    @Override
    public void deOccupy() {
        setOccupied(false);
        this.setBlocking(false);
    }

    @Override
    public boolean isDelegatingInteraction() {
        return false;
    }

    /*@Override
    public void draw(Batch batch, float alpha){

    }*/

    @Override
    public void occupy() {
        setOccupied(true);
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }
}
