public class Main {

    public static void main(String[] args) {
        WikiCrawler w = new WikiCrawler("/wiki/Computer_Science", 20, "wikiCS.txt");
        w.crawl();
    }
}
