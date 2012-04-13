package gka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.WeightedMultigraph;


/**
 * GKA group 6
 */

/**
 * We try the new library and explore the possibilities to create graphs and so
 * on. This is the HelloJGraph example from source forge.
 * 
 * @author hoelschers
 * 
 */
public class ExampleJGraphT {

	// Constants
	// Type of undirected graph
	private static String GRAPH_TYPE_UNDIRECTED = "#ungerichtet";

	// Type of directed graph
	private static String GRAPH_TYPE_DIRECTED = "#gerichtet";

	private static List<String> setOfVertex = new ArrayList<String>();

	private static boolean depthFirstFinish = false;

	private static boolean breadthFirstFinish = false;

	private static List<String> vertexWayList;

	// Delimiter to split the edge source data
	private static final String EDGE_SOURCE_DELIMITER = ",";
	
	//~ Constructors ----------------------------------------------------------
    private ExampleJGraphT()
    {
    } // ensure non-instantiability.

    //~ Methods ---------------------------------------------------------------
    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     * @throws IOException Error during process the input file.
     */
    public static void main(String [] args) throws IOException
    {
//        UndirectedGraph<String, DefaultEdge> stringGraph = createStringGraph();
//
//        // note undirected edges are printed as: {<v1>,<v2>}
//        System.out.println(stringGraph.toString());
//
//        // create a graph based on URL objects
//        DirectedGraph<URL, DefaultEdge> hrefGraph = createHrefGraph();
//
//        // note directed edges are printed as: (<v1>,<v2>)
//        System.out.println(hrefGraph.toString());
    	
    	BufferedReader input = new BufferedReader(new FileReader("/Users/hoelschers/Documents/workspace/GraphenTheoriePraktikum/etc/graph_01.graph"));
    	
        // First we try to get the type of the graph.
        String graphType = input.readLine();
        if (!graphType.equals(GRAPH_TYPE_DIRECTED) && !graphType.equals(GRAPH_TYPE_UNDIRECTED) ) {
        	// Type of graph is not supported.
        	throw new RuntimeException("Error in dataset line [1]. Graph type [" + graphType + "] is not supported.");
        }
        
        // Next we get the data from the file.
        int count = 1; 
        List<BaseSourceEdge> baseSourceList = new ArrayList<BaseSourceEdge>();
        String line = "";
        while ((line = input.readLine()) != null) {
        	count++;
        	String[] edgeSource = line.split(EDGE_SOURCE_DELIMITER);
        	BaseSourceEdge source = new BaseSourceEdge();
        	if (edgeSource.length == 3) {
				source.setVertexFrom(edgeSource[0]);
				source.setVertexTo(edgeSource[1]);
				source.setEdgeWeight(Long.parseLong(edgeSource[2]));
			} else if (edgeSource.length == 2) {
				source.setVertexFrom(edgeSource[0]);
				source.setVertexTo(edgeSource[1]);
				source.setEdgeWeight(0L);
        	} else {
        		// If array length is either 3 nor 2 the dataset is invalid.
        		throw new RuntimeException("Error in dataset line [" + count + "]. Count of attribute is [" + edgeSource.length + "].");
        	}
        	baseSourceList.add(source);
        }
        input.close();
        
        Graph<String,DefaultWeightedEdge> createdGraph = null;
        if (GRAPH_TYPE_DIRECTED.equals(graphType)) {
        	// We need a directed graph so let's create one.
        	createdGraph = createDirectedGraph(baseSourceList);
        } else if (GRAPH_TYPE_UNDIRECTED.endsWith(graphType)) {
        	// We need an undirected graph so let's create one.
        	createdGraph = createUndirectedGraph(baseSourceList);
        }
        
        if (createdGraph != null) {
        	System.out.println("Graph : " + createdGraph.toString());
        	
        	// Now we try to find a vertex.
        	String startVertex = setOfVertex.get((int) Math.round(Math.random()*(setOfVertex.size()-1)));
        	String destinationVertex = setOfVertex.get((int) Math.round(Math.random()*(setOfVertex.size()-1)));
        	System.out.println("Search the way between [" + startVertex + "] and [" + destinationVertex + "].");
        	
        	// First via depth first search.
        	try {
        		depthFirstSearchForVertex(createdGraph, startVertex, destinationVertex);
        	} catch (Exception e) {
        		System.err.println("Finalize depth first search after exception.");
        	}
        	
        	// Now via breadth first search
        	try {
        		breadthFirstSearchForVertex(createdGraph, startVertex, destinationVertex);
        	} catch (Exception e) {
        		System.err.println("Finalize breadth first search after exception.");
        	}
        	
        } else {
        	// Could not create graph.
        	throw new RuntimeException("Error. Could not create valid error.");
        }
        
//        // Tree graph
//        DirectedGraph<String, DefaultEdge> treeGraph = createTreeGraph();
//        
//        System.out.println(treeGraph.toString());
//        
//        List<String> predNode3 = Graphs.neighborListOf(treeGraph, "Node_3");
//        
//        System.out.println(predNode3.toString());
    }

    
    
    
    /**
	 * Search the way between startVertex and destination vertex using the breadth
	 * first search.
	 * 
	 * @param createdGraph
	 *            the graph to search
	 * @param startVertex
	 *            the start vertex
	 * @param destinationVertex
	 *            the destination vertex
	 */
	private static void breadthFirstSearchForVertex(
			Graph<String, DefaultWeightedEdge> createdGraph,
			String startVertex, String destinationVertex) {
		System.out.println("Using breadth first search.");
		
		if (startVertex.equals(destinationVertex)) {
			System.out.println("Start vertex [" + startVertex + "] equals destination vertex [" + destinationVertex + "].");
			return;
		}
		
		vertexWayList = new ArrayList<String>();
		
		vertexWayList.add(startVertex);
		
		recursiveBreadthFirstStep(createdGraph, startVertex, destinationVertex, "");
		
		displayFinalWayToVertex();
	}


	/**
	 * @param createdGraph
	 * @param startVertex
	 * @param destinationVertex
	 */
	private static void recursiveBreadthFirstStep(
			Graph<String, DefaultWeightedEdge> createdGraph,
			String startVertex, String destinationVertex, String predecessorVertex) {
		//displayFinalWayToVertex();
		List<String> neighborList = Graphs.neighborListOf(createdGraph, startVertex);
		if (neighborList == null || neighborList.size() == 0 || (neighborList.size() == 1 && neighborList.contains(predecessorVertex))) {
			System.out.println("Vertex [" + startVertex + "] has no neighbours exept the pedecessor. It's time for the recursive step.");
			return;
		}
		//showNeighborListForVertex(startVertex, neighborList);
		for (String vertex : neighborList) {
			vertexWayList.add(vertex);
			displayFinalWayToVertex();
			if (vertex.equals(destinationVertex)) {
				System.out.println("Vertex found.");
				breadthFirstFinish  = true;
				return;
			} else {
				removeLastVertexFromWayToVertex(vertex);
			}
		}
		
		for (String vertex : neighborList) {
			if (vertex.equals(predecessorVertex)) {
				System.out.println("Skip vertex [" + vertex + "]. It is the predecessor [" + startVertex + "]");
			} else {
				if (vertexWayList.contains(vertex)) {
					break;
				}
				//System.out.println("Next vertex in List is [" + vertex + "].");
				vertexWayList.add(vertex);
				recursiveBreadthFirstStep(createdGraph, vertex, destinationVertex, startVertex);
				if (breadthFirstFinish) {
					return;
				} else {
					removeLastVertexFromWayToVertex(vertex);
				}
			}
		}
	}

	/**
	 * @param vertex
	 */
	private static void removeLastVertexFromWayToVertex(String vertex) {
		vertexWayList.remove(vertexWayList.lastIndexOf(vertex));
	}

	/**
	 * Search the way between startVertex and destination vertex using the depth
	 * first search.
	 * 
	 * @param createdGraph
	 *            the graph to search
	 * @param startVertex
	 *            the start vertex
	 * @param destinationVertex
	 *            the destination vertex
	 */
	private static void depthFirstSearchForVertex(
			Graph<String, DefaultWeightedEdge> createdGraph,
			String startVertex, String destinationVertex) {
		System.out.println("Using depth first search.");
		
		if (startVertex.equals(destinationVertex)) {
			System.out.println("Start vertex [" + startVertex + "] equals destination vertex [" + destinationVertex + "].");
			return;
		}
		
		vertexWayList = new ArrayList<String>();
		
		vertexWayList.add(startVertex);
		
		// Now we make several recursive depth first steps.
		recursiveDepthFirstStep(createdGraph, startVertex, destinationVertex, "");

		displayFinalWayToVertex();
	}

	/**
	 * Recursive steps for depth first search.
	 * 
	 * @param createdGraph
	 *            the graph to search
	 * @param startVertex
	 *            the start vertex
	 * @param destinationVertex
	 *            the destination vertex
	 * @param predecessorVertex
	 * 			  the predecessor vertex
	 */
	private static void recursiveDepthFirstStep(
			Graph<String, DefaultWeightedEdge> createdGraph,
			String startVertex, String destinationVertex, String predecessorVertex) {
		displayFinalWayToVertex();
		List<String> neighborList = Graphs.neighborListOf(createdGraph, startVertex);
		if (neighborList == null || neighborList.size() == 0) {
			return;
		}
		//showNeighborListForVertex(startVertex, neighborList);
		for (String vertex : neighborList) {
			if (depthFirstFinish) {
				return;
			}
			if (vertex.equals(predecessorVertex)) {
				;
			} else {
				if (vertexWayList.contains(vertex)) {
					break;
				}
				//System.out.println("Vertex on stack [" + vertex + "].");
				if (vertex.equals(destinationVertex)) {
					System.out.println("Vertex found.");
					depthFirstFinish = true;
				}
				vertexWayList.add(vertex);
				if (depthFirstFinish) {
					return;
				}
				recursiveDepthFirstStep(createdGraph, vertex,
						destinationVertex, startVertex);
				if (!depthFirstFinish) {
					removeLastVertexFromWayToVertex(vertex);
				}
			}
		}
	}
	
	/**
	 * Display all vertex contains in the neighbor list for a vertex
	 * 
	 * @param vertex the vertex
	 * @param neighborList the neighbor list
	 */
	private static void showNeighborListForVertex(String vertex, List<String> neighborList) {
		System.out.print("Vertex [" + vertex + "] has following neighbor vertex [");
		for (String neighbor : neighborList) {
			System.out.print("(" + neighbor + ")");
		}
		System.out.println("].");
	}

	/**
	 * Display the content of the final way to vertex.
	 */
	private static void displayFinalWayToVertex() {
		System.out.print("Way from start vertex to destination vertex was : [");
		int count = 1;
		for (String vertex : vertexWayList) {
			if (count < vertexWayList.size()) {
				System.out.print("{" + vertex + "} -> ");
				count++;
			} else {
				System.out.print("{" + vertex + "}");
			}
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
	private static UndirectedGraph<String,DefaultWeightedEdge> createUndirectedGraph(List<BaseSourceEdge> baseSourceList) {
		
		// Create an undirected graph.
		UndirectedGraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		// Now we add the edge to the graph.
		for (BaseSourceEdge item : baseSourceList) {
			// If possible we try add the from vertex.
			if (!graph.containsVertex(item.getVertexFrom())) {
				System.out.println("Add vertex [" + item.getVertexFrom() + "].");
				graph.addVertex(item.getVertexFrom());
				setOfVertex.add(item.getVertexFrom());
			}
			// If possible we try add the to vertex.
			if (!graph.containsVertex(item.getVertexTo())) {
				System.out.println("Add vertex [" + item.getVertexTo() + "].");
				graph.addVertex(item.getVertexTo());
				setOfVertex.add(item.getVertexTo());
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
    private static Graph<String, DefaultWeightedEdge> createDirectedGraph(List<BaseSourceEdge> baseSourceList) {
		
		// Create an undirected graph.
		DirectedGraph<String, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		// Now we add the edge to the graph.
		for (BaseSourceEdge item : baseSourceList) {
			// If possible we try add the from vertex.
			if (!graph.containsVertex(item.getVertexFrom())) {
				graph.addVertex(item.getVertexFrom());
			}
			// If possible we try add the to vertex.
			if (!graph.containsVertex(item.getVertexTo())) {
				graph.addVertex(item.getVertexTo());
			}
			// Let's check if the edge is already there. If not we will add the
			// edge.
			if (!graph.containsEdge(item.getVertexFrom(), item.getVertexTo())) {
				Graphs.addEdge(graph, item.getVertexFrom(), item.getVertexTo(),
						item.getEdgeWeight().doubleValue());
			}
		}

		return graph;
	}

	private static DirectedGraph<String, DefaultEdge> createTreeGraph() {
    	DirectedGraph<String, DefaultEdge> g =
                new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
    	
    	g.addVertex("Node_0");
    	g.addVertex("Node_1");
    	g.addVertex("Node_2");
    	g.addVertex("Node_3");
    	g.addVertex("Node_4");
    	g.addVertex("Node_5");
    	g.addVertex("Node_6");
    	g.addVertex("Node_7");
    	g.addVertex("Node_8");
    	
    	g.addEdge("Node_0", "Node_1");
    	g.addEdge("Node_0", "Node_2");
    	g.addEdge("Node_1", "Node_3");
    	g.addEdge("Node_1", "Node_4");
    	g.addEdge("Node_2", "Node_5");
    	g.addEdge("Node_2", "Node_6");
    	g.addEdge("Node_3", "Node_7");
    	g.addEdge("Node_3", "Node_8");
    	
    	return g;
    }
    
    /**
     * Creates a toy directed graph based on URL objects that represents link
     * structure.
     *
     * @return a graph based on URL objects.
     */
    private static DirectedGraph<URL, DefaultEdge> createHrefGraph()
    {
        DirectedGraph<URL, DefaultEdge> g =
            new DefaultDirectedGraph<URL, DefaultEdge>(DefaultEdge.class);

        try {
            URL amazon = new URL("http://www.amazon.com");
            URL yahoo = new URL("http://www.yahoo.com");
            URL ebay = new URL("http://www.ebay.com");

            // add the vertices
            g.addVertex(amazon);
            g.addVertex(yahoo);
            g.addVertex(ebay);

            // add edges to create linking structure
            g.addEdge(yahoo, amazon);
            g.addEdge(yahoo, ebay);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return g;
    }

    /**
     * Craete a toy graph based on String objects.
     *
     * @return a graph based on String objects.
     */
    private static UndirectedGraph<String, DefaultEdge> createStringGraph()
    {
        UndirectedGraph<String, DefaultEdge> g =
            new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add the vertices
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        // add edges to create a circuit
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v4);
        g.addEdge(v4, v1);

        return g;
    }
}