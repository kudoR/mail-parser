package de.jgh.pricetrend.mailparser;

import de.jgh.pricetrend.mailparser.model.*;
import de.jgh.pricetrend.mailparser.repo.DetailEntryRepository;
import de.jgh.pricetrend.mailparser.repo.ModelEntryRepository;
import de.jgh.pricetrend.mailparser.repo.RawEntryRepository;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static de.jgh.pricetrend.mailparser.AnbieterType.GEWERBLICH;
import static de.jgh.pricetrend.mailparser.AnbieterType.PRIVAT;
import static de.jgh.pricetrend.mailparser.Artikelzustand.GEBRAUCHT;
import static de.jgh.pricetrend.mailparser.Artikelzustand.NEU;

@Service
@Profile("production")
public class ParserService {

    @Autowired
    private DetailEntryRepository detailEntryRepository;

    @Autowired
    private RawEntryRepository rawEntryRepository;

    @Autowired
    private ModelEntryRepository modelEntryRepository;

    @Value("${parser_dialect}")
    private String parserDialect;

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
        try {
            Document document = Jsoup.connect(url).get();
            String priceAsString = document.getElementsByClass("cldt-price").get(0).text();
            String model = document.getElementsByClass("cldt-detail-makemodel").get(0).text();
            Element body = document.body();
            Elements vendor_contact_box = body.getElementsByClass("cldt-vendor-contact-box");

            AnbieterType anbieterType = body.getElementsByClass("cldt-stage-vendor-text").text().contains("Gewerblicher Anbieter") ? GEWERBLICH : PRIVAT;

            String zipNCity = vendor_contact_box.get(1).child(1).child(0).text();
            if (parserDialect.equals("car")) {
                try {
                    zipNCity = vendor_contact_box.get(1).child(1).child(2).text();

                } catch (Exception e) {

                }
            }

            String zip = "";
            try {
                zip = zipNCity.split(" ")[0];
            } catch (Exception e) {

            }
            String city = "";
            try {
                city = zipNCity.split(" ")[1];
            } catch (Exception e) {

            }

            boolean unfall = false;
            try {
                unfall = body.getElementsByClass("sc-font-s cldt-stage-att-description").get(1).text().contains("Unfallfahrzeug");
            } catch (Exception e) {

            }
            Artikelzustand gebrauchtNeu = GEBRAUCHT;
            try {
                gebrauchtNeu =
                        !body.getElementsByClass("sc-font-s cldt-stage-att-description").get(0).text().contains("Gebraucht") ?
                                NEU : GEBRAUCHT;
            } catch (Exception e) {

            }

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
                    detailEntry.setAnbieterType(anbieterType);
                    detailEntry.setCity(city);
                    detailEntry.setZip(zip);
                    detailEntry.setArtikelzustand(gebrauchtNeu);
                    detailEntry.setUnfall(unfall);
                    detailEntryRepository.save(detailEntry);
                }

                saveModelEntry(inseratId, model);

            } catch (Exception e) {
            }
        } catch (HttpStatusException e) {
            rawEntryRepository
                    .findByLink(inseratId)
                    .stream()
                    .map(rawEntry -> rawEntry.setOffline())
                    .forEach(rawEntryRepository::save);
        }


    }

    private void saveModelEntry(String inseratId, String model) {
        List<RawEntry> byLink = rawEntryRepository.findByLink(inseratId);
        if (!byLink.isEmpty()) {
            Long id = byLink.get(0).getId();

            Optional<ModelEntry> byId = modelEntryRepository.findById(id);
            if (!byId.isPresent()) {
                ModelEntry modelEntry = new ModelEntry();
                modelEntry.setId(id);
                modelEntry.setModel(model);
                modelEntryRepository.save(modelEntry);
            }
        }
    }

}