package gka;


import java.util.ArrayList;
import java.util.List;

// class used for internal representation of network
class NodeType<V, E>
{
    /**
	 * 
	 */
	private EdmondsKarpFlowAlgorithm<V, E> NodeType;
	V prototype; // corresponding node in the original network
    List<EdgeType> outgoingEdges = new ArrayList<EdgeType>(); // list of outgoing arcs
                                                   // in the residual
                                                   // network
    boolean visited; // this mark is used during BFS to mark visited nodes
    EdgeType lastEdge; // last arc in the shortest path
    double flowAmount; // amount of flow, we are able to push here

    NodeType(EdmondsKarpFlowAlgorithm<V, E> edmondsKarpMaximumFlow, V prototype) {
        NodeType = edmondsKarpMaximumFlow;
		this.prototype = prototype;
    }
}