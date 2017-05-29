package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Entities.IsometricTileManager;
import de.hsd.hacking.Entities.Tile;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomIntPool;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public class TileMap implements MovementProvider {

    private Tile[][] tilePositions;


    public TileMap(){
        IsometricTileManager manager = new IsometricTileManager(new Vector2(GameStage.VIEWPORT_WIDTH / 2f, GameStage.VIEWPORT_HEIGHT - 50f));
        tilePositions = manager.generateTiles(20f, 5);
    }

    @Override
    public Vector2 getNextMovetoPoint(Employee employee) {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(tilePositions.length * tilePositions.length);
        for (int i = 0; i < tilePositions.length; i++) {
            for (int j = 0; j < tilePositions[i].length; j++) {
                if (tilePositions[i][j].isEmpty()){
                    possiblePositions.add(tilePositions.length * i + j);
                }
            }
        }
        Integer[] a = new Integer[1];
        RandomIntPool pool = new RandomIntPool(possiblePositions.toArray(a));
        int newTile = pool.getRandomNumber();
        int x = newTile / tilePositions.length;
        int y = newTile % tilePositions.length;
        removeEntity(employee);
        tilePositions[x][y].setEntity(employee);


        Gdx.app.log(Constants.TAG, toString());
        return tilePositions[x][y].getMiddlePosition();
    }



    @Override
    public Vector2 getStartPosition(Employee employee) {
        for (int i = 0; i < entityPositions.length; i++) {
            if(entityPositions[i] == null){
                entityPositions[i] = employee;
                return tilePositions[i].cpy();
            }
        }
        return null;
    }

    public boolean placeEntity(Entity entity, int tile){
        if (entityPositions[tile] == null){
            entityPositions[tile] = entity;
            return true;
        }
        return false;
    }

    private void removeEntity(Entity entity){
        for (int i = 0; i < tilePositions.length; i++) {
            for (int j = 0; j < tilePositions.length; j++) {
                if (tilePositions[i][j].getEntity().equals(entity)){
                    tilePositions[i][j].setEntity(null);
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        String string = "TileMap: ";
        for (int i = 0; i < entityPositions.length; i++) {
            if (entityPositions[i] != null){
                string += "Tile " + i + ": [ " + entityPositions[i].getName() + " ]. ";
            }else{
                string += "Tile " + i + ": [ ]";
            }

        }
        return string;
    }
}
