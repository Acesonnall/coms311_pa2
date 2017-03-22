import java.util.ArrayList;

/**
 * The GraphProcessor reads a graph stored in a file, using a Strongly Connected Components (SCC)
 * algorithm to determine the following:
 * <p>
 * - Out degree of a vertex
 * - Whether two vertices are in the same SCC
 * - Number of SCC's of the graph
 * - Size of the largest component
 * - Given a vertex v, find all vertices that belong to the same SCC as v
 * - Find shortest (BFS) path from a vertex v to u.
 *
 * @author nkarasch
 */
public class GraphProcessor {
    /**
     * @param graphData The absolute path of a file that stores a directed graph
     */
    public GraphProcessor(String graphData) {
        // TODO
    }

    /**
     * @param v Represents a vertex in the graph
     * @return the out degree of v
     */
    public int outDegree(String v) {
        // TODO
        return 0;
    }

    /**
     * @param u Represents a vertex in the graph
     * @param v Represents a vertex in the graph
     * @return 'true' if u and v belong to the same SCC; otherwise returns 'false'
     */
    public boolean sameComponent(String u, String v) {
        // TODO
        return false;
    }

    /**
     * @param v Represents a vertex in the graph
     * @return All the vertices that belong to the same Strongly Connected Component of v (including v)
     */
    public ArrayList<String> componentVertices(String v) {
        // TODO
        return null;
    }

    /**
     * @return The size of the largest component
     */
    public int largestComponent() {
        // TODO
        return 0;
    }

    /**
     * @return The number of Strongly Connected Components
     */
    public int numComponents() {
        // TODO
        return 0;
    }

    /**
     * This method returns an array list of strings that represents the BFS path from u to v.
     * The first vertex in the path must be u and the last vertex must be v. If there is no
     * path from u to v, then this method returns an empty list.
     *
     * @param u Represents a vertex in the graph
     * @param v Represents a vertex in the graph
     * @return The BFS path from u to v.
     */
    public ArrayList<String> bfsPath(String u, String v) {
        // TODO
        return null;
    }
}
