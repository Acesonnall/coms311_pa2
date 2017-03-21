import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * The WikiCrawler class has methods that can be used to crawl Wiki.
 *
 * @author nkarasch
 */
public class WikiCrawler {
    private String seedUrl;
    private int max;
    private String fileName;
    private static final String BASE_URL = "https://www.wikipedia.org";

    /**
     * @param seedUrl  The relative address of the seed url (within Wiki domain)
     * @param max      Represents maximum number of pages to be crawled
     * @param fileName Represents the name of the file the graph will be written to
     */
    public WikiCrawler(String seedUrl, int max, String fileName) {
        this.seedUrl = seedUrl;
        this.max = max;
        this.fileName = fileName;
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
        // TODO
        return null;
    }

    /**
     * This method should construct the web graph over following pages: Consider the first
     * max many pages that are visited when you do a BFS with seedUrl. Your program should construct
     * the web graph only over those pages. and writes the graph to the file fileName.
     */
    public void crawl() {
        // TODO
    }

    private String curlUrl(String urlString) {
        String line;
        boolean startingTagFound = false;
        int startingTagIndex;
        try {
            URL url = new URL(BASE_URL + urlString);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (startingTagFound) {
                    sb.append(line).append('\n');
                } else if ((startingTagIndex = line.toLowerCase().indexOf("<p>")) >= 0) {
                    startingTagFound = true;
                    sb.append(line.substring(startingTagIndex)).append('\n');
                }
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
        return null;
    }
}
