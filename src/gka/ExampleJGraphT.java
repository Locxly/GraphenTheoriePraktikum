package gka;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


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

	//~ Constructors ----------------------------------------------------------

    private ExampleJGraphT()
    {
    } // ensure non-instantiability.

    //~ Methods ---------------------------------------------------------------

    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main(String [] args)
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
        
        // Tree graph
        DirectedGraph<String, DefaultEdge> treeGraph = createTreeGraph();
        
        System.out.println(treeGraph.toString());
        
        List<String> predNode3 = Graphs.neighborListOf(treeGraph, "Node_3");
        
        System.out.println(predNode3.toString());
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