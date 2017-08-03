package de.hsd.hacking.Entities.Objects.Equipment.Items;

import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;

/**
 * An Equipment Item that increases the Bandwidth resource.
 * @author Dominik
 */


public class Modem extends Equipment implements Upgradable {

    public Modem() {
        super("Modem", 100, /*EquipmentAttributeType.BANDWIDTH, 100,*/ null, true, Direction.DOWN, 0, Direction.DOWN);
        data.setType(Proto.Equipment.EquipmentType.Modem);
    }

    @Override
    public int getBandwidthBonus() { return data.getLevel() * 100; }

    public void upgrade() {
        data.setLevel(data.getLevel() + 1);
        teamManager.updateResources();
    }

    public int getMaxLevel() { return 5; }

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
