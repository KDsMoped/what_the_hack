package de.hsd.hacking.Data.Tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;

import de.hsd.hacking.Data.Path;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.IsometricTileManager;
import de.hsd.hacking.Entities.Objects.Object;
import de.hsd.hacking.Entities.Objects.PlaceHolderObject;
import de.hsd.hacking.Entities.Tile;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomIntPool;

/**
 * Holds all tiles in game and manages tile-movement through A* Pathfinder
 * Created by Cuddl3s on 24.05.2017.
 */
public class TileMap extends Group implements TileMovementProvider {

    private final GameStage stage;
    private TilePathFinder pathFinder;
    private Tile[][] tiles;


    public TileMap(GameStage stage){
        this.stage = stage;
        IsometricTileManager manager = new IsometricTileManager(new Vector2(GameStage.VIEWPORT_WIDTH / 2f - Constants.TILE_WIDTH / 2f, GameStage.VIEWPORT_HEIGHT - 105f));
        tiles = manager.generateTiles(Constants.TILE_WIDTH, Constants.TILES_PER_SIDE);
        this.pathFinder = new TilePathFinder(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                tiles[j][i].draw(batch, parentAlpha);
            }
        }
    }


    @Override
    public void act(float delta) {
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                tiles[j][i].act(delta);
            }
        }
    }

    @Override
    public Tile getNextTile() {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(Constants.TILES_PER_SIDE * Constants.TILES_PER_SIDE);
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].isMovableTo()){
                    possiblePositions.add(Constants.TILES_PER_SIDE * j + i);
                }
            }
        }
        if (possiblePositions.size() > 0) {
            Integer[] a = new Integer[1];
            RandomIntPool pool = new RandomIntPool(possiblePositions.toArray(a));
            int newTile = pool.getRandomNumber();
            int x = newTile % Constants.TILES_PER_SIDE;
            int y = newTile / Constants.TILES_PER_SIDE;
            return tiles[x][y];
        }
        return null;
    }

    @Override
    public Path getPathToTile(Tile startTile, Tile destinationTile){
        int sTileNumber = startTile.getTileNumber();
        int dTileNumber = destinationTile.getTileNumber();
        int sx = sTileNumber % getWidthInTiles();
        int sy = sTileNumber / getHeightInTiles();
        int tx = dTileNumber % getWidthInTiles();
        int ty = dTileNumber / getHeightInTiles();
        return pathFinder.findPath(sx, sy, tx, ty);
    }

    /**
     * DON'T USE FOR GETTING TILE OBJECTS! RETURNED TILE IS NOT ALWAYS CORRECT TILE
     */
    @Override
    public Tile getTileWhileMoving(Vector2 position) {
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if(tiles[i][j].isInTile(position)){
                    return tiles[i][j];
                }
            }
        }
        return null;
    }

    @Override
    public Tile getDiscreteTile(Vector2 position) {
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                Tile tile = tiles[i][j];
                if (tile.getPosition().x == position.x && tile.getPosition().y == position.y) {
                    return tile;
                }
            }
        }
        return null;
    }

    @Override
    public Tile getTile(int tileNumber) {
        int x = tileNumber % Constants.TILES_PER_SIDE;
        int y = tileNumber / Constants.TILES_PER_SIDE;
        return tiles[x][y];
    }


    @Override
    public Tile getStartTile(Employee employee) {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(Constants.TILES_PER_SIDE * Constants.TILES_PER_SIDE);
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].isMovableTo()){
                    possiblePositions.add(Constants.TILES_PER_SIDE * j + i);
                }
            }
        }
        if (possiblePositions.size() > 0){
            Integer[] a = new Integer[1];
            RandomIntPool pool = new RandomIntPool(possiblePositions.toArray(a));
            int newTile = pool.getRandomNumber();
            int x = newTile % Constants.TILES_PER_SIDE;
            int y = newTile / Constants.TILES_PER_SIDE;
            tiles[x][y].setOccupyingEmployee(employee);


            if (Constants.DEBUG) Gdx.app.log(Constants.TAG, toString());
            return tiles[x][y];
        }
        return null;

    }

    /**
     * Removes the specified employee from his current tile
     * @param employee
     */
    private void removeEmployee(Employee employee){
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].getOccupyingEmployee().equals(employee)){
                    tiles[i][j].setOccupyingEmployee(null);
                    return;
                }
            }
        }
    }

    /**
     * Removes the specified object from its current tile
     * @param object
     */
    private void removeObject(Object object){
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                Object tileObj = tiles[i][j].getObject();
                if (tileObj.equals(object)){
                    if (tileObj instanceof Touchable){
                        stage.removeTouchable((Touchable) object);
                    }
                    tiles[i][j].setObject(null);
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        String string = "TileMap: ";
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                int tileNumber = j * Constants.TILES_PER_SIDE + i;
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
        return Constants.TILES_PER_SIDE;
    }

    public int getHeightInTiles() {
        return Constants.TILES_PER_SIDE;
    }

    public boolean addObject(int tileNumber, Object object){
        int x = tileNumber % Constants.TILES_PER_SIDE;
        int y = tileNumber / Constants.TILES_PER_SIDE;
        return addObject(x, y, object);
    }

    public boolean addObject(int x, int y, Object object){
        if (tiles[x][y].hasNoObject()){
            tiles[x][y].setObject(object);
            if (object.isTouchable()){
                stage.addTouchable((Touchable) object);
            }
            int currentX = x;
            int currentY = y;
            for (int i = object.getOccupyAmount(); i > 0; i--) {
                switch (object.getOccupyDirection()){
                    case UP:
                        tiles[currentX][--currentY].setObject(new PlaceHolderObject(object));
                        break;
                    case RIGHT:
                        tiles[++currentX][currentY].setObject(new PlaceHolderObject(object));
                        break;
                    case DOWN:
                        tiles[currentX][++currentY].setObject(new PlaceHolderObject(object));
                        break;
                    case LEFT:
                        tiles[--currentX][currentY].setObject(new PlaceHolderObject(object));
                        break;
                }
            }

            return true;
        }
        return false;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Returns a tile that currently has no object or employee on it
     * @return tile
     */
    public Tile getFreeTile() {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(Constants.TILES_PER_SIDE * Constants.TILES_PER_SIDE);
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].hasNoObject() && tiles[i][j].isMovableTo()){
                    possiblePositions.add(Constants.TILES_PER_SIDE * j + i);
                }
            }
        }
        if (possiblePositions.size() > 0) {
            Integer[] a = new Integer[1];
            RandomIntPool pool = new RandomIntPool(possiblePositions.toArray(a));
            int newTile = pool.getRandomNumber();
            int x = newTile % Constants.TILES_PER_SIDE;
            int y = newTile / Constants.TILES_PER_SIDE;
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
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(Constants.TILES_PER_SIDE * Constants.TILES_PER_SIDE);
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].isMovableTo()){
                    possiblePositions.add(Constants.TILES_PER_SIDE * j + i);
                }
            }
        }
        if (possiblePositions.size() > 0) {
            Integer[] a = new Integer[1];
            RandomIntPool pool = new RandomIntPool(possiblePositions.toArray(a));
            int newTile = pool.getRandomNumber();
            int x = newTile % Constants.TILES_PER_SIDE;
            int y = newTile / Constants.TILES_PER_SIDE;
            removeEmployee(employee);
            tiles[x][y].setOccupyingEmployee(employee);

            if (Constants.DEBUG) Gdx.app.log(Constants.TAG, toString());
            return tiles[x][y].getPosition();
        }
        return null;
    }

    @Override
    public Vector2 getStartPosition(Employee employee) {
        return getStartTile(employee).getPosition().cpy();
    }

    public void debugCheck(int maxEmployees){
        int registeredEmployees = 0;
        int drawnEmployees = 0;
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[j][i].getOccupyingEmployee() != null){
                    registeredEmployees++;
                }
                drawnEmployees += tiles[j][i].getEmployeesToDrawSize();
            }
        }
        if (registeredEmployees != maxEmployees){
            throw new IllegalStateException("Count of registered employees in tileMap is different than actual employees. EmployeeCount: " +maxEmployees + ", registered: "+ registeredEmployees);
        }
        if (drawnEmployees != maxEmployees){
            throw new IllegalStateException("Different amount of employees drawn than there are actual employees. EmployeeCount: " +maxEmployees + ", drawn: "+ drawnEmployees);
        }

    }

}