package gka;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a node. We decided to build a new class for nodes
 * because this way we can mark the node as visited and add the outgoing edges
 * of a node.
 * 
 * @author hoelschers, dreierm
 * 
 * @param <V>
 *            Type of vertex
 * @param <E>
 *            Type of edge
 */
class NodeType<V, E> {
    
	/** The vertex from the original graph. */
	private V prototype;
	
    /** The list of all outgoing edges from this node. */
    private List<EdgeType> outgoingEdges = new ArrayList<EdgeType>(); 
    
    /** The flag if the node is visited or not. */
    private boolean visited; 
    
	/** The last edge from which the node will be reached on shortest path. */
    private EdgeType lastEdge; 

    /** The amount of flow we are able to push through this node. */
    private double flowAmount;
    
    /**
     * The constructor.
     * 
     * @param prototype
     */
    public NodeType(V prototype) {
		this.prototype = prototype;
    }

    /** Getter and setter. */
	
    public boolean isVisited() {
		return visited;
	}

	public void setLastEdge(EdgeType lastEdge) {
		this.lastEdge = lastEdge;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public double getFlowAmount() {
		return flowAmount;
	}

	public void setFlowAmount(double flowAmount) {
		this.flowAmount = flowAmount;
	}

	public V getPrototype() {
		return prototype;
	}

	public List<EdgeType> getOutgoingEdges() {
		return outgoingEdges;
	}

	public EdgeType getLastEdge() {
		return lastEdge;
	}
	
	public void addOutgoingEdge(EdgeType e){
		outgoingEdges.add(e);
	}
	
	public void mark(EdgeType last, double posFlow)
	{
		lastEdge = last;
		flowAmount = posFlow;
	}
}