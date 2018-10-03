package de.jgh.pricetrend.mailparser;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class ParserService {

    public List<RawEntry> parseMail(Mail mailEntry) {
        ArrayList<RawEntry> autoScoutEntries = new ArrayList<>();

        String content = mailEntry.getContent();
        String htmlInput = new String(Base64.getDecoder().decode(content));
        Date receivedDate = mailEntry.getReceivedDate();

        Elements elements = Jsoup
                .parse(htmlInput)
                .getElementsByClass("produktbild");

        elements.forEach(element -> {
            String title = element.parent().select("table.drop td").text();
            String link = element.parent().select("table.drop a").attr("href");
            Elements base = element.parent().select("table.mobile-hidden td");
            String preis = base.get(0).text();
            String laufleistung = base.get(2).text();
            String erstzulassung = base.get(4).text();
            String motorleistungInKw = base.get(6).text();
            link = link.replace("parent.phx.event.mailUrlClicked('", "");
            link = link.replace("'); return true;", "");
            link = link.replace("http://click.rtm.autoscout24.com/?qs=", "");
            autoScoutEntries.add(new RawEntry(
                    title.trim(),
                    link.trim(),
                    preis.trim(),
                    laufleistung
                            .trim()
                            .replaceAll(String.valueOf((char) 32), "")
                            .replaceAll(String.valueOf((char) 160), ""),
                    erstzulassung.trim(),
                    motorleistungInKw.trim(), LocalDate.from(receivedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())));
        });
        return autoScoutEntries;
    }
}
