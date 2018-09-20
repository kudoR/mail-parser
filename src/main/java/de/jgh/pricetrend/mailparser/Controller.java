package de.jgh.pricetrend.mailparser;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class Controller {

    private ParserService parserService;
    private MailService mailService;
    private BaseEntryRepository baseEntryRepository;
    private ProcessedEntryRepository processedEntryRepository;

    public Controller(ParserService parserService, MailService mailService, BaseEntryRepository baseEntryRepository, ProcessedEntryRepository processedEntryRepository) {
        this.parserService = parserService;
        this.mailService = mailService;
        this.baseEntryRepository = baseEntryRepository;
        this.processedEntryRepository = processedEntryRepository;
    }

    @RequestMapping(value = "fetchMails", produces = APPLICATION_JSON_VALUE)
    public Object fetchMails(
            @RequestParam String host,
            @RequestParam String port,
            @RequestParam String user,
            @RequestParam String pw) throws Exception {
        Stream<List<AutoScoutEntryDTO>> listStream = mailService
                .getMails(host, port, user, pw)
                .stream()
                .map(parserService::parseMail);
        return listStream;
    }

    @RequestMapping(value = "parseMailsAndSaveData", produces = APPLICATION_JSON_VALUE)
    public void parseMailsAndSaveData(
            @RequestParam String host,
            @RequestParam String port,
            @RequestParam String user,
            @RequestParam String pw) throws Exception {

        Stream<List<AutoScoutEntryDTO>> listStream = mailService
                .getMails(host, port, user, pw)
                .stream()
                .map(parserService::parseMail);

        listStream.forEach(list -> {
            baseEntryRepository.saveAll(
                    list
                            .stream()
                            .map(dto -> new BaseEntry(dto))
                            .collect(Collectors.toList())
            );
        });
    }

    @RequestMapping(value = "getSavedEntries", produces = APPLICATION_JSON_VALUE)
    public List<BaseEntry> getSavedEntries() {
        return baseEntryRepository.findAll();
    }

    @RequestMapping(value = "processEntries", produces = APPLICATION_JSON_VALUE)
    public void processEntries() {
        processedEntryRepository.saveAll(
            this.getSavedEntries()
                    .stream()
                    .map(o -> new ProcessedEntry(o))
                    .collect(Collectors.toList())
        );
    }
}
