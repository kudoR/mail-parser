package de.jgh.pricetrend.mailparser;

import de.jgh.pricetrend.mailparser.model.ScoreCardExtended;
import de.jgh.pricetrend.mailparser.repo.ScoreCardExtendedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ScorecardController {

    @Autowired
    private ScorecardService scorecardService;

    @Autowired
    private ScoreCardExtendedRepository scoreCardExtendedRepository;

    @GetMapping("/calcScoring")
    public void initFullScoring() {
        System.out.println("called initFullScoring");
        scorecardService.calculateScoring();
    }

    @GetMapping("/calcScoring/{model}")
    public void initScoringForModel(@PathVariable("model") String model) {
        System.out.println("called initFullScoring");
        scorecardService.calculateScoringForModel(model);
    }

    @GetMapping("/candidates/{model}/{limitDesiredPricePercentileNiveau}")
    public List<ScoreCardExtended> getFilteredResult(
            @PathVariable("model") String model,
            @PathVariable("limitDesiredPricePercentileNiveau") Integer limitDesiredPricePercentileNiveau) {
        return getFilteredResult(model)
                .stream()
                .filter(scoreCardExtended -> scoreCardExtended.getPricePercentile() != null)
                .filter(scoreCardExtended -> scoreCardExtended.getPricePercentile() > 0)
                .filter(scoreCardExtended -> scoreCardExtended.getPricePercentile() < limitDesiredPricePercentileNiveau)
                .collect(Collectors.toList());
    }

    @GetMapping("/candidates/{model}")
    public List<ScoreCardExtended> getFilteredResult(@PathVariable("model") String model) {
        return scoreCardExtendedRepository
                .findAll()
                .stream()
                .filter(scoreCardExtended -> scoreCardExtended.getModel().equals(model))
                .collect(Collectors.toList());
    }
}
