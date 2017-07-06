package de.hsd.hacking.Entities.Objects.Equipment;

import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;

/**
 * Created by domin on 14.06.2017.
 */

public class Modem extends Equipment implements Upgradable {

    private int maxLevel;


    public Modem() {
        super("Super Modem 2000", 100, /*EquipmentAttributeType.BANDWIDTH, 100,*/ null, true, Direction.DOWN, 0, Direction.DOWN);
    }

    @Override
    public int getBandwidthBonus() { return level * 100; }

    public void upgrade() {
        level++;
        team.updateResources();
    }

    public void setMaxLevel() { maxLevel = 5; }

    public void onTouch() {};

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
