/**
 * 
 */
package gka;

/**
 * This class contains the data from the input file
 * 
 * @author hoelschers
 *
 */
public class BaseSourceEdge {
	
	private Vertex vertexFrom;
	private Vertex vertexTo;
	private Long edgeWeight;
	/**
	 * @return the vertexFrom
	 */
	public Vertex getVertexFrom() {
		return vertexFrom;
	}
	/**
	 * @param vertexFrom the vertexFrom to set
	 */
	public void setVertexFrom(Vertex vertexFrom) {
		this.vertexFrom = vertexFrom;
	}
	/**
	 * @return the vertexTo
	 */
	public Vertex getVertexTo() {
		return vertexTo;
	}
	/**
	 * @param vertexTo the vertexTo to set
	 */
	public void setVertexTo(Vertex vertexTo) {
		this.vertexTo = vertexTo;
	}
	/**
	 * @return the edgeWeight
	 */
	public Long getEdgeWeight() {
		return edgeWeight;
	}
	/**
	 * @param edgeWeight the edgeWeight to set
	 */
	public void setEdgeWeight(Long edgeWeight) {
		this.edgeWeight = edgeWeight;
	}

}
