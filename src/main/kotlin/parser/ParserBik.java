package parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ParserBik {
    private Document document;

    public void parseWebPage(String bik) {
        try {
            String url = "https://bik-info.ru/bik_";
            Connection.Response response = Jsoup.connect(url + bik + ".html").timeout(10_000).execute();
            if (response.statusCode() == 200) {
                document = response.parse();
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCorrAccount() {
        String selectorCorrAccount = "body > div.container > ul:nth-child(7) > li:nth-child(2) > b";
        return document.select(selectorCorrAccount).html();
    }

    public String getBakName() {
        String selectorNameBank = "body > div.container > ul:nth-child(7) > li:nth-child(3) > b";
        return document.select(selectorNameBank).html();
    }
}
