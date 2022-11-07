package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Parser {
    //     0 -  ООО , 1  - ИП
    private byte type = 0;
    private final String select = "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div >";
    private Document document;

    public Document parsing(String inn) {
        try {
            String url = "https://sbis.ru/contragents/";
            document = Jsoup.connect(url + inn).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        type = (byte) (!document.html().contains("ИП") ? 0 : 1);
        return document;
    }

    // для ООО у которых есть несколкьо организаций внутри себя
    public Document parsing(String inn, String kpp) {
        try {
            String url = "https://sbis.ru/contragents/";
            document = Jsoup.connect(url + inn + "/" + kpp).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return document;
    }

    public String getFullName() {
        String fullName = document.select(select.concat(" div:nth-child(1) > div.cCard__MainReq >" +
                " div.ws-flexbox.ws-justify-content-between > div.cCard__MainReq-LeftSide.ws-flexbox.ws-flex-column >" +
                " div.cCard__MainReq-Left > div.cCard__Director.ws-flexbox.ws-flex-column.ws-flex-wrap.ws-justify" +
                "-content-start.ws-align-items-start > div > span")).html();
        if (fullName.isEmpty()) {
            fullName = document.select(select.concat(" div:nth-child(1) >" +
                    " div.cCard__MainReq.cCard__MainReq-IP > div.cCard__Name-Addr-IP >" +
                    " div.cCard__MainReq-Name > h1")).html().replaceAll(", ИП", "");
        }
        return fullName;
    }

    public String getShortName() {
        String[] fAndIO = getFullName().split(" ", 2);
        return (fAndIO[0].concat(" ").concat(fAndIO[1].replaceAll("[а-я]+", ".")));
    }

    private String[] getMainInfoAboutOrg() {
        String infoAboutOrg = document.select(select.concat(" div.cCard__CompanyDescription >" +
                " p:nth-child(5)")).html().replaceAll("<!-- -->,", "");
        return infoAboutOrg.replaceAll("присвоен", "").split("&nbsp;<!-- -->");
    }

    public String getFullNameOfOrg() {
        return type == 0 ? getMainInfoAboutOrg()[0] : "ИП " + getMainInfoAboutOrg()[0];
    }

    public String getInnOfOrg() {
        return getMainInfoAboutOrg()[1];
    }

    public String getKppOfOrg() {
        return getMainInfoAboutOrg()[2];
    }

    public String getOrgnOfOrg() {
        return type == 0 ? getMainInfoAboutOrg()[3] : getMainInfoAboutOrg()[2];
    }

    public String getOkpoOfOrg() {
        return type == 0 ? getMainInfoAboutOrg()[4] : getMainInfoAboutOrg()[3];
    }
}

