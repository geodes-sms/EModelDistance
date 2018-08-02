package PacmanGame.search;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * This class allows to compute the shortests number of links from an EObject to another,
 * given an ordered list of EObject from an Ecore model. 
 * It implements the Floyd-Warshall all paths shortest path algorithm.
 * It computes the shortest path from any node to any node in O(|nodes|^3) time.
 * @see <a href="https://en.wikipedia.org/wiki/Floyd-Warshall_algorithm">Wikipedia</a>
 * @author Eugene Syriani
 */
public class EcoreShortestPaths {

	private double[][] distances;
	private int[][] next;
	private int numNodes;
	private List<EObject> nodes;
	private IEReferenceNavigator ref_nav;
	
	private final double WEIGHT = 1.0;

	/**
	 * Returns the matrix of the distance from any source node to any target node
	 * @return the matrix where (i,j) is the distance from i to j
	 */
	public double[][] getAllDistances() {
		return distances;
	}
	
	/**
	 *  Returns the matrix of all paths from any source node to any target node.
	 *  i and j indices are in the order provided by the original sequence of nodes.
	 * @return the matrix where (i,j)=k where k is the node that connects i to j  
	 */
	public int[][] getAllPaths() {
		return next;
	}

	/**
	 * Implements the Floyd-Warshall all paths shortest path algorithm.
	 * It computes the shortest path from any node to any node in O(|nodes|^3) time.
	 * @see <a href="https://en.wikipedia.org/wiki/Floyd-Warshall_algorithm">Wikipedia</a>
	 * @param nodes sequence of nodes in the order they will be indexed in the resulting matrix
	 * @param ref_nav interface to access the neighbors of each node
	 */
	public EcoreShortestPaths(List<EObject> nodes, IEReferenceNavigator ref_nav)
	{
		this.nodes = nodes;
		numNodes = nodes.size();
		distances = new double[numNodes][numNodes];
		next = new int[numNodes][numNodes];
		this.ref_nav = ref_nav;
		init();
	}
	
	/**
	 * Initializes the data structures
	 */
	private void init()
	{
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
            	next[i][j] = -1;								// null
                if (i == j) {
                	distances[i][j] = 0;						// self-loop
                } else {
                	distances[i][j] = Double.POSITIVE_INFINITY;	// infinity
                }
            }
        }
    }
	
	/**
	 * Computes all shortest distances from any node to any node.
	 * All edges have a weight of 1.
	 */
	public void computeDistances()
	{
		for (int i = 0; i < numNodes; i++) {
			EObject node = nodes.get(i);
			setWeight(i, ref_nav.getNeighbors(node));
		}
		double min;
		for (int i = 0; i < numNodes; i++) {
			for (int j = 0; j < numNodes; j++) {
				for (int k = 0; k < numNodes; k++) {
					min = distances[i][k] + distances[k][j];
					if (distances[i][j] > min) {
						distances[i][j] = min;
						next[i][j] = next[i][k];
					}
				}
			}
		}
	}
	
	/**
	 * Returns the distance from source to target.
	 * @param source source node
	 * @param target target node
	 * @return the number of connected objects from source to target, excluding source
	 */
	public double getDistance(EObject source, EObject target)
	{
		return getDistance(nodes.indexOf(source), nodes.indexOf(target));
	}
	
	/**
	 * Returns the distance from source to target.
	 * @param source index of source node
	 * @param target index of target node
	 * @return the number of connected objects from source to target, excluding source
	 */
	public double getDistance(int source, int target)
	{
		return distances[source][target];
	}
	
	/**
	 * Returns the path from source to target. To navigate through path, 
	 * you need to know which attribute encodes the relation to the next 
	 * node in the list.
	 * @param source source node
	 * @param target target node
	 * @return sequence of all nodes to follow
	 */
	public List<EObject> getPath(EObject source, EObject target)
	{
		return getPath(nodes.indexOf(source), nodes.indexOf(target));
	}
	
	/**
	 * Returns the path from source to target. To navigate through path, 
	 * you need to know which attribute encodes the relation to the next 
	 * node in the list.
	 * @param source index of source node
	 * @param target index of target node
	 * @return sequence of all nodes to follow
	 */
	public List<EObject> getPath(int source, int target)
	{
		List<EObject> path = new ArrayList<EObject>();
		if (next[source][target] != -1) {
			while (source != target) {
				path.add(nodes.get(source));
				source = next[source][target];
			}
			path.add(nodes.get(source));	// add the target
		}
		return path;
	}

	/**
	 * Sets the weight and corresponding next node for 1 or 0..1 relations
	 * @param source index of source node
	 * @param target index of target node
	 */
	private void setWeight(int source, EObject target) {
		if (target != null) {
			int j = nodes.indexOf(target);
			distances[source][j] = WEIGHT;
			next[source][j] = j;
		}
	}

	/**
	 * Sets the weight and corresponding next node for n..* relations, n &gt;= 0
	 * @param source index of source node
	 * @param target index of target node
	 */
	private void setWeight(int source, List<EObject> target) {
		for (EObject n : target) {
			setWeight(source, n);
		}
	}

}