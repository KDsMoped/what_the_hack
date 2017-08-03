package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;
import de.hsd.hacking.Entities.Employees.States.WorkingState;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Computer;

public class Chair extends InteractableObject {
    private Array<TextureRegion> regions;
    private Computer computer;

    public Chair(Assets assets) {
        super(assets.chair, false, false, true, Direction.DOWN, 0, Direction.DOWN, false);
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

    @Override
    public void draw(Batch batch, float alpha){
        Vector2 drawPos = getPosition().sub(0, Constants.TILE_WIDTH / 4f);
        batch.draw(drawableRegion, drawPos.x, drawPos.y);
    }

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
