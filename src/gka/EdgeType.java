package gka;

/**
 * This class represents an edge. We decided to build a new class for edges
 * because this way we can mark the capacity and flow of each edge. Also an edge
 * has a reversed edge.
 * 
 * @author hoelschers, dreierm
 * 
 * @param <V> Type of vertex
 * @param <E> Type of edge
 */
public class EdgeType<V, E> {
	
	/** Index of the tail node ('from'). */
	private int tail;
	
    /** Index of the head node ('to'). */
    private int head;
    
    /** The capacity of the node => 0. */
    private double capacity;
    
    /** The flow of the edge <=> 0. */
    private double flow;
    
	/**
	 * The reversed edge for the original edge. We need it for incoming edges
	 * which also can have an effect on the flow.
	 */
    private EdgeType<V, E> reversed; 
    
	/** The original edge from the graph. Is null in case of a reversed edge. */
    private E prototype; 

    /**
     * The constructor to set the base data.
     * 
     * @param tail
     * @param head
     * @param capacity
     * @param prototype
     */
    public EdgeType(int tail, int head, double capacity, E prototype) {
    	this.tail = tail;
    	this.head = head;
    	this.capacity = capacity;
    	this.prototype = prototype;
    }

    public EdgeType<V, E> getReversed() {
		return reversed;
	}

	public void setReversed(EdgeType<V, E> reversed) {
		this.reversed = reversed;
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