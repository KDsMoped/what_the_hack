package de.hsd.hacking.Entities.Team;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Tile.TileMap;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Objects.Chair;
import de.hsd.hacking.Entities.Objects.Desk;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Computer;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Direction;

/**
 * Created by domin on 07.06.2017.
 *
 * A Workspace holds one Employee and one Computer.
 */

public class Workspace {

    //private Employee employee;
    private Computer computer = null;
    private Desk desk;
    private Chair chair;
    private TileMap tileMap;

    public Workspace(TileMap tileMap, int tileX, int tileY) {
        this.tileMap = tileMap;
        desk = new Desk(Assets.instance(), Direction.RIGHT, 1);
        tileMap.addObject(tileX, tileY, desk);
        chair = new Chair(Assets.instance());
        tileMap.addObject(tileX, tileY - 1, chair);
    }



    public Computer getComputer() { return computer; }
    public void addComputer(Computer computer) {
        this.computer = computer;
        computer.setWorkingChair(chair);
        GameStage.instance().addTouchable(computer);
        desk.setContainedObject(computer);
    }

}
