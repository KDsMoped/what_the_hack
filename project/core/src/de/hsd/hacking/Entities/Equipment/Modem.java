package de.hsd.hacking.Entities.Equipment;

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

    int level = 0;

    public Modem(float price, EquipmentAttributeLevel attributeLevel, Assets assets, Team team) {
        super(assets.computer.get(0), price, EquipmentAttributeType.BANDWIDTH, attributeLevel, true, Direction.DOWN, 0, Direction.DOWN, team);
    }

    public EquipmentType getType() { return EquipmentType.MODEM; }

    public void upgrade() {
        level++;
        team.addBandwidth(100);
    }

    public int getLevel() { return level; }

    public void setInitialLevel(int level) {
        this.level = level;
    }

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
