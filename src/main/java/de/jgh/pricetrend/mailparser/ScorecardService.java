package de.jgh.pricetrend.mailparser;

import de.jgh.pricetrend.mailparser.model.ModelDetailPrice;
import de.jgh.pricetrend.mailparser.model.ScoreCard;
import de.jgh.pricetrend.mailparser.repo.ModelDetailPriceRepository;
import de.jgh.pricetrend.mailparser.repo.RawEntryRepository;
import de.jgh.pricetrend.mailparser.repo.ScoreCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

@Service
public class ScorecardService {

    @Autowired
    private PercentileCalcService percentileCalcService;

    @Autowired
    private ModelDetailPriceRepository modelDetailPriceRepository;

    @Autowired
    private RawEntryRepository rawEntryRepository;

    @Autowired
    private ScoreCardRepository scoreCardRepository;

    public Object calculateScoring() {
        return calculateScoringInternal(modelDetailPriceRepository.findAll());
    }

    public Object calculateScoringForModel(String model) {
        return calculateScoringInternal(modelDetailPriceRepository.findByModel(model));
    }

    private Object calculateScoringInternal(List<ModelDetailPrice> modelDetailPrices) {
        HashMap<String, Double> report = new HashMap<>();

        modelDetailPrices.forEach(modelDetailPrice -> {
            Long id = modelDetailPrice.getId();
            CalculatedPercentile mileagePercentile = null;
            try {
                mileagePercentile = percentileCalcService.mileagePercentile(String.valueOf(modelDetailPrice.getLaufleistung()), modelDetailPrices);
            } catch (Exception e) {
            }

            CalculatedPercentile registrationPercentile = null;
            try {
                registrationPercentile = percentileCalcService.registrationPercentile(String.valueOf(LocalDateTime.of(modelDetailPrice.getErstzulassung(), LocalTime.MIN)), modelDetailPrices);
            } catch (Exception e) {
            }

            CalculatedPercentile pricePercentile = null;
            try {
                pricePercentile = percentileCalcService.pricePercentile(String.valueOf(modelDetailPrice.getPreis()), modelDetailPrices);
            } catch (Exception e) {
            }

            ScoreCard scoreCard = new ScoreCard(id, pricePercentile != null ? pricePercentile.getLabel().getPercentile() : null, registrationPercentile != null ? registrationPercentile.getLabel().getPercentile() : null, mileagePercentile != null ? mileagePercentile.getLabel().getPercentile() : null);
            scoreCardRepository.save(scoreCard);
        });

        return report;
    }
}
