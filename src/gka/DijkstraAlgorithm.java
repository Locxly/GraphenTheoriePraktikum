/**
 * 
 */
package gka;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * This class contains the implementation of the dijkstra algorithm.
 * 
 * @author dreierm
 *
 */
public class DijkstraAlgorithm {
	
	/**
	 * The graph to work with.
	 */
	private Graph<String, DefaultWeightedEdge> graph;

	/**
	 * The constructor.
	 * 
	 * @param graph
	 */
	public DijkstraAlgorithm(Graph<String, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}
	
	/**
	 * This method calculates the way between start and destination vertex using the floyd warshall algorithm.
	 * 
	 * @param startVertex
	 * @param destinationVertex
	 */
	public void calculate(String startVertex, String destinationVertex) {
		;
	}

}
