/**
 * 
 */
package gka;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.EdmondsKarpMaximumFlow;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Contains the implementation of a edmonds and karp algorithm.
 * 
 * We need to implement the following code.
 * 
 * [algorithm EdmondsKarp input: C[1..n, 1..n] (Capacity matrix) E[1..n, 1..?]
 * (Neighbour lists) s (Source) t (Sink) output: f (Value of maximum flow) F (A
 * matrix giving a legal flow with the maximum value) f := 0 (Initial flow is
 * zero) F := array(1..n, 1..n) (Residual capacity from u to v is C[u,v] -
 * F[u,v]) forever m, P := BreadthFirstSearch(C, E, s, t, F) if m = 0 break f :=
 * f + m (Backtrack search, and write flow) v := t while v ­ s u := P[v] F[u,v]
 * := F[u,v] + m F[v,u] := F[v,u] - m v := u return (f, F)]
 * 
 * @author hoelschers
 * @param <V>
 * @param <E>
 * 
 */
@SuppressWarnings("hiding")
public class EdmondsKarpAlgorithm<V, DefaultWeightedEdge> {

	// Der Graph zum Bearbeiten.
	private Graph<V, DefaultWeightedEdge> graph;
	// Die Menge der Knoten im Graph.
	private List<V> vertexSet;

	private static boolean breadthFirstFinish = false;

	private List<V> vertexWayList;
	
	private Map<DefaultWeightedEdge, Double> edgeListCapacity;

	/**
	 * The constructor.
	 * 
	 * @param graph
	 * @param vertexSet
	 */
	public EdmondsKarpAlgorithm(Graph<V, DefaultWeightedEdge> graph, List<V> vertexSet) {
		super();
		this.graph = graph;
		this.vertexSet = vertexSet;
	}

	/**
	 * Function to calculate the flow between source and sink after the Edmonds
	 * Karp algorithm.
	 * 
	 * @param source
	 *            the source vertex
	 * @param sink
	 *            the sink vertex
	 */
	public void calculate(V source, V sink) {
		if (graph instanceof DirectedGraph<?, ?>) {
			EdmondsKarpMaximumFlow<V, DefaultWeightedEdge> flow = new EdmondsKarpMaximumFlow<V, DefaultWeightedEdge>(
					(DirectedGraph<V, DefaultWeightedEdge>) graph);
			flow.calculateMaximumFlow(source, sink);

			ownAlgorithm(source, sink, null);

		}

	}

	private void ownAlgorithm(V source, V sink, V predessor) {
		// 1. Get neighbor list of source vertex
		List<V> neighborList = Graphs.neighborListOf(graph, source);
		
		// 2. Find the minimum weight edge
		double capacityCheck = Double.POSITIVE_INFINITY;
		
		for (V vertex : neighborList) {
			if (vertex.equals(predessor)) {
				continue;
			}
			
			graph.getEdge(source, vertex);
			
			// Check if edge already known.
			
			
		}
		
	}

}
