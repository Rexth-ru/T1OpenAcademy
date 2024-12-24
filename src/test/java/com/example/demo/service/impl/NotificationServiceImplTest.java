package com.example.demo.service.impl;

import com.example.demo.config.MailProp;
import com.example.demo.model.dto.TaskUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private MailProp mailProp;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private TaskUpdateDto taskDto;

    @BeforeEach
    void setUp() {
        taskDto = new TaskUpdateDto(1L, "COMPLETED");
        when(mailProp.getFrom()).thenReturn("from@example.com");
        when(mailProp.getTo()).thenReturn("to@example.com");
        when(mailProp.getSubject()).thenReturn("Task Update");
    }

    @Test
    @DisplayName("Тест успешной отправки сообщения")
    void testSendEmailSuccess() {

        notificationService.sendEmail(taskDto);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("from@example.com");
        message.setTo("to@example.com");
        message.setSubject("Task Update");
        message.setText("Task ID: 1 has been updated to status: COMPLETED");

        verify(javaMailSender).send(eq(message));
    }

    @Test
    @DisplayName("Тест отправки сообщения с ошибкой")
    void testSendEmailWithException() {

        doThrow(new RuntimeException("Failed to send email")).when(javaMailSender).send(any(SimpleMailMessage.class));

        assertThrows(RuntimeException.class, () -> notificationService.sendEmail(taskDto));

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}