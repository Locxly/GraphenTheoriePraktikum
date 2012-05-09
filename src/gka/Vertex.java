package gka;


import java.util.HashMap;

public class Vertex implements Comparable<Vertex>{

	
	private String name;
	private double dist;
	private Vertex pred;
	private boolean marked;
	
	public Vertex(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public double getDist()
	{
		return dist;
	}
	
	public Vertex getPred()
	{
		return pred;
	}

	public boolean isMarked()
	{
		return marked;
	}
	
	public void setDist(double value)
	{
		dist = value;
	}
	
	public void setPred(Vertex newP)
	{
		pred = newP;
	}
	
	public void changeStatus(boolean b)
	{
		marked = b;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		if(o==null)
			return false;
		if(o instanceof Vertex)
		{
			Vertex v = (Vertex)o;
			if(name == v.getName())
				return true;
		}
		return false;
		
	}

	public int compareTo(Vertex o) {
		double other = o.getDist();
		if(this.dist > other)
			return 1;
		else if(this.dist < other)
			return -1;
		else
			return 0;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
