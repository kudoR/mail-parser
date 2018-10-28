package de.jgh.pricetrend.mailparser.repo;

import de.jgh.pricetrend.mailparser.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
