package de.hsd.hacking.Entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public class IsometricTileManager {

    private Vector2 topMiddlePos;

    public IsometricTileManager(Vector2 topMiddlePos){
       this.topMiddlePos = topMiddlePos;
    }

    public Tile[][] generateTiles(float tileWidth, int tileAmountPerSide){
        Tile[][] tiles = new Tile[tileAmountPerSide][tileAmountPerSide];
        for (int x = 0; x < tileAmountPerSide; x++) {
            for (int y = 0; y < tileAmountPerSide; y++) {
                Vector2 position = topMiddlePos.cpy().add(new Vector2(x-y * (tileWidth / 2f), x+y * (tileWidth / 4f)));
                tiles[x][y] = new Tile(position, x * 5 + y);
            }
        }
        return tiles;
    }
}
