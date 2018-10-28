package de.jgh.pricetrend.mailparser.repo;

import de.jgh.pricetrend.mailparser.model.ScoreCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {

}
