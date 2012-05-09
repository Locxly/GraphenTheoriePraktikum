/**
 * 
 */
package gka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.GraphPathImpl;

/**
 * The implementation is based on the implementation of jGraphT. The main
 * methods are simplified and also renamed because of better readable names.
 * Also I deleted a lot of unused stuff for our implementation.
 * 
 * @author hoelschers
 * 
 */
public class FloydWarshallImpl<V, E> {
	
	// Der Graph zum Bearbeiten.
	private Graph<V, E> graph;
	// Die Menge der Knoten im Graph.
    private List<V> vertexSet;
    // Die Adjadenzmatrix.
    private double [][] adjacencyArray = null;
    // Die Matrix mit den kürzesten Pfaden zwischen den Knoten.
    private int [][] shortPathArray = null;

 
    /**
     * The constructor initializes the adjacency array and the vertex set.
     * 
     * @param graph
     */
    public FloydWarshallImpl(Graph<V, E> graph) {
    	// Setze den Graph.
        this.graph = graph;
        // Hole die Menge aller Knoten im Graph.
        this.vertexSet = new ArrayList<V>(graph.vertexSet());
        // Initialisiere die FW-Matrix.
        initalizeMatrix(graph);
        
    }

	/**
	 * Initialize the floy warshall matrix
	 * 
	 * @param graph
	 */
	private void initalizeMatrix(Graph<V, E> graph) {
		int n = vertexSet.size();
        
        // Initialisieren des shortPathArray mit -1.
		// D.h. im Moment ist immer der direkte Weg zwischen zwei Knoten der kürzeste
        shortPathArray = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(shortPathArray[i], -1);
        }
 
        // Initialisieren der Ajadenzmatrix. Alle Werte werden auf unendlich gesetzt.
        adjacencyArray = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(adjacencyArray[i], Double.POSITIVE_INFINITY);
        }
 
        // Initialisieren der Ajadenzmatrix. Die Mitteldiagonale (die Vertex 
        // verbinden sich hier selbst) wird initialisiert. Der Wert ist per 
        // Definition 0.
        for (int i = 0; i < n; i++) {
            adjacencyArray[i][i] = 0.0;
        }
 
        // Initialisieren der Ajadenzmatrix. Eintragen der Kantengewichte 
        // bei Kanten zwischen den Knoten. Ist der Graph ungerichtet, wird 
        // für das Reziproke der selbe Wert eingetragen.
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
        System.out.println("Write matrix.");
        writeMatrix(n);
        
        // Initialisieren der Ajadenzmatrix. Jetzt findet der eigentlich 
        // Floyd-Warsgall-Alg. statt.
        // Für jede Knotenverbindung i nach j wird überprüft, ob es einen Knoten 
        // k gibt, über den die Verbindung 'kürzer' ist. Ist dies der Fall, so 
        // wird der Wert in der Adjadenzmatrix gespeichert und der Knoten wird 
        // als Verbindung in die ShortestPathMatrix gespeichert.
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                	if (i != k && j != k) {
						double ik_kj = adjacencyArray[i][k]
								+ adjacencyArray[k][j];
						if (ik_kj < adjacencyArray[i][j]) {
							adjacencyArray[i][j] = ik_kj;
							shortPathArray[i][j] = k;
						}
                	}
                }
            }
        }
        System.out.println("Write matrix.");
        writeMatrix(n);
	}

	/**
	 * @param n
	 */
	private void writeMatrix(int n) {
		// Ausgabe der Adjadenzmatrix.
        for (int i = 0; i < n; i++) {
        	for (int j = 0; j < n; j++) {
        		if (Double.POSITIVE_INFINITY == adjacencyArray[i][j]) {
        			System.out.print("[I]|");
        		} else {
        			System.out.print("[" + adjacencyArray[i][j] + "]|");
        		}
        	}
        	System.out.println("");
        }
        System.out.println("");
	}
 
    /**
     * Recursive method for the search of path between given vertexes.
     *  
     * @param edges all edges will be saved for the back track
     * @param indexVertexA the first vertex
     * @param indexVertexB the second vertex
     */
    private void recursivePathTrac(List<E> edges, int indexVertexA, int indexVertexB) {
    	// Finde einen Zwischenknoten für die Verbindung der beiden Knoten.
        int indexSubsequentVertex = shortPathArray[indexVertexA][indexVertexB];
        // Gibt es keinen und gibt es eine Kante, nehme diese in den kürztesten Weg auf. 
        // Ansonsten ist diese Verbindung zwischen den Knoten nicht gegeben.
        if (indexSubsequentVertex == -1) {
            E edge = graph.getEdge(vertexSet.get(indexVertexA), vertexSet.get(indexVertexB));
            if (edge != null) {
                edges.add(edge);
            }
        } else {
        	// Gibt es einen Zwischenknoten, führe für beide Teilstücke einen 
        	// rekursiven Aufruf durch.
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
        // Finde die Indizees für die übergebenen Vertexe
    	int indexStartVertex = vertexSet.indexOf(startVertex);
        int indexDestinationVertex = vertexSet.indexOf(destinationVertex);
 
        // Finde rekursiv die kürzeste Verbindung zwischen den beiden Knoten.
        List<E> edges = new ArrayList<E>();
        recursivePathTrac(edges, indexStartVertex, indexDestinationVertex);
 
        // Gibt es keine, gib null zurück.
        if (edges.size() < 1) {
            return null;
        }
 
        // Baue den Pfad auf und gebe ihn zurück.
		GraphPathImpl<V, E> path = new GraphPathImpl<V, E>(graph, startVertex,
				destinationVertex, edges, edges.size());
        return path;
    }
 
}
