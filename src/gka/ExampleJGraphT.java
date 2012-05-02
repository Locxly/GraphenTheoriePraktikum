package gka;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;


/**
 * GKA group 6
 * 
 * Dokumentationskopf
 * ------------------
 *
 * Team 6: 					Stephan Hölscher, Milena Dreier
 *
 * Aufgabenaufteilung: 		Gemeinsame Bearbeitung der gesamten Aufgabe
 *							Verwendung von Saros zur gemeinsamen Entwicklung
 *
 * Quellenangaben: 			https://github.com/jgrapht/jgrapht/wiki/HelloWorld
 * 							http://jgrapht.org/javadoc/
 * 							Christoph Klauck, Christoph Mass: Graphentheorie für Studierende der Informatik, 4. Auflage.
 * 
 * Bearbeitungszeitraum: 	Start am 04.04.2012, Fertigstellung am 17.04.2012
 * 							tägliche Bearbeitung (Ausnahme Ostern) von ca. 1 Stunde
 *
 * Aktueller Stand:			fertig :)
 *							eventuell Änderungen am Programmier-Stil und zur Optimierung nötig
 * 
 * @author hoelschers, dreierm
 * 
 */
public class ExampleJGraphT {

	private static boolean depthFirstFinish = false;

	private static boolean breadthFirstFinish = false;

	private static List<String> vertexWayList;
	
	// Initialize the runtime attributes in default state
	private static String filename = GKAGraphUtils.DEFAULT_GRAPH_FILE_LOCATION;
	private static String startVertex = null;
	private static String destinationVertex = null;

	//~ Constructors ----------------------------------------------------------
    private ExampleJGraphT()
    {
    } // ensure non-instantiability.

    //~ Methods ---------------------------------------------------------------
    /**
     * The starting point for the demo.
     *
     * @param args in the first arg the file name could be inserted. If empty take the default one.
     * @throws IOException Error during process the input file.
     */
    public static void main(String[] args) throws IOException {
    	
    	startUpProcedure(args);
    	
    	Graph<String, DefaultWeightedEdge> createdGraph = null;
    	
    	// Create the graph.
		try {
			createdGraph = GKAGraphUtils.readGraphFromFile(filename);
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(
					"Error during read file. Could not read file!");
		} catch (IOException e) {
			throw new RuntimeException(
					"Error during read file. Could not handle file!");
		}
        
        if (createdGraph != null) {
        	System.out.println("Graph : " + createdGraph.toString());
        	
        	// Now we try to find start and destination vertex if vertex are empty.
			if (startVertex == null) {
				startVertex = GKAGraphUtils.setOfVertex.get((int) Math
						.round(Math.random()
								* (GKAGraphUtils.setOfVertex.size() - 1)));
			}
			if (destinationVertex == null) {
				destinationVertex = GKAGraphUtils.setOfVertex.get((int) Math
						.round(Math.random()
								* (GKAGraphUtils.setOfVertex.size() - 1)));
			}
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
        	
        	// Now using the floyd warshall algorithm.
        	FlyodWarshallAlgorithm algorithmFW = new FlyodWarshallAlgorithm(createdGraph);
        	algorithmFW.calculate(startVertex, destinationVertex);
        	
        	// Now using the dijkstra algorithm.
        	DijkstraAlgorithm algorithmD = new DijkstraAlgorithm(createdGraph);
        	algorithmD.calculate(startVertex, destinationVertex);
        	
        } else {
        	// Could not create graph.
        	throw new RuntimeException("Error. Could not create valid error.");
        }
        
    }

	/**
	 * Procedure to handle the application start up.
	 * 
	 * @param args the argument array from start up
	 * @throws NumberFormatException
	 * @throws RuntimeException
	 */
	private static void startUpProcedure(String[] args)
			throws NumberFormatException, RuntimeException {
		// Instruction to use the Jar file.
    	if (args != null && args.length != 0 && args[0].equals("howto")) {
    		System.out.println("Please note the following use instruction : Type arg[0] as ...");
    		System.out.println("0 -> default use of params.");
    		System.out.println("1 -> arg[1] contains filename.");
    		System.out.println("2 -> arg[1] start vertex, arg[2] destination vertex.");
    		System.out.println("3 -> arg[1] filename, arg[2] start vertex, arg[3] destination vertex.");
    		System.exit(0);
    	}
    	
    	
    	// Switch case for use mode
		if (args != null && args.length != 0 && args[0] != "") {
			int stateOfCase = Integer.parseInt(args[0]);
			switch (stateOfCase) {
			case 0:
				filename = "graph_01.graph";
				break;
			case 1:
				if (args[1] != null) {
					filename = args[1];
				} else {
					throw new RuntimeException(
							"Error during start app. File name requested, but no file name available.");
				}
				break;
			case 2:
				filename = "graph_01.graph";
				if (args[1] != null && args[2] != null) {
					startVertex = args[1];
					destinationVertex = args[2];
				} else {
					throw new RuntimeException(
							"Error during start app. Start and destination vertex requested, but no vertex available.");
				}
				break;
			case 3:
				if (args[1] != null) {
					filename = args[1];
				} else {
					throw new RuntimeException(
							"Error during start app. File name requested, but no file name available.");
				}
				if (args[2] != null && args[3] != null) {
					startVertex = args[2];
					destinationVertex = args[3];
				} else {
					throw new RuntimeException(
							"Error during start app. Start and destination vertex requested, but no vertex available.");
				}
			default:
				break;
			}
		}
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
		
		// First check if start and destination vertex are equal.
		if (startVertex.equals(destinationVertex)) {
			System.out.println("Start vertex [" + startVertex
					+ "] equals destination vertex [" + destinationVertex
					+ "].");
			return;
		}

		// Initialize the way to destination list.
		vertexWayList = new ArrayList<String>();

		// Add the start vertex to way to destination list.
		vertexWayList.add(startVertex);

		// Now we make several recursive depth first steps.
		recursiveBreadthFirstStep(createdGraph, startVertex, destinationVertex, "");

		// Display the final way to destination.
		displayFinalWayToVertex();
	}


	/**
	 * Recursive step for the breadth first search.
	 * 
	 * @param createdGraph
	 *            the graph to search
	 * @param workingVertex
	 *            the start vertex
	 * @param destinationVertex
	 *            the destination vertex
	 * @param predecessorVertex
	 * 			  the predecessor vertex
	 */
	private static void recursiveBreadthFirstStep(
			Graph<String, DefaultWeightedEdge> createdGraph,
			String workingVertex, String destinationVertex, String predecessorVertex) {
		
		// Calculate the neighbor list for the actual vertex. Keep in mind that the predecessor vertex is also contained in this list.
		List<String> neighborList = Graphs.neighborListOf(createdGraph, workingVertex);
		
		// Check if the neighbor list only contains the predecessor as neighbor. If the case happens we could skip the processing of this vertex.
		if (neighborList == null || neighborList.size() == 0 || (neighborList.size() == 1 && neighborList.contains(predecessorVertex))) {
			System.out.println("Vertex [" + workingVertex + "] has no neighbours exept the pedecessor. It's time for the recursive step.");
			return;
		}
		//showNeighborListForVertex(startVertex, neighborList);
		
		// Now check in the breadth if the destination vertex is contained in the neighbor list. We check vertex after vertex.
		for (String vertex : neighborList) {
			// If the actual vertex is already contained in the way to destination list we skip the processing of this vertex.
			if (vertexWayList.contains(vertex)) {
				break;
			}
			// First we add the actual vertex to the way to destination.
			vertexWayList.add(vertex);
			// Now we display the actual state of the way to destination.
			displayFinalWayToVertex();
			// If the actual vertex is the destination vertex we can finish the search.
			if (vertex.equals(destinationVertex)) {
				System.out.println("Vertex found.");
				// Set the breadth first finish marker to 'TRUE'.
				breadthFirstFinish  = true;
				return;
			} else {
				// If the actual vertex is not the destination vertex we will remove it from the way to destination.
				removeLastVertexFromWayToVertex(vertex);
			}
		}
		
		// After the complete breadth search we start to process the vertex for subsequent search.
		for (String vertex : neighborList) {
			// If vertex is the predecessor we will skip it.
			if (vertex.equals(predecessorVertex)) {
				System.out.println("Skip vertex [" + vertex + "]. It is the predecessor [" + workingVertex + "]");
			} else {
				// If the actual vertex is already contained in the way to destination list we will have a circle. So we skip the processing of this vertex.
				if (vertexWayList.contains(vertex)) {
					break;
				}
				//System.out.println("Next vertex in List is [" + vertex + "].");
				// Add the actual vertex to the vertex way to destination list.
				vertexWayList.add(vertex);
				// Start the recursive process for this vertex.
				recursiveBreadthFirstStep(createdGraph, vertex, destinationVertex, workingVertex);
				// If we found the destination vertex during the recursive process we can skip more processing.
				if (breadthFirstFinish) {
					return;
				} else {
					// If we didn't find the destination vertex we can remove this vertex from way to destination.
					removeLastVertexFromWayToVertex(vertex);
				}
			}
		}
	}

	/**
	 * This method removes the last element of the the given vertex from the vertex way to destination list.
	 * 
	 * @param vertex the last element of this vertex shall be deleted.
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
		
		// First check if start and destination vertex are equal.
		if (startVertex.equals(destinationVertex)) {
			System.out.println("Start vertex [" + startVertex + "] equals destination vertex [" + destinationVertex + "].");
			return;
		}
		
		// Initialize the way to destination list.
		vertexWayList = new ArrayList<String>();
		
		// Add the start vertex to way to destination list.
		vertexWayList.add(startVertex);
		
		// Now we make several recursive depth first steps.
		recursiveDepthFirstStep(createdGraph, startVertex, destinationVertex, "");

		// Display the final way to destination.
		displayFinalWayToVertex();
	}

	/**
	 * Recursive steps for depth first search.
	 * 
	 * @param createdGraph
	 *            the graph to search
	 * @param workingVertex
	 *            the start vertex
	 * @param destinationVertex
	 *            the destination vertex
	 * @param predecessorVertex
	 * 			  the predecessor vertex
	 */
	private static void recursiveDepthFirstStep(
			Graph<String, DefaultWeightedEdge> createdGraph,
			String workingVertex, String destinationVertex, String predecessorVertex) {
		// Display the actual state of the way to destination list.
		displayFinalWayToVertex();
		
		// Calculate the neighbor list for the actual vertex. Keep in mind that the predecessor vertex is also contained in this list.
		List<String> neighborList = Graphs.neighborListOf(createdGraph, workingVertex);
		
		// If neighbor list is empty we skip the work flow for this vertex. Keep in mind that this could maybe never happen because the predecessor is always contained in the neighbor list.
		if (neighborList == null || neighborList.size() == 0) {
			return;
		}
		//showNeighborListForVertex(startVertex, neighborList);
		
		Collections.shuffle(neighborList);
		
		// Now keep on working for all vertex in the list.
		for (String vertex : neighborList) {
			// If destination vertex still found skip the work flow and return to caller. We should get back to primary recursive start.
			if (depthFirstFinish) {
				return;
			}
			
			// If the actual vertex is the predecessor vertex we skip this vertex.
			if (vertex.equals(predecessorVertex)) {
				;
			} else {
				// If the actual vertex is already contained in the way to destination list we will have a circle. So we skip the processing of this vertex.
				if (vertexWayList.contains(vertex)) {
					break;
				}
				//System.out.println("Vertex on stack [" + vertex + "].");
				
				// If the actual vertex is the destination vertex we have finished the search and can return to primary recursive start.
				if (vertex.equals(destinationVertex)) {
					System.out.println("Vertex found.");
					// Set the depth first finish mark to 'TRUE'. This shows that the search is finished.
					depthFirstFinish = true;
				}
				
				// Add the actual vertex to the way to destination list.
				vertexWayList.add(vertex);
				
				// If search is finished we cancel all former processes.
				if (depthFirstFinish) {
					return;
				}
				
				// The next recursive step for the actual vertex. We go deeper into the graph on this vertex.
				recursiveDepthFirstStep(createdGraph, vertex,
						destinationVertex, workingVertex);
				
				// If the search is not finished for this vertex we will remove it from way to destination list because the vertex is not part of the way to destination.
				if (!depthFirstFinish) {
					removeLastVertexFromWayToVertex(vertex);
				}
			}
		}
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