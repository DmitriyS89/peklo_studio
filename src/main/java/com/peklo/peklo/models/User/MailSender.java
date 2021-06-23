package com.peklo.peklo.models.User;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.File;

@RequiredArgsConstructor
@Service
public class MailSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;


    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

    public void sendMailWithAttachment(String emailTo, String subject, String text, String fileToAttach) {
        MimeMessage message = mailSender.createMimeMessage();

        File file = new File(fileToAttach);
        if (file.exists() && !file.isDirectory()) {

            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(username);
                helper.setTo(emailTo);
                helper.setSubject(subject);
                helper.setText(text);

                helper.addAttachment("Something.xlsx", file);
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }

        mailSender.send(message);
    }
}
