package de.hsd.hacking.Entities.Objects.Equipment;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeState;

/**
 * Created by domin on 27.06.2017.
 */

public class Coffeemaker extends Equipment {

    public Coffeemaker(Assets assets){
        super("Coffee Maker 5000", 100, EquipmentAttributeType.SKILL_ALLPURPOSE, 5, null, true, Direction.DOWN, 0, Direction.DOWN);
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
