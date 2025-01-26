package com.clau.eventntfy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Properties;

@Configuration
public class EmailConfig {

  @Value("${spring.mail.host}")
  private String mailHost;

  @Value("${spring.mail.port}")
  private int mailPort;

  @Value("${spring.mail.username}")
  private String mailUsername;

  @Value("${spring.mail.password}")
  private String mailPassword;

  @Value("${spring.mail.properties.mail.smtp.auth}")
  private String smtpAuth;

  @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
  private String startTlsEnable;

  @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
  private int connectionTimeout;

  @Value("${spring.mail.properties.mail.smtp.timeout}")
  private int timeout;

  @Value("${spring.mail.properties.mail.smtp.writetimeout}")
  private int writeTimeout;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailHost);
    mailSender.setPort(mailPort);
    mailSender.setUsername(mailUsername);
    mailSender.setPassword(mailPassword);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.smtp.auth", smtpAuth);
    props.put("mail.smtp.starttls.enable", startTlsEnable);
    props.put("mail.smtp.connectiontimeout", connectionTimeout);
    props.put("mail.smtp.timeout", timeout);
    props.put("mail.smtp.writetimeout", writeTimeout);

    return mailSender;
  }
}
