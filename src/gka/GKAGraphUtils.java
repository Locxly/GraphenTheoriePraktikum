/**
 * 
 */
package gka;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

/**
 * This class contains some utilities to create and display a graph and other
 * structures.
 * 
 * @author hoelschers, dreierm
 * 
 */
public class GKAGraphUtils {

	public static final String DEFAULT_GRAPH_FILE_LOCATION = "/Users/hoelschers/Documents/workspace/GraphenTheoriePraktikum/etc/graph_08b.graph";
	// public static final String DEFAULT_GRAPH_FILE_LOCATION =
	// "/Users/milena/Desktop/GKA/graph_01.graph";

	// Constants
	// Type of undirected graph
	static String GRAPH_TYPE_UNDIRECTED = "#ungerichtet";

	// Type of directed graph
	static String GRAPH_TYPE_DIRECTED = "#gerichtet";

	/** Delimiter to split the edge source data */
	static final String EDGE_SOURCE_DELIMITER = ",";

	static List<String> setOfVertex = new ArrayList<String>();

	/**
	 * Creates a graph from a given graph file.
	 * 
	 * @return a created graph.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RuntimeException
	 * @throws NumberFormatException
	 */
	static Graph<String, DefaultWeightedEdge> readGraphFromFile(
			String graphFileLocation) 
					throws FileNotFoundException,
			IOException, RuntimeException, NumberFormatException {
		BufferedReader input = new BufferedReader(new FileReader(
				graphFileLocation));
		// First we try to get the type of the graph.
		String graphType = input.readLine();
		if (!graphType.equals(GKAGraphUtils.GRAPH_TYPE_DIRECTED)
				&& !graphType.equals(GKAGraphUtils.GRAPH_TYPE_UNDIRECTED)) {
			// Type of graph is not supported.
			throw new RuntimeException(
					"Error in dataset line [1]. Graph type [" + graphType
							+ "] is not supported.");
		}
		// Next we get the data from the file.
		int count = 1;
		List<BaseSourceEdge> baseSourceList = new ArrayList<BaseSourceEdge>();
		String line = "";
		while ((line = input.readLine()) != null) {
			count++;
			String[] edgeSource = line
					.split(GKAGraphUtils.EDGE_SOURCE_DELIMITER);
			BaseSourceEdge source = new BaseSourceEdge();
			if (edgeSource.length == 3) {
				source.setVertexFrom(edgeSource[0]);
				source.setVertexTo(edgeSource[1]);
				source.setEdgeWeight(Long.parseLong(edgeSource[2]));
			} else if (edgeSource.length == 2) {
				source.setVertexFrom(edgeSource[0]);
				source.setVertexTo(edgeSource[0]);
				source.setEdgeWeight(0L);
			} else {
				// If array length is either 3 nor 2 the dataset is invalid.
				throw new RuntimeException("Error in dataset line [" + count
						+ "]. Count of attribute is [" + edgeSource.length
						+ "].");
			}
			baseSourceList.add(source);
		}
		input.close();

		Graph<String, DefaultWeightedEdge> createdGraph = null;
		if (GKAGraphUtils.GRAPH_TYPE_DIRECTED.equals(graphType)) {
			// We need a directed graph so let's create one.
			createdGraph = GKAGraphUtils.createDirectedGraph(baseSourceList);
		} else if (GKAGraphUtils.GRAPH_TYPE_UNDIRECTED.endsWith(graphType)) {
			// We need an undirected graph so let's create one.
			createdGraph = GKAGraphUtils.createUndirectedGraph(baseSourceList);
		}
		return createdGraph;
	}

	/**
	 * Display all vertex contains in the neighbor list for a vertex.
	 * 
	 * @param vertex
	 *            the vertex
	 * @param neighborList
	 *            the neighbor list
	 */
	private static void showNeighborListForVertex(String vertex,
			List<String> neighborList) {
		System.out.print("Vertex [" + vertex
				+ "] has following neighbor vertex [");
		for (String neighbor : neighborList) {
			System.out.print("(" + neighbor + ")");
		}
		System.out.println("].");
	}

	/**
	 * Creates an undirected graph from base source list.
	 * 
	 * @param baseSourceList
	 *            the base source list with graph data.
	 * @return an undirected graph.
	 */
	static UndirectedGraph<String, DefaultWeightedEdge> createUndirectedGraph(
			List<BaseSourceEdge> baseSourceList) {

		// Create an undirected graph.
		UndirectedGraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		// Now we add the edge to the graph.
		for (BaseSourceEdge item : baseSourceList) {
			// If possible we try add the from vertex.
			System.out.println("Graph already contains vertex [" + graph.containsVertex(item.getVertexFrom()) + "].");
			if (!graph.containsVertex(item.getVertexFrom())) {
				System.out
						.println("Add vertex [" + item.getVertexFrom() + "].");
				graph.addVertex(item.getVertexFrom());
				GKAGraphUtils.setOfVertex.add(item.getVertexFrom());
			}
			// If possible we try add the to_vertex.
			System.out.println("Graph already contains vertex [" + graph.containsVertex(item.getVertexTo()) + "].");
			if (!graph.containsVertex(item.getVertexTo())) {
				System.out.println("Add vertex [" + item.getVertexTo() + "].");
				graph.addVertex(item.getVertexTo());
				GKAGraphUtils.setOfVertex.add(item.getVertexTo());
			}
			// Let's check if the edge is already there. If not we will add the
			// edge.
			if (!graph.containsEdge(item.getVertexFrom(), item.getVertexTo())) {
				System.out.println("Add edge from vertex ["
						+ item.getVertexFrom() + "] to vertex ["
						+ item.getVertexTo() + "] with weigth ["
						+ item.getEdgeWeight().doubleValue() + "]");
				Graphs.addEdge(graph, item.getVertexFrom(), item.getVertexTo(),
						item.getEdgeWeight().doubleValue());
			}
		}

		return graph;
	}

	/**
	 * Creates a directed graph from base source list.
	 * 
	 * @param baseSourceList
	 *            the base source list with graph data.
	 * @return a directed graph.
	 */
	static Graph<String, DefaultWeightedEdge> createDirectedGraph(
			List<BaseSourceEdge> baseSourceList) {

		// Create an undirected graph.
		DirectedGraph<String, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		// Now we add the edge to the graph.
		for (BaseSourceEdge item : baseSourceList) {
			// If possible we try add the from_vertex.
			System.out.println("Graph already contains vertex [" + graph.containsVertex(item.getVertexFrom()) + "].");
			if (!graph.containsVertex(item.getVertexFrom())) {
				System.out
						.println("Add vertex [" + item.getVertexFrom() + "].");
				graph.addVertex(item.getVertexFrom());
				GKAGraphUtils.setOfVertex.add(item.getVertexFrom());
			}
			// If possible we try add the to_vertex.
			System.out.println("Graph already contains vertex [" + graph.containsVertex(item.getVertexTo()) + "].");
			if (!graph.containsVertex(item.getVertexTo())) {
				System.out.println("Add vertex [" + item.getVertexTo() + "].");
				graph.addVertex(item.getVertexTo());
				GKAGraphUtils.setOfVertex.add(item.getVertexFrom());
			}
			// Let's check if the edge is already there. If not we will add the
			// edge.
			if (!graph.containsEdge(item.getVertexFrom(), item.getVertexTo())) {
				System.out.println("Add edge from vertex ["
						+ item.getVertexFrom() + "] to vertex ["
						+ item.getVertexTo() + "] with weigth ["
						+ item.getEdgeWeight().doubleValue() + "]");
				Graphs.addEdge(graph, item.getVertexFrom(), item.getVertexTo(),
						item.getEdgeWeight().doubleValue());
			}
		}

		return graph;
	}

}
