/**
 * 
 */
package gka;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * This class contains the implementation of the floyd warshall algorithm.
 * 
 * @author hoelschers
 *
 */
public class FlyodWarshallAlgorithm {
	
	/**
	 * The graph to work with.
	 */
	private Graph<String, DefaultWeightedEdge> graph;

	/**
	 * The constructor.
	 * 
	 * @param graph
	 */
	public FlyodWarshallAlgorithm(Graph<String, DefaultWeightedEdge> graph) {
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
