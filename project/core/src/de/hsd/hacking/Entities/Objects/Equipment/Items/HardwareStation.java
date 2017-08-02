package de.hsd.hacking.Entities.Objects.Equipment.Items;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Direction;

public class HardwareStation extends Equipment implements Upgradable {

    private float elapsedTime = 0f;
    private int state = 1;

    public HardwareStation(){
        super("Hardware Station", 800, null, true, Direction.DOWN, 0, Direction.DOWN);
        data.setType(Proto.Equipment.EquipmentType.HardwareStation);
    }

    public void upgrade() {
        data.setLevel(data.getLevel() + 1);
        team.updateResources();
    }

    public int getMaxLevel() { return 5; }

    @Override
    public int getHardwareSkillBonus() {
        return data.getLevel();
    }

    @Override
    public void onTouch() {

    }

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
