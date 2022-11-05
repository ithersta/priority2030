package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class ParserBik {
    private Document document;

    void parseWebPage(String bik) {
        try {
            String url = "https://bik-info.ru/bik_";
            document = Jsoup.connect(url + bik + ".html").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String getCorrAccount() {
        String selectorCorrAccount = "body > div.container > ul:nth-child(7) > li:nth-child(2) > b";
        return document.select(selectorCorrAccount).html();
    }

    String getBakName() {
        String selectorNameBank = "body > div.container > ul:nth-child(7) > li:nth-child(3) > b";
        return document.select(selectorNameBank).html();
    }
}
