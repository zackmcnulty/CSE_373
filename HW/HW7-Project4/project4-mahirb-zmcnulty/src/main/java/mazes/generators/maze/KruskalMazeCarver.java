package mazes.generators.maze;

import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Wall;
import mazes.entities.Room;
import datastructures.concrete.ChainedHashSet;
import misc.graphs.Graph;
import java.util.Random;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.

        Random rand = new Random();

        ISet<Wall> walls = maze.getWalls();
        ISet<Room> rooms = maze.getRooms();
        ISet<Wall> untouchableWalls = maze.getUntouchableWalls();
        for (Wall wall : walls) {
            wall.setDistance(rand.nextInt(Integer.MAX_VALUE));
        }
        Graph<Room, Wall> graph = new Graph<>(rooms, walls);
        ISet<Wall> mst = graph.findMinimumSpanningTree();

        ISet<Wall> toRemove = new ChainedHashSet<>();
        for (Wall wall : walls) {
            if (mst.contains(wall) && !untouchableWalls.contains(wall)) {
                toRemove.add(wall);
            }
            wall.resetDistanceToOriginal();
        }
        return toRemove;
    }
}
