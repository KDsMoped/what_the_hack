package de.hsd.hacking.Data.Tile;

import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Data.MovementProvider;
import de.hsd.hacking.Data.Path;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Tile;

/**
 * Returns various types of tiles from underlying TileMap.
 * @author Florian
 */
public interface TileMovementProvider extends MovementProvider {

    /**
     * Returns the next Tile that is free to move to
     * @return free Tile
     */
    Tile getNextTile();

    /**
     * Returns a path between 2 distinct tiles as a Path object with a List of tiles
     * @param startTile Tile
     * @param destinationTile Tile
     * @return Path
     */
    Path getPathToTile(Tile startTile, Tile destinationTile);

    Tile getDiscreteTile(Vector2 position);

    Tile getTile(int tileNumber);

    Tile findAndSetStartTile(Employee employee);
}
