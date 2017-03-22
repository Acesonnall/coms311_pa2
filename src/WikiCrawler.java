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
    private String seedUrl;
    private int max;
    private String fileName;

    private HashSet<String> vertices;
    private HashSet<String> visited;
    private HashSet<Edge> edges;
    private int discoveryCount = 0;

    private static final String BASE_URL = "https://www.wikipedia.org";

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
        visited = new HashSet<>(initialSize);
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
     * This method should construct the web graph over following pages: Consider the first
     * max many pages that are visited when you do a BFS with seedUrl. Your program should construct
     * the web graph only over those pages. and writes the graph to the file fileName.
     */
    public void crawl() {
        BFS(seedUrl);
        Edge[] edgeArray = hashSetToArray(edges);
        Arrays.sort(edgeArray, new EdgeComparator());
        saveToFile(edgeArray, vertices.size());
    }

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

    private void BFS(String v) {
        LinkedList<String> queue = new LinkedList<>();
        queue.add(v);
        visited.add(v);
        while (!queue.isEmpty()) {
            String u = queue.pop();
            String urlDoc = curlUrl(u);
            ArrayList<String> links = extractLinks(urlDoc);
            for (String link : links) {
                if (vertices.contains(link)) {
                    edges.add(new Edge(u, link, discoveryCount++));
                } else if (vertices.size() < max) {
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
            System.out.println("Results saved to " + fileName);
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

    private String curlUrl(String urlString) {
        String line;
        try {
            System.out.println("Curling " + BASE_URL + urlString + " ...");
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
}
