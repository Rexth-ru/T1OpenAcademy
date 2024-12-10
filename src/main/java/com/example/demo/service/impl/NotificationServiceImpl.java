package com.example.demo.service.impl;

import com.example.demo.model.dto.TaskUpdateDto;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${email}")
    private String mailTo;
    @Value("${subject}")
    private String subject;
    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender emailSender;

    public NotificationServiceImpl(final JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(final TaskUpdateDto taskDto) {
        String text = "Task ID: " + taskDto.getId() + " has been updated to status: " + taskDto.getStatus();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(mailTo);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}

