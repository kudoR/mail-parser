package de.jgh.pricetrend.mailparser;

import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.util.Base64;
import java.util.Date;

import static javax.mail.Flags.Flag.DELETED;

@Configuration
public class MessageProcessorConfig {

    @Bean
    @Profile("!production")
    public MessageProcessor defaultMessageProcessor() {
        return new DefaultMessageProcessor();
    }

    @Bean
    @Profile("production")
    public MessageProcessor productionModeMessageProcessor() {
        return new ProductionModeMessageProcessor();
    }

    interface MessageProcessor {
        void process(Message message) throws Exception;
    }

    private class ProductionModeMessageProcessor extends DefaultMessageProcessor implements MessageProcessor {
        @Override
        public void process(Message message) throws Exception {
            super.process(message);
            message.setFlag(DELETED,true);
        }
    }

    private class DefaultMessageProcessor implements MessageProcessor {

        private final String AUTOSCOUT24_SENDER = "AutoScout24 <no-reply@rtm.autoscout24.com>";

        @Autowired
        private MailRepository mailRepository;

        public void process(Message message) throws Exception {
            Mail mail = extractDetails(message);
            if (mail != null) {
                mailRepository.save(mail);
            }
        }

        private Mail extractDetails(Message message) throws Exception {
            MimeMessage mimeMessage = new MimeMessageHelper((MimeMessage) message).getMimeMessage();

            Date receivedDate = mimeMessage.getReceivedDate();
            String title = mimeMessage.getSubject().substring(2);
            Address from = mimeMessage.getSender();

            if (AUTOSCOUT24_SENDER.equals(from.toString())) {
                MimeMessageParser mimeMessageParser = new MimeMessageParser(mimeMessage).parse();
                String content = mimeMessageParser.getHtmlContent();

                return new Mail(
                        title,
                        receivedDate,
                        from.toString(),
                        Base64.getEncoder().encodeToString(content.getBytes())
                );
            }

            return null;
        }
    }
}
