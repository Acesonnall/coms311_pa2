public class Main {

    public static void main(String[] args) {
        WikiCrawler w = new WikiCrawler("/wiki/Computer_Science", 100, "wikiCS.txt");
        w.crawl();
    }
}
