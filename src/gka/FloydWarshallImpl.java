/**
 * 
 */
package gka;

import java.util.HashMap;

import org.jgrapht.Graph;

/**
 * @author hoelschers
 * @param <V>
 * @param <E>
 *
 */
public class FloydWarshallImpl<V, E> {
	//~ Instance fields --------------------------------------------------------

    int nextIndex = 0;
    HashMap<V, Integer> indices;

    double [][] d;

    double diameter;

    //~ Constructors -----------------------------------------------------------

    /**
     * Constructs the shortest path array for the given graph.
     *
     * @param g input graph
     */
    public FloydWarshallImpl(Graph<V, E> g)
    {
        int sz = g.vertexSet().size();
        d = new double[sz][sz];
        indices = new HashMap<V, Integer>();

        //Initialise distance to infinity, or the neighbours weight, or 0 if
        //same
        for (V v1 : g.vertexSet()) {
            for (V v2 : g.vertexSet()) {
                if (v1 == v2) {
                    d[index(v1)][index(v2)] = 0;
                } else {
                    E e = g.getEdge(v1, v2);

                    if (e == null) {
                        d[index(v1)][index(v2)] = Double.POSITIVE_INFINITY;
                    } else {
                        d[index(v1)][index(v2)] = g.getEdgeWeight(e);
                    }
                }
            }
        }

        //now iterate k times
        for (int k = 0; k < sz; k++) {
            for (V v1 : g.vertexSet()) {
                for (V v2 : g.vertexSet()) {
                    d[index(v1)][index(v2)] =
                        Math.min(
                            d[index(v1)][index(v2)],
                            d[index(v1)][k] + d[k][index(v2)]);
                    diameter = Math.max(diameter, d[index(v1)][index(v2)]);
                }
            }
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Retrieves the shortest distance between two vertices.
     *
     * @param v1 first vertex
     * @param v2 second vertex
     *
     * @return distance, or positive infinity if no path
     */
    public double shortestDistance(V v1, V v2)
    {
        return d[index(v1)][index(v2)];
    }

    /**
     * @return diameter computed for the graph
     */
    public double getDiameter()
    {
        return diameter;
    }

    private int index(V vertex)
    {
        Integer index = indices.get(vertex);
        if (index == null) {
            indices.put(vertex, nextIndex);
            index = nextIndex++;
        }
        return index;
    }
}
