/**
 * 
 */
package gka;

import java.util.*;

import org.jgrapht.*;

/**
 * An implementation of the Edmonds Karp Algorithm based on the jGraphT
 * algorithm.
 * 
 * @author hoelschers
 * 
 */
public final class EdmondsKarpFlowAlgorithm<V, E> {

	/**
	 * Default tolerance.
	 */
	public static final double DEFAULT_EPSILON = 0.000000001;

	/** The net work. */
	private DirectedGraph<V, E> graph; // our network

	private double epsilon; // tolerance (DEFAULT_EPSILON or user-defined)
	/** The source. */
	private int currentSource;
	/** The sink. */
	private int currentSink;
	/** Calculated maximum flow. */
	private Map<E, Double> maximumFlow;
	/** Maximum flow value. */
	private Double maximumFlowValue;
	/** Number of vertexes. */
	private int numNodes;
	/** Mapping of vertex to internal representation. */
	private Map<V, Integer> indexer;
	/** Internal representation of the network. */
	private List<NodeType<V, E>> nodes;

	/**
	 * The constructor which initializes the algorithm.
	 * 
	 * @param graph
	 *            the graph.
	 */
	public EdmondsKarpFlowAlgorithm(DirectedGraph<V, E> graph) {
		if (graph == null) {
			throw new NullPointerException("Graph is empty.");
		}

		this.graph = graph;
		this.epsilon = DEFAULT_EPSILON;

		currentSource = -1;
		currentSink = -1;
		maximumFlow = null;
		maximumFlowValue = null;

		// We build the network internally from the given graph.
		buildInternalNetwork();
	}

	/**
	 * Build the internal network from the given graph.
	 */
	private void buildInternalNetwork() {
		// Get number of nodes.
		numNodes = graph.vertexSet().size();
		nodes = new ArrayList<NodeType<V, E>>();
		Iterator<V> it = graph.vertexSet().iterator();
		indexer = new HashMap<V, Integer>();

		// Create for every vertex a new node.
		for (int i = 0; i < numNodes; i++) {
			V currentNode = it.next();
			nodes.add(new NodeType<V, E>(this, currentNode));
			// Add the index of the new node to list
			indexer.put(currentNode, i);
		}

		// Now add the edges for the nodes.
		// Therefore we also take a new object
		for (int i = 0; i < numNodes; i++) {
			// Take the original vertex.
			V we = nodes.get(i).prototype;
			// Add all outgoing edges.
			// Incoming edges won't be used.
			for (E e : graph.outgoingEdgesOf(we)) {
				// Get the vertex for the target.
				V he = graph.getEdgeTarget(e);
				// Get the index of the target.
				int j = indexer.get(he);
				// Create a new own edge type for this direction.
				EdgeType<V, E> e1 = new EdgeType<V, E>(this, i, j,
						graph.getEdgeWeight(e), e);
				// Create a new own edge type for reverse direction.
				EdgeType<V, E> e2 = new EdgeType<V, E>(this, j, i, 0.0, null);
				e1.reversed = e2;
				e2.reversed = e1;
				// Add edges to outgoing arcs.
				nodes.get(i).outgoingEdges.add(e1);
				nodes.get(j).outgoingEdges.add(e2);
			}
		}
	}

	/**
	 * Sets source and sink and calculates the maximum flow.
	 * 
	 * @param source
	 *            source vertex
	 * @param sink
	 *            sink vertex
	 */
	public void calculateMaximumFlow(V source, V sink) {
		// Check if we can work with source and sink
		if (!graph.containsVertex(source)) {
			throw new IllegalArgumentException("Source not in the network.");
		}
		if (!graph.containsVertex(sink)) {
			throw new IllegalArgumentException("Sink not in the network.");
		}

		if (source.equals(sink)) {
			throw new IllegalArgumentException("Source is equal to sink.");
		}

		// currentSource = indexer.get(source);
		// currentSink = indexer.get(sink);

		// Initialize the current flow of every edge in outgoing edge list
		for (int i = 0; i < numNodes; i++) {
			for (EdgeType<V, E> currentEdge : nodes.get(i).outgoingEdges) {
				currentEdge.flow = 0.0;
			}
		}

		// The maximum flow is currently 0 too.
		maximumFlowValue = 0.0;

		for (;;) {
			// Use the breadth first search.
			breadthFirstSearch(indexer.get(source));
			// If sink is not visited after this turn we break and add the
			// current maximum flow the hash map. A not visited sink is the
			// return criteria.
			if (!nodes.get(indexer.get(sink)).visited) {
				// Add flow to hash map
				maximumFlow = new HashMap<E, Double>();
				for (int i = 0; i < numNodes; i++) {
					for (EdgeType<V, E> currentEdge : nodes.get(i).outgoingEdges) {
						if (currentEdge.prototype != null) {
							maximumFlow.put(currentEdge.prototype, currentEdge.flow);
						}
					}
				}
				return;
			}
			increaseFlow(indexer.get(sink));
		}
	}

	/**
	 * Use the breadth first search to discover the maximum flow from source.
	 * 
	 * @param currentSource
	 *            the index of the source.
	 */
	private void breadthFirstSearch(int currentSource) {
		for (int i = 0; i < numNodes; i++) {
			nodes.get(i).visited = false;
		}
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.offer(currentSource);
		nodes.get(currentSource).visited = true;
		nodes.get(currentSource).flowAmount = Double.POSITIVE_INFINITY;
		while (queue.size() != 0) {
			int currentNode = queue.poll();
			for (EdgeType<V, E> currentEdge : nodes.get(currentNode).outgoingEdges) {
				if (currentEdge.flow < currentEdge.capacity) {
					if (!nodes.get(currentEdge.head).visited) {
						nodes.get(currentEdge.head).visited = true;
						nodes.get(currentEdge.head).flowAmount = Math.min(
								nodes.get(currentNode).flowAmount,
								currentEdge.capacity - currentEdge.flow);
						nodes.get(currentEdge.head).lastEdge = currentEdge;
						queue.add(currentEdge.head);
					}
				}
			}
		}
	}

	/**
	 * @param currentSink
	 */
	private void increaseFlow(int currentSink) {
		double deltaFlow = nodes.get(currentSink).flowAmount;
		maximumFlowValue += deltaFlow;
		int currentNode = currentSink;
		while (currentNode != currentSource) {
			nodes.get(currentNode).lastEdge.flow += deltaFlow;
			nodes.get(currentNode).lastEdge.reversed.flow -= deltaFlow;
			currentNode = nodes.get(currentNode).lastEdge.tail;
		}
	}

	/**
	 * Returns maximum flow value, that was calculated during last <tt>
	 * calculateMaximumFlow</tt> call, or <tt>null</tt>, if there was no <tt>
	 * calculateMaximumFlow</tt> calls.
	 * 
	 * @return maximum flow value
	 */
	public Double getMaximumFlowValue() {
		return maximumFlowValue;
	}

	/**
	 * Returns maximum flow, that was calculated during last <tt>
	 * calculateMaximumFlow</tt> call, or <tt>null</tt>, if there was no <tt>
	 * calculateMaximumFlow</tt> calls.
	 * 
	 * @return mapping from edges to doubles - flow values
	 */
	public Map<E, Double> getMaximumFlow() {
		if (maximumFlow == null) {
			return null;
		}
		return Collections.unmodifiableMap(maximumFlow);
	}

}
