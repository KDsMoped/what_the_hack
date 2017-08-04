package de.hsd.hacking.Entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Helper class to generate Isometric tileMap representation as a 2D Array.
 * Transforms position for isometric perspective.
 * @author Florian
 */
public class IsometricTileManager {

    private Vector2 topMiddlePos;

    /**
     * Instantiates an IsometricTileManager.
     * @param topMiddlePos1 The bottom left position of the top-center tile
     */
    public IsometricTileManager(final Vector2 topMiddlePos1){
       this.topMiddlePos = topMiddlePos1;
    }

    /**
     * Generates a grid of tiles with the given tileWidth consisting of tileAmountPerSide * tileAmountPerSide Tiles
     * @param tileWidth width of a single tile
     * @param tileAmountPerSide how many tiles should make up one side of the rectangular tile-grid
     * @return Tilemap as a 2D array.
     */
    public Tile[][] generateTiles(float tileWidth, int tileAmountPerSide) {
        Tile[][] tiles = new Tile[tileAmountPerSide][tileAmountPerSide];
        for (int x = 0; x < tileAmountPerSide; x++) {
            for (int y = 0; y < tileAmountPerSide; y++) {
                Vector2 position = topMiddlePos.cpy().add((x-y) * (tileWidth / 2f), - ((x+y) * (tileWidth / 4f)));
                tiles[x][y] = new Tile(position, y * tileAmountPerSide + x, tileWidth);
            }
        }
        return tiles;
    }
}
