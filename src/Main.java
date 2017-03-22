import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        String u, v;

//        WikiCrawler w = new WikiCrawler("/wiki/Computer_Science", 500, "wikiCS.txt");
//        w.crawl();

        // TODO: Test with absolute path
        GraphProcessor g = new GraphProcessor("wikiCS.txt");

        v = "/wiki/Computer_Science";
        System.out.println("Running g.outDegree(\"" + v + "\")");
        long start = System.nanoTime();
        int outDegree = g.outDegree(v);
        long end = System.nanoTime();
        System.out.println("  Result:  " + outDegree);
        System.out.println("  Runtime: " + (end - start) + " ns   (" + ((end - start) / 1000000.0) + " seconds)");

        u = "";
        v = "";
        System.out.println("Running g.sameComponent(\"" + u + "\", \"" + v + "\")");
        start = System.nanoTime();
        boolean sameComponent = g.sameComponent(u, v);
        end = System.nanoTime();
        System.out.println("  Result:  " + sameComponent);
        System.out.println("  Runtime: " + (end - start) + " ns   (" + ((end - start) / 1000000.0) + " seconds)");

        v = "";
        System.out.println("Running g.componentVertices(\"" + v + "\")");
        start = System.nanoTime();
        ArrayList<String> componentVertices = g.componentVertices(v);
        end = System.nanoTime();
        System.out.println("  Result:\n  [");
        for (String componentVertex : componentVertices) {
            System.out.println(componentVertex);
        }
        System.out.println("  ]");
        System.out.println("  Runtime: " + (end - start) + " ns   (" + ((end - start) / 1000000.0) + " seconds)");

        System.out.println("Running g.largestComponent()");
        start = System.nanoTime();
        int largestComponent = g.largestComponent();
        end = System.nanoTime();
        System.out.println("  Result:  " + largestComponent);
        System.out.println("  Runtime: " + (end - start) + " ns   (" + ((end - start) / 1000000.0) + " seconds)");

        System.out.println("Running g.numComponents()");
        start = System.nanoTime();
        int numComponents = g.numComponents();
        end = System.nanoTime();
        System.out.println("  Result:  " + numComponents);
        System.out.println("  Runtime: " + (end - start) + " ns   (" + ((end - start) / 1000000.0) + " seconds)");

        v = "";
        u = "";
        System.out.println("Running g.bfsPath(\"" + u + "\", \"" + v + "\")");
        start = System.nanoTime();
        ArrayList<String> bfsPath = g.bfsPath(u, v);
        end = System.nanoTime();
        System.out.println("  Result:\n  [");
        for (String s : bfsPath) {
            System.out.println(s);
        }
        System.out.println("  ]");
        System.out.println("  Runtime: " + (end - start) + " ns   (" + ((end - start) / 1000000.0) + " seconds)");
    }
}
