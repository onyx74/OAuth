package com.vanya.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;


    public void sendMessage(final String recipientAddress,
                            final String message,
                            final String subject) {
        SimpleMailMessage mail = constructEmailMessage(recipientAddress, message, subject);
        send(mail);
    }

    public void send(SimpleMailMessage email) {
        mailSender.send(email);
    }

    public final SimpleMailMessage constructEmailMessage(final String recipientAddress,
                                                         final String message,
                                                         final String subject) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
