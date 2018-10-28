package de.jgh.pricetrend.mailparser.repo;

import de.jgh.pricetrend.mailparser.model.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {

    List<Mail> findByProcessed(boolean processed);

}
