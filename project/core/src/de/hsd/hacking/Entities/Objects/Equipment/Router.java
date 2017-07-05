package de.hsd.hacking.Entities.Objects.Equipment;

import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;

/**
 * Created by domin on 28.06.2017.
 */

public class Router extends Equipment implements Upgradable {

    private int maxLevel;
    private int mul;


    public Router() {
        super("Ultra router 4000", 200, EquipmentAttributeType.BANDWIDTH, 50, null, true, Direction.DOWN, 0, Direction.DOWN);
    }

    public void upgrade() {
        level++;
        attributeValue += 50;
        updatePrice();
        team.updateResources();
    }

    public void setMaxLevel() { maxLevel = 5; }
    public void updatePrice() { price *= 2; }

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
