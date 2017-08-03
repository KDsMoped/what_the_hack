package de.hsd.hacking.Entities.Team;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Tile.TileMap;
import de.hsd.hacking.Entities.Objects.Chair;
import de.hsd.hacking.Entities.Objects.Desk;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Computer;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Direction;

/**
 * A Workspace holds a pairing set of one {@link Desk}, {@link Chair} and {@link Computer}.
 */

public class Workspace {

    private Computer computer = null;
    private Desk desk;
    private Chair chair;

    /**
     * Creates {@link Desk} and {@link Chair} at the specified position on the {@link TileMap}
     * @param tileMap the TileMap object to place Desk and Chair on
     * @param tileX the x-position for the new Desk
     * @param tileY the y-position for the new Desk
     */
    public Workspace(TileMap tileMap, int tileX, int tileY) {
        desk = new Desk(Assets.instance(), Direction.RIGHT, 1);
        tileMap.addObject(tileX, tileY, desk);
        chair = new Chair(Assets.instance());
        tileMap.addObject(tileX, tileY - 1, chair);
    }

    /**
     * Assigns the given {@link Computer} object to the Desk and makes the Workspace usable by an
     * {@link de.hsd.hacking.Entities.Employees.Employee}.
     * @param computer the Computer to assign to the Workspace
     */
    public void addComputer(Computer computer) {
        this.computer = computer;
        computer.setWorkingChair(chair);
        GameStage.instance().addTouchable(computer);
        desk.setContainedObject(computer, 0);
    }
}
