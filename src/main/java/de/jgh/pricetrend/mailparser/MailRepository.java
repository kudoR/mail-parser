package de.jgh.pricetrend.mailparser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {

    List<Mail> findByProcessed(boolean processed);

}
