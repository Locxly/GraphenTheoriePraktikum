package gka;

// class used for internal representation of network
class EdgeType<V, E>
{
    /**
	 * 
	 */
	private int tail; // "from"
    private int head; // "to"
    private double capacity; // capacity (can be zero)
    private double flow; // current flow (can be negative)
    private EdgeType<V, E> reversed; // for each arc in the original network we are to create
                  // reversed arc
    E prototype; // corresponding edge in the original network, can be null,
                 // if it is reversed arc

    EdgeType( int tail,
        int head,
        double capacity,
        E prototype)
    {
		this.tail = tail;
        this.head = head;
        this.capacity = capacity;
        this.prototype = prototype;
    }

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getFlow() {
		return flow;
	}

	public void setFlow(double flow) {
		this.flow = flow;
	}

	public int getTail() {
		return tail;
	}

	public int getHead() {
		return head;
	}

	public E getPrototype() {
		return prototype;
	}
    
    
    
    
}