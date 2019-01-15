package misc.graphs;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.exceptions.NoPathExistsException;
import misc.exceptions.NoSuchKeyException;

//added imports
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.IDisjointSet;
import static misc.Searcher.topKSort;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've constrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.
    
    
    // IDEAS: have chained hash dictionary of V --> ChainedHashSet of Edges
    //        when we need to know what vertices something is adjacent to, traverse the ChainedHashSet
    //        and use the default getOtherVertexMethod
    // Will this allow parallel edges? If the edges are separate objects yes.
    private IDictionary<V, ChainedHashSet<E>> connections;
    private IList<E> edges;
    private IList<V> vertices;
    private IDisjointSet<V> connectedComponents;
    
    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        this.edges = edges;
        this.vertices = vertices;
        this.connections = new ChainedHashDictionary<>(vertices.size() + 1);
        this.connectedComponents = new ArrayDisjointSet<V>();
        for (V vertex : vertices) {
            connections.put(vertex, new ChainedHashSet<>());
            connectedComponents.makeSet(vertex); //make set for every single vertex (one element)
        }
        for (E edge: edges) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("Edges cannot have negative weights");
            }
            V v1 = edge.getVertex1();
            V v2 = edge.getVertex2();
            try {
                connections.get(v1).add(edge); // adds edge to set of edges for vertices v1, v2
                connections.get(v2).add(edge);
            } catch (NoSuchKeyException ex) {
                throw new IllegalArgumentException("Edge is connected to vertex not present in graph");
            }
           // components will only be part of the same set if they share an edge and are
           // not already part of the same set.
           // Helps deal with parallel edge case as well, as both vertexes will already be part of
           // the same disjoint set.
           if (connectedComponents.findSet(v1) != connectedComponents.findSet(v2)) {
                connectedComponents.union(v1, v2);
           }
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return vertices.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return edges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        ISet<E> minSpanTree = new ChainedHashSet<>();
        IDisjointSet<V> currentComponents = new ArrayDisjointSet<>();
        for (V vertex : this.vertices) {
            currentComponents.makeSet(vertex);
        }
        IList<E> sortedEdges = topKSort(numEdges(), this.edges);
        for (E edge : sortedEdges) {
            V vertex1 = edge.getVertex1();
            V vertex2 = edge.getVertex2();
            if (currentComponents.findSet(vertex1) != currentComponents.findSet(vertex2)) {
                currentComponents.union(vertex1, vertex2);
                minSpanTree.add(edge);
            }
        }
        return minSpanTree;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {       
        if (connectedComponents.findSet(start) != connectedComponents.findSet(end)) {
            throw new NoPathExistsException("Start and end are not connected");
        }       
        IPriorityQueue<PathNode<V, E>> unprocessed = new ArrayHeap<>();
        ISet<V> processed = new ChainedHashSet<V>();
        
        PathNode<V, E> current = new PathNode<V, E>(start, 0, null, null); //
        while (!current.vertex.equals(end)) {
            processed.add(current.vertex);
            ISet<E> adjacentEdges = connections.get(current.vertex);
            for (E edge : adjacentEdges) {
                V otherVertex = edge.getOtherVertex(current.vertex);
                if (!processed.contains(otherVertex)) {
                    double newPathSum = current.currentPathSum + edge.getWeight();
                    unprocessed.insert(new PathNode<V, E>(otherVertex, newPathSum, edge, current));
                }
            }
            
            // because we are adding duplicate elements rather than updating priority, we end up sometimes reaching
            // a vertex after it has already been processed. This can lead to a lot of redundant work, searching
            // through vertices that have already been explored.
            while (processed.contains(current.vertex)) {
                current = unprocessed.removeMin();
            }
        }
        
        // at this point, current.vertex is end; we can just traverse path backwards now to find
        // shortest path
        IList<E> shortestPath = new DoubleLinkedList<E>();
        while (!current.vertex.equals(start)) {
            // since we are traversing path backwards, we have to reverse order of list.
            shortestPath.insert(0, current.lastPath);
            current = current.predecessor;
        }
        return shortestPath;
    }
    
    private static class PathNode<V, E> implements Comparable<PathNode<V, E>> {
        public double currentPathSum;
        public E lastPath;
        public PathNode<V, E> predecessor;
        public V vertex;
        
        public PathNode(V vertex) {
            this(vertex, Double.POSITIVE_INFINITY, null, null);
        }
        
        public PathNode(V vertex, double pathSum, E edge, PathNode<V, E> predecessor) {
            this.vertex = vertex;
            this.currentPathSum = pathSum;
            this.lastPath = edge;
            this.predecessor = predecessor;
        }
        
        
        public int compareTo(PathNode<V, E> other) {
            if (this.currentPathSum - other.currentPathSum > 0) {
                return 1;
            } else if (this.currentPathSum - other.currentPathSum == 0) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
