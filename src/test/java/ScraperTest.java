
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class ScraperTest {

    @Test
    public void scrape_vendor() {
        String input;

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            input = IOUtils.toString(classLoader.getResourceAsStream("input.html"));
            Document document = Jsoup
                    .parse(input);

            Element body = document.body();
            Elements vendor_contact_box = body.getElementsByClass("cldt-vendor-contact-box");

            String anbieterType = vendor_contact_box.get(1).child(0).text();
            String city = vendor_contact_box.get(1).child(1).child(0).text();
            // auto vendor_contact_box.get(1).child(1).child(2).text()
            String country = vendor_contact_box.get(1).child(1).child(1).text();

            System.out.println("AnbieterTyp: " + anbieterType);
            System.out.println("City: " + city);
            System.out.println("Country: " + country);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
