package de.jgh.pricetrend.mailparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/calcScoring")
public class ScorecardController {

    @Autowired
    private ScorecardService scorecardService;

    @GetMapping
    public void initFullScoring() {
        scorecardService.calculateScoring();
    }
}
