package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomIntPool;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public class TileMap implements MovementProvider {

    private Vector2[] tilePositions;
    private Entity[] entityPositions;


    public TileMap(){
        tilePositions = new Vector2[]{new Vector2(0, 0), new Vector2(100f, 100f), new Vector2(458, 0), new Vector2(256, 128), new Vector2(370, 50)};
        entityPositions = new Entity[tilePositions.length];
    }

    @Override
    public Vector2 getNextMovetoPoint(Employee employee) {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(tilePositions.length);
        for (int i = 0; i < entityPositions.length; i++) {
            if (entityPositions[i] == null){
                possiblePositions.add(i);
            }
        }
        Integer[] a = new Integer[1];
        RandomIntPool pool = new RandomIntPool(possiblePositions.toArray(a));
        int newTile = pool.getRandomNumber();
        removeEntity(employee);
        entityPositions[newTile] = employee;

        Gdx.app.log(Constants.TAG, toString());
        return tilePositions[newTile].cpy();
    }



    @Override
    public Vector2 getStartPosition(Employee employee) {
        for (int i = 0; i < entityPositions.length; i++) {
            if(entityPositions[i] == null){
                entityPositions[i] = employee;
                return tilePositions[i];
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
        for (int i = 0; i < entityPositions.length; i++) {
            if(entityPositions[i] != null && entityPositions[i].equals(entity)){
                entityPositions[i] = null;
                break;
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
