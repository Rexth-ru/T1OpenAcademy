package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MailProp.class)
public class MailConfig {

    private final MailProp properties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(properties.getHost());
        mailSender.setPort(properties.getPort());

        mailSender.setUsername(properties.getUsername());
        mailSender.setPassword(properties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", properties.getProtocol());
        props.put("mail.smtp.auth", properties.getProperties().getSmtp().getAuth());
        props.put("mail.smtp.ssl.enable", properties.getProperties().getStarttls().getEnable());
        props.put("mail.debug", properties.getProperties().getStarttls().getRequire());

        return mailSender;
    }
}
