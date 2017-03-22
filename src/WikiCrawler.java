import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The WikiCrawler class has methods that can be used to crawl Wiki.
 *
 * @author nkarasch
 */
@SuppressWarnings("WeakerAccess")
public class WikiCrawler {
    // The page the crawler starts crawling from
    private String seedUrl;
    // The max number of unique pages (vertices) the crawler will crawl
    private int max;
    // The name of the file that the crawl() method will save to
    private String fileName;

    // The set of unique web links being crawled
    private HashSet<String> vertices;
    // The set of unique edges (link from one page to another) that have been discovered
    private HashSet<Edge> edges;
    // A counter that keeps track of the order that edges are discovered
    private int discoveryCount = 0;

    /**
     * The base url from which all relative urls (wiki pages) are curled.
     */
    public static final String BASE_URL = "https://en.wikipedia.org";

    // The following variables pertain to the "politeness policy"
    // to avoid overloading the wikipedia servers
    private int politenessPolicyCounter = 0;
    private static final int NUM_CURLS_BEFORE_WAITING = 100;
    private static final int SECONDS_TO_WAIT = 3;

    // Set to 'true' if you want some status messages printed during the crawl.
    // Can be used for debugging.
    private static final boolean VERBOSE_OUTPUT = false;

    // Represents a directed edge in the web graph -- a link from one page to another
    private class Edge {
        String from;
        String to;
        int orderDiscovered;

        public Edge(String from, String to, int orderDiscovered) {
            this.from = from;
            this.to = to;
            this.orderDiscovered = orderDiscovered;
        }

        @Override
        public String toString() {
            return from + " " + to;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Edge)) return false;
            if (o == this) return true;
            Edge oEdge = (Edge) o;
            return oEdge.from.equals(from) && oEdge.to.equals(to);
        }

        @Override
        public int hashCode() {
            return from.hashCode() + to.hashCode();
        }
    }

    // Used for sorting an array of Edge objects based on the order the links were discovered
    private class EdgeComparator implements Comparator<Edge> {
        @Override
        public int compare(Edge e1, Edge e2) {
            return e1.orderDiscovered - e2.orderDiscovered;
        }
    }

    /**
     * @param seedUrl  The relative address of the seed url (within Wiki domain)
     * @param max      Represents maximum number of pages to be crawled
     * @param fileName Represents the name of the file the graph will be written to
     */
    public WikiCrawler(String seedUrl, int max, String fileName) {
        this.seedUrl = seedUrl;
        this.max = max;
        this.fileName = fileName;
        int initialSize = (int) Math.ceil(1.5 * max);
        vertices = new HashSet<>(initialSize);
        edges = new HashSet<>(initialSize);
    }

    /**
     * This method gets a string (that represents contents of a .html
     * file) as parameter. This method should return an array list (of Strings) consisting of links from doc.
     * Type of this method is ArrayList<String>. You may assume that the html page is the source
     * (html) code of a wiki page.
     * <p>
     * This method must:
     * - Extract only wiki links. I.e. only links that are of form /wiki/XXXXX.
     * - Only extract links that appear after the first occurrence of the html tag <p> (or <P>).
     * - Should not extract any wiki link that contain the characters “#” or “:”.
     * - The order in which the links in the returned array list must be exactly the same order in
     * which they appear in doc.
     *
     * @param doc Represents the contents of a .html file
     * @return An ArrayList of Strings consisting of links from doc
     */
    public ArrayList<String> extractLinks(String doc) {
        ArrayList<String> arr = new ArrayList<>();
        String content = "";

        // The content to search is everything after the first <p> or <P> tag
        Matcher matcher = Pattern.compile("<[pP]>").matcher(doc);
        if (matcher.find()) {
            content = doc.substring(matcher.start());
        }

        // Get all valid relative urls and add them to the array arr
        matcher = Pattern.compile("<a +href *= *\"(/wiki/[^#:]*?)\"")
                .matcher(content);
        while (matcher.find()) {
            arr.add(matcher.group(1));
        }

        return arr;
    }

    /**
     * This method crawls the wikipedia site using Breadth-First Search, starting at the seed url
     * and constrained by the max number of pages to crawl, which was set by the user in the constructor.
     * The crawler has a "politeness policy" that waits 3 seconds per 100 requests to avoid overloading
     * the server. After crawling, it outputs all the edges in the order they were found to the filename
     * indicated by the user in the constructor.
     */
    public void crawl() {
        System.out.println("Beginning crawl of " + BASE_URL + seedUrl);
        System.out.println("Max number of pages to visit:  " + max);
        System.out.println("Saving to filename:  " + fileName);
        BFS(seedUrl);
        Edge[] edgeArray = hashSetToArray(edges);
        Arrays.sort(edgeArray, new EdgeComparator());
        saveToFile(edgeArray, vertices.size());
    }

    // Converts a HashSet of Edge objects to an Edge array
    private Edge[] hashSetToArray(HashSet<Edge> edgeSet) {
        Edge[] edgeArr = new Edge[edgeSet.size()];
        Iterator<Edge> iterator = edgeSet.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Edge next = iterator.next();
            edgeArr[i++] = next;
        }
        return edgeArr;
    }

    // Performs a Bread-First Search to create a web graph, starting at url 'v'
    private void BFS(String v) {
        LinkedList<String> queue = new LinkedList<>();
        HashSet<String> visited = new HashSet<>((int) Math.ceil(1.5 * max));

        queue.add(v);
        visited.add(v);
        vertices.add(v);
        while (!queue.isEmpty()) {
            String u = queue.pop();
            String urlDoc = curlUrl(u);
            ArrayList<String> links = extractLinks(urlDoc);
            for (String link : links) {
                if (vertices.contains(link)) {
                    // Add edges to previously-visited pages
                    if (!u.equals(link)) {
                        // Prevent self loops
                        edges.add(new Edge(u, link, discoveryCount++));
                    }
                } else if (vertices.size() < max) {
                    // Add edges to not-yet-visited pages if we haven't reached the
                    // max number of vertices yet
                    vertices.add(link);
                    edges.add(new Edge(u, link, discoveryCount++));
                    if (!visited.contains(link)) {
                        queue.add(link);
                        visited.add(link);
                    }
                }
            }
        }
    }

    // Saves an array of Edge objects to the file at fileName.
    // The first line of the file is the number of vertices found.
    private void saveToFile(Edge[] edges, int numVertices) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write(Integer.toString(numVertices));
            bw.write('\n');
            for (Edge edge : edges) {
                bw.write(edge.toString());
                bw.write('\n');
            }
            print("Results saved to " + fileName);
        } catch (IOException e) {
            System.out.println("IOException for file name:  " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                System.out.println("IOException for file name:  " + fileName);
                ex.printStackTrace();
            }
        }
    }

    // Curls the given relative url, using the BASE_URL and adhering to the "politeness policy"
    private String curlUrl(String urlString) {
        // Wait if necessary, or increment the request counter
        if (politenessPolicyCounter > NUM_CURLS_BEFORE_WAITING - 1) {
            try {
                print("Waiting " + SECONDS_TO_WAIT + " seconds, per \"politeness policy.\"");
                Thread.sleep(1000 * SECONDS_TO_WAIT);
            } catch (InterruptedException e) {
                System.out.println("Received InterruptedException upon trying to sleep for " + SECONDS_TO_WAIT
                        + " seconds, per the \"politeness policy.\"");
                e.printStackTrace();
            }
            politenessPolicyCounter = 0;
        }
        politenessPolicyCounter++;

        String line;
        try {
            print("Curling " + BASE_URL + urlString + " ...");
            URL url = new URL(BASE_URL + urlString);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append(' ');
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException:  " + urlString);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException:  " + urlString);
            e.printStackTrace();
        }
        return null;
    }

    // Used for printing helpful information if VERBOSE_OUTPUT is set to 'true'
    private void print(String msg) {
        if (VERBOSE_OUTPUT) {
            System.out.println(msg);
        }
    }
}
