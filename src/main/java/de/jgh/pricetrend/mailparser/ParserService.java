package de.jgh.pricetrend.mailparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ParserService {

    @Autowired
    private DetailEntryRepository detailEntryRepository;

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

    public void fetchDetailEntry(String inseratId) throws IOException {
        boolean createNewDetailEntry = false;

        String url = String.format("http://click.rtm.autoscout24.com/?qs=%s", inseratId);
        Document document = Jsoup.connect(url).get();

        String priceAsString = document.getElementsByClass("cldt-price").get(0).text();
        String model = document.getElementsByClass("cldt-detail-makemodel").get(0).text();
        try {
            priceAsString = priceAsString
                    .replace(",", "")
                    .replace(".", "")
                    .replace("-", "")
                    .replace("€", "");
            List<DetailEntry> byIdInseratId = detailEntryRepository.findByIdInseratId(inseratId);

            Double parsedPrice = Double.valueOf(priceAsString);

            if (!byIdInseratId.isEmpty()) {
                Optional<DetailEntry> max = byIdInseratId.stream().max(Comparator.comparing(o -> o.getId().getDateTime()));
                if (!max.isPresent() || !max.get().getPrice().equals(parsedPrice)) {
                    createNewDetailEntry = true;
                }
            } else {
                createNewDetailEntry = true;
            }

            if (createNewDetailEntry) {
                DetailEntry detailEntry = new DetailEntry(new DetailEntryId(inseratId));
                detailEntry.setPrice(parsedPrice);
                detailEntry.setModel(model);
                detailEntryRepository.save(detailEntry);
            }

        } catch (Exception e) {
        }
    }

}
