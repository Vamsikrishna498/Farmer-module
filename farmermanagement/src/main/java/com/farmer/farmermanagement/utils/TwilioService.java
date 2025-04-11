package com.farmer.farmermanagement.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private final String twilioAccountSid;
    private final String twilioAuthToken;
    private final String twilioPhoneNumber;

    // Constructor-based dependency injection
    public TwilioService(
        @Value("${twilio.account.sid}") String twilioAccountSid,
        @Value("${twilio.auth.token}") String twilioAuthToken,
        @Value("${twilio.phone.number}") String twilioPhoneNumber) {
        
        this.twilioAccountSid = twilioAccountSid;
        this.twilioAuthToken = twilioAuthToken;
        this.twilioPhoneNumber = twilioPhoneNumber;

        // Initialize Twilio only once during service creation
        Twilio.init(twilioAccountSid, twilioAuthToken);
    }

    public void sendOtp(String phoneNumber, String otp) {
        Message.creator(
                new com.twilio.type.PhoneNumber(phoneNumber),
                new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                "Your OTP code is: " + otp
        ).create();
    }
}
