package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.mail")
@ToString
public class MailProp {
    private String protocol;
    private String host;
    private Integer port;
    private String username;
    private String password;

    @NestedConfigurationProperty
    private Properties properties;

    @Getter
    @Setter
    @ToString
    public static class Properties {
        private Smtp smtp;
        private Starttls starttls;

        @Getter
        @Setter
        @ToString
        public static class Smtp {
            private String auth;
        }

        @Getter
        @Setter
        @ToString
        public static class Starttls {
            private String enable;
            private String require;
        }
    }
}
