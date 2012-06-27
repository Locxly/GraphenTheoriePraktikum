package gka;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Hierholzer
{
	private UndirectedGraph<String, DefaultWeightedEdge> graph;
	private ArrayList<String> vertexList;
	private int steps;
	private String start;
	private ArrayList<String> eulertour;
	private HashMap<String, Integer> degrees;
	private HashMap<String, List<String>> neighbours;
	private int euler, edges;

	public Hierholzer(UndirectedGraph<String, DefaultWeightedEdge> graph,
			String start)
	{
		steps = 0;
		euler = 0;
		this.graph = graph;
		vertexList = new ArrayList<String>(graph.vertexSet());
		edges = graph.edgeSet().size();
		steps++;
		this.start = start;
		eulertour = new ArrayList<String>();
		eulertour.add(start);

		degrees = new HashMap<String, Integer>();
		neighbours = new HashMap<String, List<String>>();

		List<String> neighbour;
		for (String v : vertexList)
		{
			neighbour = Graphs.neighborListOf(graph, v);
			steps++;
			neighbours.put(v, neighbour);
			degrees.put(v, neighbour.size());
			if (neighbour.size()%2 != 0)
			{
				euler++;
			}
		}

		if (euler != 0)
		{
			System.err.println("It's not possible to find a Eulertour");

		}
		// else if(euler>0)
		// {
		// System.err.println("It's not possible to find a Eulertour. Now searching for a Eulerpfad");
		// eulerpfad(start);
		// }

		else
		{
			startAlgo(start, 1);
		}
	}

	private List<String> eulertour(String vertex, String last, String start)
	{
		List<String> tour = new ArrayList<String>();
		tour.add(vertex);
		List<String> nList = neighbours.get(vertex);
		for (int i = 0; i < nList.size(); i++)
		{
			if (nList.get(i).equals(last))
			{
				nList.remove(i);
				degrees.put(vertex, degrees.get(vertex) - 1);
				break;
			}
		}
		
		if(vertex.equals(start))
		{
			return tour;
		}

		for(int neighbour=0; neighbour<nList.size(); neighbour++)
		{
			if (degrees.get(nList.get(neighbour)) > 1 
					|| (degrees.get(nList.get(neighbour)) == 1 && nList.get(neighbour).equals(start)))
			{
				String nVertex = nList.get(neighbour);
				nList.remove(neighbour);
				degrees.put(vertex, degrees.get(vertex) - 1);
				tour.addAll(eulertour(nVertex, vertex, start));
				break;
			} 
			else
			{
				neighbour++;
			}
		}
		
		return tour;

	}

	private void startAlgo(String tourStart, int position)
	{
		List<String> nList = neighbours.get(tourStart);
		for(int neighbour=0; neighbour<nList.size(); neighbour++)
		{
			String nVertex = nList.get(neighbour);
			if (degrees.get(nVertex) > 1)
			{
				nList.remove(neighbour);
				degrees.put(tourStart, degrees.get(tourStart) - 1);
				eulertour.addAll(position, eulertour(nVertex, tourStart, tourStart));
				break;
			} 
			else
			{
				neighbour++;
			}
		}
		if(eulertour.size() == edges+1)
		{
			System.out.println("Eulertour: "+eulertour.toString());
		}
		else
		{
			String v;
			for (int i=0; i<eulertour.size(); i++)
			{
				v = eulertour.get(i);
				if (degrees.get(v) > 1)
				{
					startAlgo(v, i+1);
				}
			}
		}

	}

}
