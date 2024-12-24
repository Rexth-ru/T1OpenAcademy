package com.example.demo.service.impl;

import com.example.demo.config.MailProp;
import com.example.demo.model.dto.TaskUpdateDto;
import com.example.demo.service.NotificationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final MailProp prop;

    private final JavaMailSender emailSender;

    public NotificationServiceImpl(final MailProp prop, final JavaMailSender emailSender) {
        this.prop = prop;
        this.emailSender = emailSender;
    }

    public void sendEmail(final TaskUpdateDto taskDto) {
        String text = "Task ID: " + taskDto.getId() + " has been updated to status: " + taskDto.getStatus();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(prop.getFrom());
        message.setTo(prop.getTo());
        message.setSubject(prop.getSubject());
        message.setText(text);
        emailSender.send(message);
    }
}

