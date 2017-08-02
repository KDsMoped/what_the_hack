package de.hsd.hacking.Entities.Objects.Equipment.Items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Tile.TileMap;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;
import de.hsd.hacking.Entities.Objects.Desk;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Stages.GameStage;
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
        return data.getLevel() * 1;
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
