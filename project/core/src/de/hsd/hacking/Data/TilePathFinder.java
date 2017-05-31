package de.hsd.hacking.Data;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public class TilePathFinder implements PathFinder {

    private TileMap tileMap;
    private TileMapHeuristics heuristics;
    private ArrayList closed = new ArrayList();
    private SortedList open = new SortedList();
    private Node[][] nodes;

    public TilePathFinder(TileMap tileMap){
        this.tileMap = tileMap;
        this.heuristics = new TileMapHeuristics();
        nodes = new Node[tileMap.getWidthInTiles()][tileMap.getHeightInTiles()];
        for (int x=0;x<tileMap.getWidthInTiles();x++) {
            for (int y=0;y<tileMap.getHeightInTiles();y++) {
                nodes[x][y] = new Node(x,y);
            }
        }
    }

    @Override
    public Path findPath(int sx, int sy, int tx, int ty) {
        int maxSearchDistance = 3 * (Math.max(sx, tileMap.getWidthInTiles() - sx) + Math.max(sy, tileMap.getHeightInTiles() - sy));

        if (!isValidTargetLocation(sx, sy, tx, ty))return null;

        // initial state for A*. The closed group is empty. Only the starting

        // tile is in the open list and it'e're already there
        nodes[sx][sy].depth = 0;
        nodes[sx][sy].cost = 0;
        closed.clear();
        open.clear();
        open.add(nodes[sx][sy]);

        nodes[tx][ty].parent = null;

        int currentDepth = 0;
        while (currentDepth < maxSearchDistance && (open.size() != 0)){

            // pull out the first node in our open list, this is determined to
            // be the most likely to be the next step based on our heuristic

            Node current = getFirstInOpen();
            if (current == nodes[tx][ty]) {
                break;
            }

            removeFromOpen(current);
            addToClosed(current);

            // search through all the neighbours of the current node evaluating

            // them as next steps

            for (int x=-1;x<2;x++) {
                for (int y=-1;y<2;y++) {
                    // not a neighbour, its the current tile
                    if ((x == 0) && (y == 0)) {
                        continue;
                    }
                    //Don't allow diagonal movement
                    if ((x != 0) && (y != 0)) {
                        continue;
                    }

                    int xp = x + current.x;
                    int yp = y + current.y;
                    if (isValidMoveLocation(sx,sy,xp,yp)) {
                        // the cost to get to this node is cost the current plus the movement
                        // cost to reach this node. Note that the heursitic value is only used
                        // in the sorted open list

                        float nextStepCost = current.cost + 1;
                        Node neighbour = nodes[xp][yp];
//                        map.pathFinderVisited(xp, yp);

                        // if the new cost we've determined for this node is lower than
                        // it has been previously makes sure the node hasn'e've
                        // determined that there might have been a better path to get to
                        // this node so it needs to be re-evaluated

                        if (nextStepCost < neighbour.cost) {
                            if (inOpenList(neighbour)) {
                                removeFromOpen(neighbour);
                            }
                            if (inClosedList(neighbour)) {
                                removeFromClosed(neighbour);
                            }
                        }

                        // if the node hasn't already been processed and discarded then
                        // reset it's cost to our current cost and add it as a next possible
                        // step (i.e. to the open list)

                        if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                            neighbour.cost = nextStepCost;
                            neighbour.heuristic = heuristics.getCost(xp, yp, tx, ty);
                            currentDepth = Math.max(currentDepth, neighbour.setParent(current));
                            addToOpen(neighbour);
                        }
                    }
                }
            }
        }
        // since we'e've run out of search
        // there was no path. Just return null

        if (nodes[tx][ty].parent == null) {
            return null;
        }

        // At this point we've definitely found a path so we can uses the parent

        // references of the nodes to find out way from the target location back

        // to the start recording the nodes on the way.

        Path path = new Path();
        Node target = nodes[tx][ty];
        while (target != nodes[sx][sy]) {
            path.prependTile(tileMap.getTiles()[target.x][target.y]);
            target = target.parent;
        }
        path.prependTile(tileMap.getTiles()[sx][sy]);

        // thats it, we have our path

        return path;
    }


    /**
     * Get the first element from the open list. This is the next
     * one to be searched.
     *
     * @return The first element in the open list
     */
    protected Node getFirstInOpen() {
        return (Node) open.first();
    }

    /**
     * Add a node to the open list
     *
     * @param node The node to be added to the open list
     */
    protected void addToOpen(Node node) {
        open.add(node);
    }

    /**
     * Check if a node is in the open list
     *
     * @param node The node to check for
     * @return True if the node given is in the open list
     */
    protected boolean inOpenList(Node node) {
        return open.contains(node);
    }

    /**
     * Remove a node from the open list
     *
     * @param node The node to remove from the open list
     */
    protected void removeFromOpen(Node node) {
        open.remove(node);
    }

    /**
     * Add a node to the closed list
     *
     * @param node The node to add to the closed list
     */
    protected void addToClosed(Node node) {
        closed.add(node);
    }

    /**
     * Check if the node supplied is in the closed list
     *
     * @param node The node to search for
     * @return True if the node specified is in the closed list
     */
    protected boolean inClosedList(Node node) {
        return closed.contains(node);
    }

    /**
     * Remove a node from the closed list
     *
     * @param node The node to remove from the closed list
     */
    protected void removeFromClosed(Node node) {
        closed.remove(node);
    }

    /**
     * Check if a given location is valid for the supplied mover
     *
     * @param sx The starting x coordinate
     * @param sy The starting y coordinate
     * @param x The x coordinate of the location to check
     * @param y The y coordinate of the location to check
     * @return True if the location is valid for the given mover
     */
    protected boolean isValidTargetLocation(int sx, int sy, int x, int y) {
        boolean invalid = (x < 0) || (y < 0) || (x >= tileMap.getWidthInTiles()) || (y >= tileMap.getHeightInTiles());

        if ((!invalid) && ((sx != x) || (sy != y))) {
            invalid = !tileMap.getTiles()[x][y].isMovableTo();
        }
        return !invalid;
    }

    /**
     * Check if a given location is valid for the supplied mover
     *
     * @param sx The starting x coordinate
     * @param sy The starting y coordinate
     * @param x The x coordinate of the location to check
     * @param y The y coordinate of the location to check
     * @return True if the location is valid for the given mover
     */
    protected boolean isValidMoveLocation(int sx, int sy, int x, int y) {
        boolean invalid = (x < 0) || (y < 0) || (x >= tileMap.getWidthInTiles()) || (y >= tileMap.getHeightInTiles());

        if ((!invalid) && ((sx != x) || (sy != y))) {
            invalid = !tileMap.getTiles()[x][y].isMovableThrough();
        }
        return !invalid;
    }

    /**
     * A simple sorted list
     *
     * @author kevin (http://www.cokeandcode.com/main/tutorials/path-finding/)
     */
    private class SortedList {
        /** The list of elements */
        private ArrayList list = new ArrayList();

        /**
         * Retrieve the first element from the list
         *
         * @return The first element from the list
         */
        public Object first() {
            return list.get(0);
        }

        /**
         * Empty the list
         */
        public void clear() {
            list.clear();
        }

        /**
         * Add an element to the list - causes sorting
         *
         * @param o The element to add
         */
        public void add(Object o) {
            list.add(o);
            Collections.sort(list);
        }

        /**
         * Remove an element from the list
         *
         * @param o The element to remove
         */
        public void remove(Object o) {
            list.remove(o);
        }

        /**
         * Get the number of elements in the list
         *
         * @return The number of element in the list
         */
        public int size() {
            return list.size();
        }

        /**
         * Check if an element is in the list
         *
         * @param o The element to search for
         * @return True if the element is in the list
         */
        public boolean contains(Object o) {
            return list.contains(o);
        }
    }

    /**
     * A single node in the search graph
     */
    private class Node implements Comparable {
        /** The x coordinate of the node */
        private int x;
        /** The y coordinate of the node */
        private int y;
        /** The path cost for this node */
        private float cost;
        /** The parent of this node, how we reached it in the search */
        private Node parent;
        /** The heuristic cost of this node */
        private float heuristic;
        /** The search depth of this node */
        private int depth;

        /**
         * Create a new node
         *
         * @param x The x coordinate of the node
         * @param y The y coordinate of the node
         */
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Set the parent of this node
         *
         * @param parent The parent node which lead us to this node
         * @return The depth we have no reached in searching
         */
        public int setParent(Node parent) {
            depth = parent.depth + 1;
            this.parent = parent;

            return depth;
        }

        /**
         * @see Comparable#compareTo(Object)
         */
        public int compareTo(Object other) {
            Node o = (Node) other;

            float f = heuristic + cost;
            float of = o.heuristic + o.cost;

            if (f < of) {
                return -1;
            } else if (f > of) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}