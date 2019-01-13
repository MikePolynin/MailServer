package com.itmo.air.mysql.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;

@Component
public class CodeService {

    public String encode(String string) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] enc = encoder.encode(string.getBytes());
        return Arrays.toString(enc);
    }

    public String decode(String string) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] dec = decoder.decode(string);
        return Arrays.toString(dec);
    }
}