import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        String u, v;

//        WikiCrawler w = new WikiCrawler("/wiki/Computer_Science", 500, "wikiCS.txt");
//        w.crawl();

        // TODO: Test with absolute path
//        GraphProcessor g = new GraphProcessor("wikiCS.txt");
        GraphProcessor g = new GraphProcessor("gp_test.txt");

        v = "Y";
        System.out.println("g.outDegree(\"" + v + "\")  =  " + g.outDegree(v));
        v = "";
        System.out.println("g.outDegree(\"" + v + "\")  =  " + g.outDegree(v));
        v = "";
        System.out.println("g.outDegree(\"" + v + "\")  =  " + g.outDegree(v));
        v = "";
        System.out.println("g.outDegree(\"" + v + "\")  =  " + g.outDegree(v));
        v = "";
        System.out.println("g.outDegree(\"" + v + "\")  =  " + g.outDegree(v));

        u = "/wiki/Metamodeling";
        v = "/wiki/Computer_Science";
        System.out.println("Running g.sameComponent(\"" + u + "\", \"" + v + "\")");
        boolean sameComponent = g.sameComponent(u, v);
        System.out.println("  Result:  " + sameComponent);

        v = "/wiki/Metamodeling";
        System.out.println("Running g.componentVertices(\"" + v + "\")");
        ArrayList<String> componentVertices = g.componentVertices(v);
        System.out.println("  Result:\n  [");
        for (String componentVertex : componentVertices) {
            System.out.println(componentVertex);
        }
        System.out.println("  ]");

        System.out.println("Running g.largestComponent()");
        int largestComponent = g.largestComponent();
        System.out.println("  Result:  " + largestComponent);

        System.out.println("Running g.numComponents()");
        int numComponents = g.numComponents();
        System.out.println("  Result:  " + numComponents);

        u = "/wiki/Metamodeling";
        v = "/wiki/Computer_Science";
        System.out.println("Running g.bfsPath(\"" + u + "\", \"" + v + "\")");
        ArrayList<String> bfsPath = g.bfsPath(u, v);
        System.out.println("  Result:\n  [");
        for (String s : bfsPath) {
            System.out.println(s);
        }
        System.out.println("  ]");
    }
}
