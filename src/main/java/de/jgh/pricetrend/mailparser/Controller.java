package de.jgh.pricetrend.mailparser;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class Controller {

    private ParserService parserService;
    private MailService mailService;

    public Controller(ParserService parserService, MailService mailService) {
        this.parserService = parserService;
        this.mailService = mailService;
    }

    @RequestMapping(value = "fetchMails", produces = APPLICATION_JSON_VALUE)
    public Object fetchMails(@RequestParam String user, @RequestParam String pw) throws Exception {
        Stream<List<AutoScoutEntryDTO>> listStream = mailService
                .getMails(user, pw)
                .stream()
                .map(parserService::parseMail);
        return listStream;
    }
}
