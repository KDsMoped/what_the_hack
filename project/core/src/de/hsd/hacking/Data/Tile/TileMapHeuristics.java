package de.hsd.hacking.Data.Tile;


/**
 * Holds Heuristic Tile Movement Cost values
 * @author Florian
 */
public class TileMapHeuristics {

    /**
     * Calculates "Manhattan Distance" and returns it as cost
     * @param x x value of the tile being evaluated
     * @param y y value of the tile being evaluated
     * @param tx x value of the target tile
     * @param ty y value of the target tile
     * @return Manhattan Distance cost
     */
    public float getCost(int x, int y, int tx, int ty){

        float dx = Math.abs(tx - x);
        float dy = Math.abs(ty - y);

        return dx+dy;
    }
}
