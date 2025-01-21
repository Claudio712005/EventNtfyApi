package com.clau.eventntfy.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

  @Value("${twilio.account.sid}")
  private String accountSid;

  @Value("${twilio.auth.token}")
  private String authToken;

  @Value("${twilio.phone.number}")
  private String phoneNumber;

  private static final Logger LOGGER = LoggerFactory.getLogger(TwilioConfig.class);

  public String getPhoneNumber() {
    return phoneNumber;
  }

  @PostConstruct
  public void initTwilio() {
    Twilio.init(accountSid, authToken);
    LOGGER.info("Twilio initialized with account sid: {} and auth token: {}", accountSid, authToken);
  }
}
