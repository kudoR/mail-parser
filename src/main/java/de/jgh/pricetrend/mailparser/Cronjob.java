package de.jgh.pricetrend.mailparser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Cronjob {

    private MailService mailService;
    private ParserService parserService;
    private BaseEntryRepository baseEntryRepository;
    private ProcessedEntryRepository processedEntryRepository;
    private MailRepository mailRepository;


    public Cronjob(MailService mailService, ParserService parserService, BaseEntryRepository baseEntryRepository, ProcessedEntryRepository processedEntryRepository, MailRepository mailRepository) {
        this.mailService = mailService;
        this.parserService = parserService;
        this.baseEntryRepository = baseEntryRepository;
        this.processedEntryRepository = processedEntryRepository;

        this.mailRepository = mailRepository;
    }

    @Value("${host}")
    private String host;

    @Value("${port}")
    private String port;

    @Value("${user}")
    private String user;

    @Value("${pw}")
    private String pw;

    @Scheduled(fixedRateString = "100000")
    public void refreshData() throws Exception {
        System.out.println("Running refreshData");

        parseMailsAndSaveData();

        processBaseEntries();
    }

    private void parseMailsAndSaveData() throws Exception {
        mailService
                .fetchAndSaveMails(host, port, user, pw);

        //Stream<List<AutoScoutEntryDTO>> listStream
       // Stream.Builder<List<AutoScoutEntryDTO>> builder = Stream.builder();
        List<List<AutoScoutEntryDTO>> listList = new ArrayList<>();
        mailRepository
                .findByProcessed(false)
                .stream()
                .forEach(mail -> {
                    mail.setProcessed(true);
                    mailRepository.save(mail);
                    List<AutoScoutEntryDTO> autoScoutEntryDTOS = parserService.parseMail(mail);
                    listList.add(autoScoutEntryDTOS);
                });
       // Stream<List<AutoScoutEntryDTO>> listStream = builder.build();
        //  .map(parserService::parseMail);

        listList.forEach(list -> baseEntryRepository.saveAll(
                list
                        .stream()
                        .map(dto -> new BaseEntry(dto))
                        .collect(Collectors.toList())
        ));
    }

    private void processBaseEntries() {
        baseEntryRepository.findAll()
                .stream()
                .forEach(baseEntry -> processedEntryRepository.save(new ProcessedEntry(baseEntry)));
    }


}
