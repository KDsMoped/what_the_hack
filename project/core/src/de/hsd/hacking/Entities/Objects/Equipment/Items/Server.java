package de.hsd.hacking.Entities.Objects.Equipment.Items;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Direction;

/**
 * An Equipment Item that increases the Bandwidth and ComputationPower resources.
 * @author Dominik
 */


public class Server extends Equipment implements Upgradable {

    public Server() {
        super("Server", 400, null, true, Direction.DOWN, 0, Direction.DOWN);
        data.setType(Proto.Equipment.EquipmentType.Server);
    }

    public void upgrade() {
        data.setLevel(data.getLevel() + 1);
        teamManager.updateResources();
    }

    public int getMaxLevel() { return 5; }

    @Override
    public int getBandwidthBonus() { return data.getLevel() * 100; }

    @Override
    public int getComputationPowerBonus() {
        return data.getLevel() * 50;
    }

    @Override
    public TextureRegionDrawable getIcon() {
        return super.getIcon();
    }

    public void onTouch() {}

    @Override
    public EmployeeState interact(Employee e) {
        return null;
    }

    @Override
    public void occupy() {

    }

    @Override
    public void deOccupy() {

    }

    @Override
    public boolean isDelegatingInteraction() {
        return false;
    }
}
