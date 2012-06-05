/**
 * 
 */
package gka;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.GraphPathImpl;

/**
 * This class contains the implementation of the dijkstra algorithm.
 * 
 * @author dreierm
 * 
 */
public class DijkstraAlgorithm {

	/**
	 * The graph to work with.
	 */
	private Graph<String, DefaultWeightedEdge> graph;
	private ArrayList<String> vertexList;
	private int steps;
	private double [][] table;
	private HashMap<String, Integer> vertexID;

	/**
	 * The constructor.
	 * 
	 * @param graph
	 */
	public DijkstraAlgorithm(Graph<String, DefaultWeightedEdge> graph, String startVertex) {
		this.graph = graph;
		vertexID = new HashMap<String, Integer>();
		vertexList = new ArrayList<String>(graph.vertexSet());
		steps = 0;
		table = new double [vertexList.size()][4];
		for(int i = 0; i<vertexList.size(); i++)
		{
			String s= vertexList.get(i);
			vertexID.put(s, i);
			if(vertexList.get(i).equals(startVertex))
			{
				table[i][0]=0.0;
				table[i][1]=i;
			}
			else
			{
				table[i][0]=Double.POSITIVE_INFINITY;
				table[i][1]=Double.NaN;
			}
			table[i][2]=0;
			
		}
		System.out.println("dijkstra gestartet");
		System.out.println(table.toString());
		calculate();

	}

	/**
	 * This method calculates the way between start and destination vertex using
	 * the dijkstra algorithm.
	 * 
	 * @param startVertex: Vertex
	 * @param destinationVertex: Vertex
	 */
	private void calculate() {

		String v;
		while (!vertexList.isEmpty()) {
			v = getMin(vertexList);
			vertexList.remove(v);
			table[vertexID.get(v)][2]=1;
			List<String> neighbours;
			if (graph instanceof DefaultDirectedWeightedGraph<?,?>) {
				neighbours= Graphs.successorListOf((DirectedGraph<String,DefaultWeightedEdge>) graph, v); 
			}else{
				neighbours= Graphs.neighborListOf(graph, v);
			}
			
			for (int i=0; i<neighbours.size(); i++) {
				int neighbour=vertexID.get(neighbours.get(i));  
				if (table[neighbour][2]!=1) {
					double dist = table[vertexID.get(v)][0] + graph.getEdgeWeight(graph.getEdge(v, neighbours.get(i)));
					if (table[neighbour][0] > dist) {
						steps++;
						table[neighbour][0]=dist;
						table[neighbour][1]=vertexID.get(v);
					}
				}
			}
		}
		System.out.println(show());
		System.out.println("we used "+steps+" steps.");
	}
	
	private String getMin(ArrayList<String> vertexList2) {
		double min = Double.POSITIVE_INFINITY;
		String minS = vertexList.get(0);
		for(String v:vertexList)
		{
			double dist = table[vertexID.get(v)][0];
			if(dist<min)
			{
				min = dist;
				minS = v;
			}
		}
		return minS;
	}
	
	
	private String show()
	{
		String s = "vertexID-Liste\n";
		Object[] keys = vertexID.keySet().toArray();
		for(Object k:keys)
		{
			s += k+" "+vertexID.get(k)+" \n";
		}
		
		s += "Dijkstra-Tabelle\n";
				
		for(int i=0; i<table.length; i++)
		{
			
			s += i +" "+table[i][0]+" "+table[i][1]+" "+table[i][2]+" \n";
		}
		
		return s;
	}

//	public void shortestWayTo(Vertex destinationVertex) {
//		if (startVertex.equals(destinationVertex)) {
//			System.out
//					.println("StartVertex and destinationVertex are the same.");
//			return;
//		}
//		for (Vertex v : vertexList) {
//			if (v.equals(startVertex)) {
//				v.setDist(0);
//			} else {
//				v.setDist(Double.POSITIVE_INFINITY);
//			}
//			v.setPred(null);
//			v.changeStatus(false);
//		}
//		
//		ArrayList<DefaultWeightedEdge> edgeList = new ArrayList<DefaultWeightedEdge>();
//
//		Vertex v = startVertex;
//		while (!vertexList.isEmpty()) {
//			v = vertexList.poll();
//			List<Vertex> neighbours = Graphs.neighborListOf(graph, v);
//			for (int i=0; i<neighbours.size(); i++) {
//				Vertex n=neighbours.get(i);  //TODO bearbeiten wir den richtigen v oder mŸssen wir uns den erst aus der vertexlist holen?
//				if (!n.isMarked()) {
//					double dist = v.getDist() + graph.getEdgeWeight(graph.getEdge(v, n));
//					if (n.getDist() > dist) {
//						steps++;
//						n.setDist(dist);
//						n.setPred(v);
//					}
//				}
//			}
//			v.changeStatus(true);
//			if(graph.containsVertex(v))
//			{
//				System.out.println("graph "+v+" drin.");
//			}
//			else System.out.println("nicht drin");
//			DefaultWeightedEdge e = graph.getEdge(v.getPred(), v);
//			edgeList.add(e);
//			weight += graph.getEdgeWeight(e);
//			if(v.equals(destinationVertex))
//				break;
//		}
//		if(!v.equals(destinationVertex))
//		{
//			System.out.println("No path between start and destination vertex.");
//			return;
//		}
//		path = new GraphPathImpl<Vertex, DefaultWeightedEdge>(graph, startVertex, destinationVertex, edgeList, weight);
//		System.out.println(path.toString());
//	}

}
