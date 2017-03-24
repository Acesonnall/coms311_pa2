import java.io.IOException;

/**
 * @author nkarasch
 */
public class _ReportRunner {
    public static void main(String[] args) throws IOException {
        // Last crawled at around 1:15 pm on 24 March 2017
//        WikiCrawler w = new WikiCrawler("/wiki/Computer_Science", 500, "wikiCS.txt");
//        w.crawl();

        GraphProcessor g = new GraphProcessor("wikiCS.txt");

        highestOut(g);
        numComponents(g);
        sizeOfLargest(g);
    }

    // Vertex with highest out degree
    private static void highestOut(GraphProcessor g) {
        // Make the private method in the GraphProcessor class public and uncomment below
//        System.out.println("Highest out degree:  " + g.largestOutDegree());
    }

    // Number of components of the graph
    private static void numComponents(GraphProcessor g) {
        System.out.println("Number of components:  " + g.numComponents());
    }

    // Size of the largest component.
    private static void sizeOfLargest(GraphProcessor g) {
        System.out.println("Size of largest component:  " + g.largestComponent());
    }
}
