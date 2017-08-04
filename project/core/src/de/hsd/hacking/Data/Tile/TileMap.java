package de.hsd.hacking.Data.Tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;

import de.hsd.hacking.Data.Path;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.IsometricTileManager;
import de.hsd.hacking.Entities.Objects.ContainerObject;
import de.hsd.hacking.Entities.Objects.Object;
import de.hsd.hacking.Entities.Objects.PlaceHolderObject;
import de.hsd.hacking.Entities.Tile;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomIntPool;

/**
 * Holds all tiles in game and manages tile-movement through A* Pathfinder
 * @author Florian
 */
public class TileMap extends Group implements TileMovementProvider {

    private final GameStage stage;
    private TilePathFinder pathFinder;
    private Tile[][] tiles;


    public TileMap(GameStage stage){
        this.stage = stage;
        Gdx.app.log("HackingGame" ,"Tile Startheight: " + (Constants.VIEWPORT_HEIGHT - 105f));
        IsometricTileManager manager = new IsometricTileManager(new Vector2(Constants.VIEWPORT_WIDTH / 2f - Constants.TILE_WIDTH / 2f, 183f));
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

    /**
     * Gets random tile that is movable to.
     * @return tile that is not blocked.
     */
    @Override
    public Tile getNextTile() {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(Constants.TILES_PER_SIDE * Constants.TILES_PER_SIDE);
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].isMovableTo()) {
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
     * Uses pathfinding to find a path between startTile and destinationTile
     * @param startTile startingPosition
     * @param destinationTile endPosition
     * @return Path as a series of Tiles if path found. Null otherwise.
     */
    @Override
    public Path getPathToTile(final Tile startTile, final Tile destinationTile){
        int sTileNumber = startTile.getTileNumber();
        int dTileNumber = destinationTile.getTileNumber();
        int sx = sTileNumber % getWidthInTiles();
        int sy = sTileNumber / getHeightInTiles();
        int tx = dTileNumber % getWidthInTiles();
        int ty = dTileNumber / getHeightInTiles();
        return pathFinder.findPath(sx, sy, tx, ty);
    }

    /**
     * Gets a tile for a given position.
     * @param position bottom left position of a tile.
     * @return Tile that corresponds to position.
     */
    @Override
    public Tile getDiscreteTile(final Vector2 position) {
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

    /**
     * Returns the tile for a given tileNumber.
     * @param tileNumber tile number of desired tile.
     * @return desired tile.
     */
    @Override
    public Tile getTile(int tileNumber) {
        int x = tileNumber % Constants.TILES_PER_SIDE;
        int y = tileNumber / Constants.TILES_PER_SIDE;
        return tiles[x][y];
    }


    /**
     * Searches and returns a valid starting tile for an employee.
     * @param employee Employee to find tile for.
     * @return The found tile.
     */
    @Override
    public Tile findAndSetStartTile(final Employee employee) {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(Constants.TILES_PER_SIDE * Constants.TILES_PER_SIDE);
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].isMovableTo()) {
                    possiblePositions.add(Constants.TILES_PER_SIDE * j + i);
                }
            }
        }
        //Randomly choose a tile from possible tiles.
        if (possiblePositions.size() > 0) {
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
     * Removes the specified employee from his current tile.
     * @param employee1 Employee to remove.
     */
    private void removeEmployee(final Employee employee1) {
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].getOccupyingEmployee().equals(employee1)) {
                    tiles[i][j].setOccupyingEmployee(null);
                    return;
                }
            }
        }
    }

    /**
     * Removes the specified object from its current tile.
     * @param object1 Object to be removed.
     */
    private void removeObject(final Object object1) {
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                Object tileObj = tiles[i][j].getObject();
                if (tileObj.equals(object1)) {
                    if (tileObj instanceof Touchable) {
                        stage.removeTouchable((Touchable) object1);
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
                if (!tiles[i][j].isMovableTo()) {
                    string += "Tile " + tileNumber + ": [ " + tiles[i][j].getName() + " ]. ";
                } else {
                    string += "Tile " + tileNumber + ": [ ]";
                }
            }
            string += "\n";
        }
        return string;
    }


    public int getWidthInTiles() {
        return Constants.TILES_PER_SIDE;
    }

    public int getHeightInTiles() {
        return Constants.TILES_PER_SIDE;
    }

    /**
     * Adds an object to given tile.
     * @param tileNumber1 tile number of desired tile.
     * @param object1 object to be placed on tile
     * @return True if object could be placed. False otherwise
     */
    public boolean addObject(final int tileNumber1, final Object object1) {
        int x = tileNumber1 % Constants.TILES_PER_SIDE;
        int y = tileNumber1 / Constants.TILES_PER_SIDE;
        return addObject(x, y, object1);
    }

    public boolean addObject(final int x, final int y, final Object object1) {
        if (tiles[x][y].hasNoObject()) {
            tiles[x][y].setObject(object1);
            if (object1.isTouchable()) {
                stage.addTouchable((Touchable) object1);
            }
            int currentX = x;
            int currentY = y;
            for (int i = object1.getOccupyAmount(); i > 0; i--) {
                switch (object1.getOccupyDirection()) {
                    case UP:
                        tiles[currentX][--currentY].setObject(new PlaceHolderObject(object1));
                        break;
                    case RIGHT:
                        tiles[++currentX][currentY].setObject(new PlaceHolderObject(object1));
                        break;
                    case DOWN:
                        tiles[currentX][++currentY].setObject(new PlaceHolderObject(object1));
                        break;
                    case LEFT:
                        tiles[--currentX][currentY].setObject(new PlaceHolderObject(object1));
                        break;
                }
            }

            return true;
        }
        return false;
    }

    Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Returns a tile that currently has no object or employee on it.
     * @return Free tile.
     */
    public Tile getFreeTile() {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(Constants.TILES_PER_SIDE * Constants.TILES_PER_SIDE);
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].hasNoObject() && tiles[i][j].isMovableTo()) {
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
     * Returns a random tile's position (Not needed for current implementation).
     * @param employee the employee to assign to the found tile.
     * @return the world position of the next point to moe to.
     */
    @Override
    public Vector2 getNextMovetoPoint(final Employee employee) {
        ArrayList<Integer> possiblePositions = new ArrayList<Integer>(Constants.TILES_PER_SIDE * Constants.TILES_PER_SIDE);
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].isMovableTo()) {
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

    /**
     * Gets a starting tile and returns its world position.
     * @param employee Employee to set to tile.
     * @return World position of starting tile.
     */
    @Override
    public Vector2 getStartPosition(final Employee employee) {
        return findAndSetStartTile(employee).getPosition().cpy();
    }

    /**
     * Checks whether employees on tile map (drawn employees and occupying employees) are equal to the amount of employees currently in the team.
     * Throws an exception if that condition is false.
     * @param maxEmployees The number that drawn and occupying employees should be equal to. Normally team amount.
     */
    public void debugCheck(final int maxEmployees) {
        int registeredEmployees = 0;
        int drawnEmployees = 0;
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[j][i].getOccupyingEmployee() != null) {
                    registeredEmployees++;
                }
                drawnEmployees += tiles[j][i].getEmployeesToDrawSize();
            }
        }
        if (registeredEmployees != maxEmployees) {
            throw new IllegalStateException("Count of registered employees in tileMap is different than actual employees. EmployeeCount: " + maxEmployees + ", registered: " + registeredEmployees);
        }
        if (drawnEmployees != maxEmployees) {
            throw new IllegalStateException("Different amount of employees drawn than there are actual employees. EmployeeCount: " + maxEmployees + ", drawn: " + drawnEmployees);
        }

    }

    /**
     * Finds the tile a given object is assigned to.
     * @param obj Object to find a tile for.
     * @return Found tile or null if object is not found on any tile.
     */
    public Tile findObject(final Object obj) {
        for (int i = 0; i < Constants.TILES_PER_SIDE; i++) {
            for (int j = 0; j < Constants.TILES_PER_SIDE; j++) {
                if (tiles[i][j].getObject() == null)
                    continue;

                if (tiles[i][j].getObject() == obj) {
                    return tiles[i][j];
                }

                if (ContainerObject.class.isAssignableFrom(tiles[i][j].getObject().getClass())) {
                    if (((ContainerObject) tiles[i][j].getObject()).getContainedObject(0) == obj) {
                        return tiles[i][j];
                    }
                }
            }
        }

        return null;
    }
}
