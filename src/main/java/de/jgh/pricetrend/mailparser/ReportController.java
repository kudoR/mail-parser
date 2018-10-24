package de.jgh.pricetrend.mailparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.stream.Stream;

@RestController("/report")
public class ReportController {

    // TODO: Fixme: a controller in a controller is a controller in a ...
    @Autowired
    private ModelDetailPriceController modelDetailPriceController;

    @Autowired
    private ModelDetailPriceRepository modelDetailPriceRepository;

    @GetMapping
    public Object getReport() {
        HashMap<Long, Double> report = new HashMap<>();

        Iterable<ModelDetailPrice> all = modelDetailPriceRepository.findAll();
        Stream<Iterable<ModelDetailPrice>> iterableStream = Stream.of(all);
        System.out.println("to process: " + iterableStream.count());
        all.forEach(modelDetailPrice -> {

            Long id = modelDetailPrice.getId();
            String model = modelDetailPrice.getModel();

            Long laufleistung = modelDetailPrice.getLaufleistung();
            if (laufleistung != null) {
                LocalDate erstzulassungRaw = modelDetailPrice.getErstzulassung();
                if (erstzulassungRaw != null) {
                    LocalDateTime erstzulassung = LocalDateTime.of(erstzulassungRaw, LocalTime.MIN);
                    BigDecimal preis = modelDetailPrice.getPreis();
                    HashMap<String, Double> evaluation = modelDetailPriceController.evaluation(model, String.valueOf(laufleistung), String.valueOf(erstzulassung), String.valueOf(preis));
                    Double evaluatedNiveau = evaluation.get("evaluatedNiveau");
                    Double priceNiveau = evaluation.get("priceNiveau");
                    if (priceNiveau > 0 && priceNiveau < evaluatedNiveau) {
                        report.put(id, evaluatedNiveau - priceNiveau);
                        System.out.println("report size: " + report.size());
                    }
                }
            }
        });

        return report;
    }
}
