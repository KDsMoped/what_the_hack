package de.hsd.hacking.Entities.Objects.Equipment.Items;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;

public class Router extends Equipment implements Upgradable {

    public Router() {
        super("Router", 200, /*EquipmentAttributeType.BANDWIDTH, 50,*/ null, true, Direction.DOWN, 0, Direction.DOWN);
        data.setType(Proto.Equipment.EquipmentType.Router);
    }

    public void upgrade() {
        data.setLevel(data.getLevel() + 1);
        team.updateResources();
    }
    public int getMaxLevel() { return 5; }

    @Override
    public int getBandwidthBonus() { return data.getLevel() * 50; }

    @Override
    public TextureRegionDrawable getIcon() {
        return Assets.instance().router_icon;
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
