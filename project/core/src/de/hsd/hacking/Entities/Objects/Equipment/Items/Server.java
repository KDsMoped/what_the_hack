package de.hsd.hacking.Entities.Objects.Equipment.Items;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Utils.Direction;

/**
 * Created by domin on 12.07.2017.
 */

public class Server extends Equipment implements Upgradable {
    int maxLevel;

    public Server() {
        super("Le Server", 400, null, true, Direction.DOWN, 0, Direction.DOWN);
    }

    public void upgrade() {
        level++;
        team.updateResources();
    }

    public void setMaxLevel() { maxLevel = 5; }

    @Override
    public int getBandwidthBonus() { return level * 100; }

    @Override
    public int getComputationPowerBonus() {
        return level * 50;
    }

    @Override
    public TextureRegionDrawable getIcon() {
        return super.getIcon();
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
