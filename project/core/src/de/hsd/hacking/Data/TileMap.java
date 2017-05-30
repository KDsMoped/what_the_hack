package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Entities.IsometricTileManager;
import de.hsd.hacking.Entities.Tile;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomIntPool;

/**
 * Holds all tiles in game and manages tile-movement through A* Pathfinder
 * Created by Cuddl3s on 24.05.2017.
 */
public class TileMap extends Group implements TileMovementProvider  {

    private TilePathFinder pathFinder;
    private Tile[][] tiles;


    public TileMap(){
        IsometricTileManager manager = new IsometricTileManager(new Vector2(GameStage.VIEWPORT_WIDTH / 2f - Constants.TILE_WIDTH / 2f, GameStage.VIEWPORT_HEIGHT - 55f));
        tiles = manager.generateTiles(Constants.TILE_WIDTH, 5);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                addActor(tiles[i][j]);
            }
        }
        this.pathFinder = new TilePathFinder(this);
    }


    @Override
    public Tile getNextTile() {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(tiles.length * tiles.length);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].isMovableTo()){
                    possiblePositions.add(tiles.length * i + j);
                }
            }
        }
        if (possiblePositions.size() > 0) {
            Integer[] a = new Integer[1];
            RandomIntPool pool = new RandomIntPool(possiblePositions.toArray(a));
            int newTile = pool.getRandomNumber();
            int x = newTile / tiles.length;
            int y = newTile % tiles.length;
            return tiles[x][y];
        }
        return null;
    }

    @Override
    public Path getPathToTile(Tile startTile, Tile destinationTile){
        int sTileNumber = startTile.getTileNumber();
        int dTileNumber = destinationTile.getTileNumber();
        int sx = sTileNumber / getWidthInTiles();
        int sy = sTileNumber % getHeightInTiles();
        int tx = dTileNumber / getWidthInTiles();
        int ty = dTileNumber % getHeightInTiles();
        return pathFinder.findPath(sx, sy, tx, ty);
    }

    @Override
    public Tile getTile(Vector2 position) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if(tiles[i][j].isInTile(position)){
                    return tiles[i][j];
                }
            }
        }
        return null;
    }


    @Override
    public Vector2 getStartPosition(Employee employee) {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(tiles.length * tiles.length);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].isMovableTo()){
                    possiblePositions.add(tiles.length * i + j);
                }
            }
        }
        if (possiblePositions.size() > 0){
            Integer[] a = new Integer[1];
            RandomIntPool pool = new RandomIntPool(possiblePositions.toArray(a));
            int newTile = pool.getRandomNumber();
            int x = newTile / tiles.length;
            int y = newTile % tiles.length;
            tiles[x][y].setEmployee(employee);


            Gdx.app.log(Constants.TAG, toString());
            return tiles[x][y].getPosition().cpy();
        }
        return null;

    }

    /**
     * Places an object at the specified tile
     * @param entity object entity
     * @param tileNumber number of the tile to place object on (= x * tiles.length + y)
     * @return whether placement was successfull
     */
    public boolean placeObjectEntity(Entity entity, int tileNumber){
        //TODO Check ob entity auch wirklich object ist
        int x = tileNumber / tiles.length;
        int y = tileNumber % tiles.length;
        if (tiles[x][y].hasNoObject()){
            tiles[x][y].setObject(entity);
            return true;
        }
        return false;
    }

    /**
     * Removes the specified employee from his current tile
     * @param employee
     */
    private void removeEmployee(Employee employee){
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j].getEmployee().equals(employee)){
                    tiles[i][j].setEmployee(null);
                    return;
                }
            }
        }
    }

    /**
     * Removes the specified object from its current tile
     * @param object
     */
    private void removeObject(Entity object){
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j].getObject().equals(object)){
                    tiles[i][j].setObject(null);
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        String string = "TileMap: ";
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                int tileNumber = i * tiles.length + j;
                if (!tiles[i][j].isMovableTo()){
                    string += "Tile " + tileNumber + ": [ " + tiles[i][j].getName() + " ]. ";
                }else{
                    string += "Tile " + tileNumber + ": [ ]";
                }
            }
        }
        return string;
    }


    public int getWidthInTiles() {
        return tiles.length;
    }

    public int getHeightInTiles() {
        return tiles[0].length;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Returns a tile that currently has no object or employee on it
     * @return tile
     */
    public Tile getFreeTile() {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(tiles.length * tiles.length);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].hasNoObject() && tiles[i][j].isMovableTo()){
                    possiblePositions.add(tiles.length * i + j);
                }
            }
        }
        if (possiblePositions.size() > 0) {
            Integer[] a = new Integer[1];
            RandomIntPool pool = new RandomIntPool(possiblePositions.toArray(a));
            int newTile = pool.getRandomNumber();
            int x = newTile / tiles.length;
            int y = newTile % tiles.length;
            return tiles[x][y];
        }
        return null;
    }


    /**
     * Returns a random tile's position (Not needed for current implementation)
     * @param employee the employee to assign to the
     * @return
     */
    @Override
    public Vector2 getNextMovetoPoint(Employee employee) {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(tiles.length * tiles.length);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].isMovableTo()){
                    possiblePositions.add(tiles.length * i + j);
                }
            }
        }
        if (possiblePositions.size() > 0) {
            Integer[] a = new Integer[1];
            RandomIntPool pool = new RandomIntPool(possiblePositions.toArray(a));
            int newTile = pool.getRandomNumber();
            int x = newTile / tiles.length;
            int y = newTile % tiles.length;
            removeEmployee(employee);
            tiles[x][y].setEmployee(employee);

            Gdx.app.log(Constants.TAG, toString());
            return tiles[x][y].getPosition();
        }
        return null;
    }
}
