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
	private Graph<Vertex, DefaultWeightedEdge> graph;

	/**
	 * The constructor.
	 * 
	 * @param createdGraph
	 */
	public FlyodWarshallAlgorithm(Graph<Vertex, DefaultWeightedEdge> createdGraph) {
		this.graph = createdGraph;
	}
	
	/**
	 * This method calculates the way between start and destination vertex using the floyd warshall algorithm.
	 * 
	 * @param startVertex
	 * @param destinationVertex
	 */
	public void calculate(Vertex startVertex, Vertex destinationVertex) {
		// Reference implementation to compare the result
		//floydWarshallJGraphT(startVertex, destinationVertex);
		
		// Using a different algorithm
		FloydWarshallImpl<Vertex, DefaultWeightedEdge> floydWarshall = new FloydWarshallImpl<Vertex, DefaultWeightedEdge>(graph);
		GraphPath<Vertex, DefaultWeightedEdge> shortestPath = floydWarshall.calculateMinPath(startVertex, destinationVertex);
		System.out.println("Shortest Path is [" + shortestPath.toString() + "]");
	}

	/**
	 * The implementation of the floyd warshall algorithm which is included in jGraphT
	 * 
	 * @param startVertex
	 * @param destinationVertex
	 */
	private void floydWarshallJGraphT(Vertex startVertex,
			Vertex destinationVertex) {
		FloydWarshallShortestPaths<Vertex, DefaultWeightedEdge> floydWarshall = new FloydWarshallShortestPaths<Vertex, DefaultWeightedEdge>(graph);
		GraphPath<Vertex, DefaultWeightedEdge> shortestPath = floydWarshall.getShortestPath(startVertex, destinationVertex);
		System.out.println("Shortest Path is [" + shortestPath.toString() + "]");
	}

}
