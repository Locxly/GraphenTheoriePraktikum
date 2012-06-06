package gka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class FundF
{
	private DirectedGraph<String, DefaultWeightedEdge> graph;
	private ArrayList<String> vertexList;
	private int steps;
	private HashMap<String, Integer> vertexID;
	private String sink, source;
	private ArrayList<NodeType<String, DefaultWeightedEdge>> nodes;
	private LinkedList<NodeType<String, DefaultWeightedEdge>> markedNodes;
	private double maxFlow;
	private int sinkID, sourceID;

	public FundF(DirectedGraph<String, DefaultWeightedEdge> graph,
			String source, String sink)
	{
		steps = 0;
		this.graph = graph;
		this.sink = sink;
		this.source = source;
		vertexID = new HashMap<String, Integer>();
		vertexList = new ArrayList<String>(graph.vertexSet());
		steps++;
		markedNodes = new LinkedList<NodeType<String, DefaultWeightedEdge>>();
		maxFlow = 0.0;

		initialise();

		sinkID = vertexID.get(sink);
		sourceID = vertexID.get(source);

		inspectAndMark();

		getMaxFlow();
	}

	// 1
	private void initialise()
	{
		nodes = new ArrayList<NodeType<String, DefaultWeightedEdge>>();

		// Create for every vertex a new node.
		for (int i = 0; i < vertexList.size(); i++)
		{
			String currentNode = vertexList.get(i);
			// Add the ID of the new node to hashmap
			vertexID.put(currentNode, i);
			nodes.add(new NodeType<String, DefaultWeightedEdge>(currentNode));
		}

		// Now add the edges for the nodes.
		// Therefore we also take a new object
		for (int i = 0; i < vertexList.size(); i++)
		{
			// Take the original vertex.
			String vertex = nodes.get(i).getPrototype();
			// Add all outgoing edges.
			// Incoming edges won't be used.
			for (DefaultWeightedEdge e : graph.outgoingEdgesOf(vertex))
			{
				steps++;
				// Get the vertex for the target.
				String neighbour = graph.getEdgeTarget(e);
				steps++;
				// Get the index of the target.
				int j = vertexID.get(neighbour);
				// Create a new own edge type for this direction.
				EdgeType<String, DefaultWeightedEdge> e1 = new EdgeType<String, DefaultWeightedEdge>(
						j, i, graph.getEdgeWeight(e), e);
				steps++;
				if (e1.getReversed() == null)
				{
					// Create a new own edge type for reverse direction.
					EdgeType<String, DefaultWeightedEdge> e2 = new EdgeType<String, DefaultWeightedEdge>(
							i, j, 0.0, null);

					e1.setReversed(e2);
					e2.setReversed(e1);

					// Add edge to List of outgoingEdges
					nodes.get(j).addOutgoingEdge(e2);
				}

				// Add edge to List of outgoingEdges
				nodes.get(i).addOutgoingEdge(e1);
			}
			if (vertex.equals(source))
			{
				nodes.get(i).mark(null, Double.POSITIVE_INFINITY);
				markedNodes.add(nodes.get(i));
			}
		}
	}

	// 2
	private void inspectAndMark()
	{
		Random rand = new Random();
		while (!markedNodes.isEmpty())
		{
			int r = rand.nextInt(markedNodes.size());
			NodeType<String, DefaultWeightedEdge> node = markedNodes.remove(r);
			node.setVisited(true);
			for (EdgeType<String, DefaultWeightedEdge> e : node
					.getOutgoingEdges())
			{
				if (e.getCapacity() > e.getFlow()
						&& !(markedNodes.contains(nodes.get(e.getTail())))
						&& !(nodes.get(e.getTail()).isVisited()))
				{
					NodeType<String, DefaultWeightedEdge> neighbour = nodes
							.get(e.getTail());
					neighbour.mark(
							e,
							Math.min(node.getFlowAmount(),
									(e.getCapacity() - e.getFlow())));
					markedNodes.add(neighbour);
				}
			}
			if (markedNodes.contains(nodes.get(sinkID)))
			{
				increaseFlow();
			}
		}

		findMaxFlow();
	}

	// 3
	private void increaseFlow()
	{
		double deltaFlow = nodes.get(sinkID).getFlowAmount();
		int currentID = sinkID;
		// TODO
		while (currentID != sourceID)
		{
			nodes.get(currentID)
					.getLastEdge()
					.setFlow(
							nodes.get(currentID).getLastEdge().getFlow()
									+ deltaFlow);
			nodes.get(currentID)
					.getLastEdge()
					.getReversed()
					.setFlow(
							nodes.get(currentID).getLastEdge().getReversed()
									.getFlow()
									- deltaFlow);
			currentID = nodes.get(currentID).getLastEdge().getHead();
		}

		markedNodes.clear();
		markedNodes.push(nodes.get(sourceID));

		for (int i = 0; i < nodes.size(); i++)
		{
			nodes.get(i).setVisited(false);
		}
	}

	// 4
	private void findMaxFlow()
	{
		for (NodeType<String, DefaultWeightedEdge> n : nodes)
		{
			String vertex = n.getPrototype();
			for (EdgeType<String, DefaultWeightedEdge> e : n.getOutgoingEdges())
			{
				e.getReversed().setFlow(0.0);
				double flow = e.getFlow();
				NodeType<String, DefaultWeightedEdge> lastNode = nodes.get(e.getTail());
				if (n.isVisited() && !(lastNode.isVisited()))
				{
					maxFlow += flow;
					System.out.println("+" + flow + " : "
							+ vertex + "-"
							+ lastNode.getPrototype());
				} else if (!(n.isVisited())
						&& lastNode.isVisited())
				{
					maxFlow -= flow;
					if (flow < 0)
					{
						flow *= -1;
						System.out.println("+" + flow + " : "
								+ vertex + "-"
								+ lastNode.getPrototype());
					} else
					{
						System.out.println("-" + flow + " : "
								+ vertex + "-"
								+ lastNode.getPrototype());
					}
				}
			}

		}

	}

	public void getMaxFlow()
	{
		System.out.println("");
		System.out.println("maximaler Fluss: " + maxFlow);
	}
}
