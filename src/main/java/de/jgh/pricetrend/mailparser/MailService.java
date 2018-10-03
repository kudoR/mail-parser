package de.jgh.pricetrend.mailparser;

import com.sun.mail.util.MailSSLSocketFactory;
import de.jgh.pricetrend.mailparser.MessageProcessorConfig.MessageProcessor;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
public class MailService {

    private MailRepository mailRepository;
    private MessageProcessor messageProcessor;

    @Autowired
    public MailService(MailRepository mailRepository, MessageProcessor messageProcessor) {
        this.mailRepository = mailRepository;
        this.messageProcessor = messageProcessor;
    }

    public void fetchAndSaveMails(String host, String port, String user, String pw) throws Exception {

        Properties props = new Properties();
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.setProperty("mail.imaps.host", host);
        props.setProperty("mail.imaps.user", user);
        props.setProperty("mail.imaps.password", pw);
        props.setProperty("mail.imaps.port", port);
        props.setProperty("mail.imaps.auth", "true");

        props.setProperty("mail.imap.starttls.enable", "true");
        props.put("mail.imap.ssl.socketFactory", sf);

        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");
        store.connect(host, Integer.valueOf(port), user, pw);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);

        Message[] messages = inbox.getMessages();

        Arrays.stream(messages).forEach(message -> {
            try {
                messageProcessor.process(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        inbox.close(true);
    }

}