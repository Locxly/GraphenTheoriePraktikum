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
	
	private String vertexFrom;
	private String vertexTo;
	private Long edgeWeight;
	/**
	 * @return the vertexFrom
	 */
	public String getVertexFrom() {
		return vertexFrom;
	}
	/**
	 * @param vertexFrom the vertexFrom to set
	 */
	public void setVertexFrom(String vertexFrom) {
		this.vertexFrom = vertexFrom;
	}
	/**
	 * @return the vertexTo
	 */
	public String getVertexTo() {
		return vertexTo;
	}
	/**
	 * @param vertexTo the vertexTo to set
	 */
	public void setVertexTo(String vertexTo) {
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
