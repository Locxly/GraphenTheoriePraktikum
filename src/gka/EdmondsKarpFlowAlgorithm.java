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

	/** The net work. */
	private DirectedGraph<V, E> graph; // our network

	/** Calculated maximum flow. */
	private Map<E, Double> maximumFlow;
	/** Maximum flow value. */
	private Double maximumFlowValue;
	/** Number of vertexes. */
	private int numNodes;
	/** Mapping of vertex to internal representation. */
	private Map<V, Integer> nodeIdMap;
	/** Internal representation of the network. */
	private List<NodeType<V, E>> nodesList;

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
		nodesList = new ArrayList<NodeType<V, E>>();
		Iterator<V> it = graph.vertexSet().iterator();
		nodeIdMap = new HashMap<V, Integer>();

		// Create for every vertex a new node.
		for (int i = 0; i < numNodes; i++) {
			V currentNode = it.next();
			nodesList.add(new NodeType<V, E>(currentNode));
			// Add the index of the new node to list
			nodeIdMap.put(currentNode, i);
		}

		// Now add the edges for the nodes.
		// Therefore we also take a new object
		for (int i = 0; i < numNodes; i++) {
			// Take the original vertex.
			V we = nodesList.get(i).getPrototype();
			// Add all outgoing edges.
			// Incoming edges won't be used.
			for (E e : graph.outgoingEdgesOf(we)) {
				// Get the vertex for the target.
				V he = graph.getEdgeTarget(e);
				// Get the index of the target.
				int j = nodeIdMap.get(he);
				// Create a new own edge type for this direction.
				EdgeType<V, E> e1 = new EdgeType<V, E>(i, j,
						graph.getEdgeWeight(e), e);
				// Create a new own edge type for reverse direction.
				EdgeType<V, E> e2 = new EdgeType<V, E>(j, i, 0.0, null);
				e1.setReversed(e2);
				e2.setReversed(e1);
				// Add edges to outgoing edges.
				nodesList.get(i).getOutgoingEdges().add(e1);
				nodesList.get(j).getOutgoingEdges().add(e2);
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

		// Initialize the current flow of every edge in outgoing edge list
		for (int i = 0; i < numNodes; i++) {
			for (EdgeType<V, E> currentEdge : nodesList.get(i).getOutgoingEdges()) {
				currentEdge.setFlow(0.0);
			}
		}

		// The maximum flow is currently 0 too.
		maximumFlowValue = 0.0;

		for (int j = 1; j > 0; j++) {
			// Use the breadth first search.
			breadthFirstSearch(nodeIdMap.get(source));
			// If sink is not visited after this turn we break and add the
			// current maximum flow the hash map. A not visited sink is the
			// return criteria.
			if (!nodesList.get(nodeIdMap.get(sink)).isVisited()) {
				System.out.println("------ Finalize and add edges wich are visited during maximum flow search ---- ");
				// Add the maximum flow to hash map
				maximumFlow = new HashMap<E, Double>();
				for (int i = 0; i < numNodes; i++) {
					for (EdgeType<V, E> currentEdge : nodesList.get(i).getOutgoingEdges()) {
						if (currentEdge.getPrototype() != null && currentEdge.getFlow() > 0) {
							// Add every single edge to maximum flow map.
							System.out.println("Add edge from ["
									+ nodesList.get(currentEdge.getTail())
											.getPrototype().toString()
									+ "] to ["
									+ nodesList.get(currentEdge.getHead())
											.getPrototype().toString()
									+ "] with flow [" + currentEdge.getFlow()
									+ "] to list.");
							maximumFlow.put(currentEdge.getPrototype(), currentEdge.getFlow());
						}
					}
				}
				return;
			}
			// Increase the flow of way from sink to source.
			increaseFlow(nodeIdMap.get(sink), nodeIdMap.get(source));
			System.out.println("Done for [" + j +"] iteration.");
		}
	}

	/**
	 * Use the breadth first search to discover the maximum flow from source. We
	 * span the hole network with each maximum flow for each node.
	 * 
	 * @param currentSource
	 *            the index of the source.
	 */
	private void breadthFirstSearch(int currentSource) {
		// Set all nodes to not visited.
		for (int i = 0; i < numNodes; i++) {
			nodesList.get(i).setVisited(false);
		}
		// Build queue for nodes.
		Queue<Integer> queue = new LinkedList<Integer>();
		
		// Add source as base for spanning network.
		queue.offer(currentSource);
		
		// Set the source to visited
		nodesList.get(currentSource).setVisited(true);
		nodesList.get(currentSource).setFlowAmount(Double.POSITIVE_INFINITY);
		
		while (queue.size() != 0) {
			// Take current node from queue.
			int currentNode = queue.poll();
			// Check the possibility to add amount to the flow.
			for (EdgeType<V, E> currentEdge : nodesList.get(currentNode).getOutgoingEdges()) {
				System.out.println("Proove if current edge from ["
						+ nodesList.get(currentEdge.getTail()).getPrototype()
								.toString()
						+ "] to ["
						+ nodesList.get(currentEdge.getHead()).getPrototype()
								.toString() + "] with capacity ["
						+ currentEdge.getCapacity() + "] can get flow ["
						+ currentEdge.getFlow() + "].");
				// If flow fits in capacity we take this edge to maximum flow list
				if (currentEdge.getFlow() < currentEdge.getCapacity()) {
					// But this will only happen if the head node wasn't visited.
					if (!nodesList.get(currentEdge.getHead()).isVisited()) {
						nodesList.get(currentEdge.getHead()).setVisited(true);
						// Set the new maximum flow amount.
						nodesList.get(currentEdge.getHead()).setFlowAmount(
								Math.min(
										nodesList.get(currentNode)
												.getFlowAmount(),
										currentEdge.getCapacity()
												- currentEdge.getFlow()));
						// Add the edge to head's last edge because it's the predessor of the head node.
						nodesList.get(currentEdge.getHead()).setLastEdge(currentEdge);
						// Add head node to queue.
						queue.add(currentEdge.getHead());
					}
				}
			}
		}
	}

	/**
	 * Increase the flow for the found way between source and sink. We'll start
	 * at sink and add flow until found the source.
	 * 
	 * @param currentSink
	 *            the id of the sink
	 * @param currentSource
	 *            the id of the source
	 */
	private void increaseFlow(int currentSink, int currentSource) {
		// The new amount of the current flow.
		double deltaFlow = nodesList.get(currentSink).getFlowAmount();
		// Add the current flow to the complete flow value.
		maximumFlowValue += deltaFlow;
		// Start the increase from the sink.
		int currentNode = currentSink;
		// The increase will take from sink to source. All edges between these nodes will be used.
		while (currentNode != currentSource) {
			double flow = nodesList.get(currentNode).getLastEdge().getFlow() + deltaFlow;
			nodesList.get(currentNode).getLastEdge().setFlow(flow);
			System.out.println("Set the flow form [" + nodesList.get(nodesList.get(currentNode).getLastEdge().getTail()).getPrototype().toString() + "] to [" + nodesList.get(nodesList.get(currentNode).getLastEdge().getHead()).getPrototype().toString() +"] to value [" + flow + "].");
			
			double reversedFlow = nodesList.get(currentNode).getLastEdge().getReversed().getFlow() - deltaFlow;
			nodesList.get(currentNode).getLastEdge().getReversed().setFlow(reversedFlow);

			currentNode = nodesList.get(currentNode).getLastEdge().getTail();

		}
	}

	/**
	 * Returns maximum flow value, that was calculated during last <tt>
	 * calculateMaximumFlow</tt> call, or <tt>null</tt>, if there was no <tt>
	 * calculateMaximumFlow</tt> calls.
	 * 
	 * @return maximum flow value
	 */
	public double getMaximumFlowValue() {
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
