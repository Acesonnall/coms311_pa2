import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class _PostSubmissionTest {
    @Test
    public void crawl() {
        WikiCrawler w = new WikiCrawler("/wiki/AA.html", 20, "test/_ps_results.txt");
        w.crawl();
    }

    @Test
    public void scc2() {
        try {
            GraphProcessor g = new GraphProcessor("test/scc-2.txt");
            assertEquals(2, g.largestComponent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bfs2() {
        try {
            GraphProcessor g = new GraphProcessor("test/bfs-2.txt");
            String[] emptyList = {};
            String[] actual = g.bfsPath("D", "F").toArray(new String[0]);
            assertArrayEquals(emptyList, actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bfs3() {
        try {
            GraphProcessor g = new GraphProcessor("test/bfs-3.txt");
            String[] emptyList = {};
            String[] actual = g.bfsPath("B", "C").toArray(new String[0]);
            assertArrayEquals(emptyList, actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
