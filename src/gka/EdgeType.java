package gka;

// class used for internal representation of network
class EdgeType<V, E>
{
    /**
	 * 
	 */
	private EdmondsKarpFlowAlgorithm<V, E> EdgeType;
	int tail; // "from"
    int head; // "to"
    double capacity; // capacity (can be zero)
    double flow; // current flow (can be negative)
    EdgeType<V, E> reversed; // for each arc in the original network we are to create
                  // reversed arc
    E prototype; // corresponding edge in the original network, can be null,
                 // if it is reversed arc

    EdgeType(
        EdmondsKarpFlowAlgorithm<V, E> edmondsKarpMaximumFlow, int tail,
        int head,
        double capacity,
        E prototype)
    {
        EdgeType = edmondsKarpMaximumFlow;
		this.tail = tail;
        this.head = head;
        this.capacity = capacity;
        this.prototype = prototype;
    }
}