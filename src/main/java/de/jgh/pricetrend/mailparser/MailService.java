package de.jgh.pricetrend.mailparser;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

@Service
public class MailService {

    public ArrayList<String> getMails(String host, String port, String user, String pw) throws Exception {

        ArrayList<String> htmlMails = new ArrayList<>();

        Properties props = new Properties();
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.setProperty("mail.imaps.host", host);
        props.setProperty("mail.imaps.user", user);
        props.setProperty("mail.imaps.password", pw);
        props.setProperty("mail.imaps.port", port);
        props.setProperty("mail.imaps.auth", "true");
//        props.setProperty("mail.debug", "true");
        props.setProperty("mail.imap.starttls.enable", "true");
        props.put("mail.imap.ssl.socketFactory", sf);

        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");
        store.connect(host, Integer.valueOf(port), user, pw);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();

        Arrays.stream(messages).forEach(message -> {
            try {
                MimeMessage mimeMessage = new MimeMessageHelper((MimeMessage) message).getMimeMessage();
                MimeMessageParser mimeMessageParser = new MimeMessageParser(mimeMessage).parse();
                htmlMails.add(mimeMessageParser.getHtmlContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return htmlMails;
    }

}
