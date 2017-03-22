public class Main {

    public static void main(String[] args) {
        WikiCrawler w = new WikiCrawler("/wiki/Complexity_theory", 20, "wikiCS.txt");
        w.crawl();
    }
}
