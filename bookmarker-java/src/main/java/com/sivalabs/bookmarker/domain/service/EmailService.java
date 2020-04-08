package com.sivalabs.bookmarker.domain.service;

import com.sivalabs.bookmarker.domain.model.email.Email;
import com.sivalabs.bookmarker.domain.model.email.EmailAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public void sendEmail(Email email) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // use the true flag to indicate you need a multipart message
        boolean hasAttachments = (email.getAttachments() != null && !email.getAttachments().isEmpty());
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, hasAttachments);
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(email.getTo().toArray(new String[0]));
        helper.setSubject(email.getSubject());
        helper.setText(email.getText(), true);

        List<EmailAttachment> attachments = email.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            for (EmailAttachment attachment : attachments) {
                String filename = attachment.getFilename();
                DataSource dataSource = new ByteArrayDataSource(attachment.getData(), attachment.getMimeType());
                if (attachment.isInline()) {
                    helper.addInline(filename, dataSource);
                } else {
                    helper.addAttachment(filename, dataSource);
                }
            }
        }
        mailSender.send(mimeMessage);
    }
}
