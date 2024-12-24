package com.example.demo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(MailProp.class)
public class MailConfig {

    private final MailProp properties;

    public MailConfig(final MailProp properties) {
        this.properties = properties;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(properties.getHost());
        mailSender.setPort(properties.getPort());
        mailSender.setUsername(properties.getUsername());
        mailSender.setPassword(properties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", properties.getProtocol());

        MailProp.Properties mailProperties = properties.getProperties();
        if (mailProperties != null && mailProperties.getSmtp() != null) {
            props.put("mail.smtp.auth", mailProperties.getSmtp().getAuth());
            MailProp.Properties.Smtp.Starttls starttls = mailProperties.getSmtp().getStarttls();
            if (starttls != null) {
                props.put("mail.smtp.starttls.enable", starttls.getEnable());
                props.put("mail.smtp.starttls.required", starttls.getRequired());
                props.put("mail.debug", starttls.getRequired());
            }
        }

        return mailSender;
    }
}