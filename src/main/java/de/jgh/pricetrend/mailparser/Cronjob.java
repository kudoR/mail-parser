package de.jgh.pricetrend.mailparser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Cronjob {

    private MailService mailService;
    private ParserService parserService;
    private BaseEntryRepository baseEntryRepository;
    private ProcessedEntryRepository processedEntryRepository;


    public Cronjob(MailService mailService, ParserService parserService, BaseEntryRepository baseEntryRepository, ProcessedEntryRepository processedEntryRepository) {
        this.mailService = mailService;
        this.parserService = parserService;
        this.baseEntryRepository = baseEntryRepository;
        this.processedEntryRepository = processedEntryRepository;

    }

    @Value("${host}")
    private String host;

    @Value("${port}")
    private String port;

    @Value("${user}")
    private String user;

    @Value("${pw}")
    private String pw;

    @Scheduled(fixedRateString = "10000")
    public void refreshData() throws Exception {
        System.out.println("Running refreshData");

        parseMailsAndSaveData();


    }

    private void parseMailsAndSaveData() throws Exception {
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


}
