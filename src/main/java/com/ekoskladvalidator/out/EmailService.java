package com.ekoskladvalidator.out;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailWithAttachment(String to, String subject, String text, String attachmentPath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setFrom("ruslan543213@gmail.com");

        // Добавление вложения
        File file = new File(attachmentPath);
        if (file.exists()) {
            helper.addAttachment(file.getName(), file);
        } else {
            throw new MessagingException("Файл не найден: " + attachmentPath);
        }

        mailSender.send(message);
    }
}