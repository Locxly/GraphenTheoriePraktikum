/**
 * 
 */
package gka;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
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
	 * @param createdGraph
	 */
	public FlyodWarshallAlgorithm(Graph<String, DefaultWeightedEdge> createdGraph) {
		this.graph = createdGraph;
	}
	
	/**
	 * This method calculates the way between start and destination vertex using the floyd warshall algorithm.
	 * 
	 * @param startVertex
	 * @param destinationVertex
	 */
	public void calculate(String startVertex, String destinationVertex) {
		// Reference implementation to compare the result
		//floydWarshallJGraphT(startVertex, destinationVertex);
		
		// Using a different algorithm
		FloydWarshallImpl<String, DefaultWeightedEdge> floydWarshall = new FloydWarshallImpl<String, DefaultWeightedEdge>(graph);
		GraphPath<String, DefaultWeightedEdge> shortestPath = floydWarshall.calculateMinPath(startVertex, destinationVertex);
		if (shortestPath != null) {
			System.out.println("Shortest Path is [" + shortestPath.toString() + "]");
		} else {
			System.out.println("No path found.");
		}
	}

	/**
	 * The implementation of the floyd warshall algorithm which is included in jGraphT
	 * 
	 * @param startVertex
	 * @param destinationVertex
	 */
	private void floydWarshallJGraphT(String startVertex,
			String destinationVertex) {
		FloydWarshallShortestPaths<String, DefaultWeightedEdge> floydWarshall = new FloydWarshallShortestPaths<String, DefaultWeightedEdge>(graph);
		GraphPath<String, DefaultWeightedEdge> shortestPath = floydWarshall.getShortestPath(startVertex, destinationVertex);
		System.out.println("Shortest Path is [" + shortestPath.toString() + "]");
	}

}
