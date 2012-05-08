/**
 * 
 */
package gka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.GraphPathImpl;
import org.jgrapht.util.VertexPair;

/**
 * The implementation is based on the implementation of jGraphT. The main
 * methods are simplified and also renamed because of better readable names.
 * 
 * @author hoelschers
 * 
 */
public class FloydWarshallImpl<V, E> {
	private Graph<V, E> graph;
    private List<V> vertexSet;
    private double diameter = 0.0;
    private double [][] adjacencyArray = null;
    private int [][] shortPathArray = null;
    private Map<VertexPair<V>, GraphPath<V, E>> shortestPathMap = null;
 
    /**
     * The constructor initializes the adjacency array and the vertex set.
     * 
     * @param graph
     */
    public FloydWarshallImpl(Graph<V, E> graph) {
        this.graph = graph;
        this.vertexSet = new ArrayList<V>(graph.vertexSet());
        
        initalizeMatrix(graph);
        
		Map<VertexPair<V>, GraphPath<V, E>> shortPaths = new HashMap<VertexPair<V>, GraphPath<V, E>>();

		for (int i = 0; i < vertexSet.size(); i++) {
			for (int j = 0; j < vertexSet.size(); j++) {
				if (i == j) {
					continue;
				}

				V vertexA = vertexSet.get(i);
				V vertexB = vertexSet.get(j);

				GraphPath<V, E> path = calculateMinPath(vertexA, vertexB);

				// we got a path
				if (path != null) {
					shortPaths.put(new VertexPair<V>(vertexA, vertexB), path);
				}
			}
		}

		this.shortestPathMap = shortPaths;
    }

	/**
	 * Initialize the floy warshall matrix
	 * 
	 * @param graph
	 */
	private void initalizeMatrix(Graph<V, E> graph) {
		int n = vertexSet.size();
        
        // init the backtrace matrix
        shortPathArray = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(shortPathArray[i], -1);
        }
 
        // initialize matrix, 0
        adjacencyArray = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(adjacencyArray[i], Double.POSITIVE_INFINITY);
        }
 
        // initialize matrix, 1
        for (int i = 0; i < n; i++) {
            adjacencyArray[i][i] = 0.0;
        }
 
        // initialize matrix, 2
        Set<E> edges = graph.edgeSet();
        for (E edge : edges) {
            V v1 = graph.getEdgeSource(edge);
            V v2 = graph.getEdgeTarget(edge);
 
            int v_1 = vertexSet.indexOf(v1);
            int v_2 = vertexSet.indexOf(v2);
 
            adjacencyArray[v_1][v_2] = graph.getEdgeWeight(edge);
            if (!(graph instanceof DirectedGraph<?, ?>)) {
                adjacencyArray[v_2][v_1] = graph.getEdgeWeight(edge);
            }
        }
        
        // initialize matrix, 3
        // run fw alg
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double ik_kj = adjacencyArray[i][k] + adjacencyArray[k][j];
                    if (ik_kj < adjacencyArray[i][j]) {
                        adjacencyArray[i][j] = ik_kj;
                        shortPathArray[i][j] = k;
                        diameter = (diameter > adjacencyArray[i][j]) ? diameter : adjacencyArray[i][j];
                    }
                }
            }
        }
	}
 
    /**
     * Get the length of a shortest path.
     *
     * @param vertexA first vertex
     * @param vertexB second vertex
     *
     * @return shortest distance between a and b
     */
    public double shortestDistance(V vertexA, V vertexB) { 
        return adjacencyArray[vertexSet.indexOf(vertexA)][vertexSet.indexOf(vertexB)];
    }
 
    /**
     * Recursive method for the search of path between given vertexes.
     *  
     * @param edges all edges will be saved for the back track
     * @param indexVertexA the first vertex
     * @param indexVertexB the second vertex
     */
    private void recursivePathTrac(List<E> edges, int indexVertexA, int indexVertexB) {
        int indexSubsequentVertex = shortPathArray[indexVertexA][indexVertexB];
        if (indexSubsequentVertex == -1) {
            E edge = graph.getEdge(vertexSet.get(indexVertexA), vertexSet.get(indexVertexB));
            if (edge != null) {
                edges.add(edge);
            }
        } else {
            recursivePathTrac(edges, indexVertexA, indexSubsequentVertex);
            recursivePathTrac(edges, indexSubsequentVertex, indexVertexB);
        }
    }
 
    /**
     * Calculates the shortest path between two given vertexes.
     *
     * @param startVertex start vertex
     * @param destinationVertex destination vertex
     *
     * @return the shorted path between the vertexes. If no path exists null.
     */ 
    public GraphPath<V, E> calculateMinPath(V startVertex, V destinationVertex) {
        int indexStartVertex = vertexSet.indexOf(startVertex);
        int indexDestinationVertex = vertexSet.indexOf(destinationVertex);
 
        List<E> edges = new ArrayList<E>();
        recursivePathTrac(edges, indexStartVertex, indexDestinationVertex);
 
        // no path, return null
        if (edges.size() < 1) {
            return null;
        }
 
		GraphPathImpl<V, E> path = new GraphPathImpl<V, E>(graph, startVertex,
				destinationVertex, edges, edges.size());
 
        return path;
    }
 
    /**
     * Search in path map for all known paths from a given vertex.
     *
     * @param vertex the vertex
     *
     * @return all known paths with vertex
     */
    public List<GraphPath<V, E>> getShortestPaths(V vertex) {
        List<GraphPath<V, E>> found = new ArrayList<GraphPath<V, E>>();
        for (VertexPair<V> pair : shortestPathMap.keySet()) {
            if (pair.hasVertex(vertex)) {
                found.add(shortestPathMap.get(pair));
            }
        }
 
        return found;
    }
}
