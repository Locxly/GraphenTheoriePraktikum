package gka;


import java.util.ArrayList;
import java.util.List;

// class used for internal representation of network
class NodeType<V, E>
{
    /**
	 * 
	 */
	private V prototype; // corresponding node in the original network
    private List<EdgeType> outgoingEdges = new ArrayList<EdgeType>(); // list of outgoing arcs
                                                   // in the residual
                                                   // network
    private boolean visited; // this mark is used during BFS to mark visited nodes
    private EdgeType lastEdge; // last arc in the shortest path
    private double flowAmount; // amount of flow, we are able to push here

    NodeType(V prototype) {
		this.prototype = prototype;
    }

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