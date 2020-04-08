package com.sivalabs.bookmarker.domain.model.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String from;
    private List<String> to = new ArrayList<>();
    private List<String> cc = new ArrayList<>();
    private List<String> bcc = new ArrayList<>();
    private String subject;
    private String text;
    private String mimeType;
    private List<EmailAttachment> attachments = new ArrayList<>();

    public void addTo(String toEmail) {
        this.to.add(toEmail);
    }
    public void addCc(String ccEmail) {
        this.cc.add(ccEmail);
    }
    public void addBcc(String bccEmail) {
        this.bcc.add(bccEmail);
    }

    public void addAttachment(EmailAttachment attachment) {
        this.attachments.add(attachment);
    }
}
