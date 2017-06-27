package de.hsd.hacking.Entities.Objects.Equipment;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeState;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Entities.Team.Team;

/**
 * Created by domin on 14.06.2017.
 */

public class Modem extends Equipment implements Upgradable {

    private int maxLevel;
    private int mul;


    public Modem() {
        super("Super Modem 2000", 100, EquipmentAttributeType.BANDWIDTH, 100, null, true, Direction.DOWN, 0, Direction.DOWN);
    }

    public void upgrade() {
        level++;
        team.addBandwidth(attributeValue);
    }

    public void setMaxLevel() { maxLevel = 5; }
    public void setUpgradePriceMultiplier() { mul = 2; }

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
