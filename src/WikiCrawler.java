import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
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

    private HashSet<String> vertices;

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
        vertices = new HashSet<>((int) Math.ceil(1.5 * max));
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
        String seedDoc = curlUrl(seedUrl);
        ArrayList<String> links = extractLinks(seedDoc);
        for (String link : links) {
            if (vertices.size() < max) {
                vertices.add(link);
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
