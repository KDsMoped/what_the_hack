package de.hsd.hacking.Data;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Entities.Tile;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public class Path {

    private List<Tile> path;

    public Path(){
        this.path = new ArrayList<Tile>();
    }

    public void addToPath(Tile tile){
        if (path.contains(tile)){
            throw new IllegalArgumentException("Path can't contain more than one instance of the same tile");
        }
        path.add(tile);
    }

    public Tile getNextStep(){
        return path.remove(0);
    }

    public boolean isPathFinished(){
        return path.size() == 0;
    }

    public void prependTile(Tile tile) {
        path.add(0, tile);
    }
}
