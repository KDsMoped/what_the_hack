package de.hsd.hacking.Data;

import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Tile;

/**
 * Created by Cuddl3s on 30.05.2017.
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

    /**
     * Returns the tile to a given position
     * @param position The position to search a tile for
     * @return The found tile
     */
    Tile getTileWhileMoving(Vector2 position);

    Tile getDiscreteTile(Vector2 position);

    Tile getTile(int tileNumber);

    Tile getStartTile(Employee employee);
}
