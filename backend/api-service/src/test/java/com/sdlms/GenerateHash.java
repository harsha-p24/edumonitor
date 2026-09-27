package com.sdlms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        String hash = encoder.encode(rawPassword);
        System.out.println("Raw: " + rawPassword);
        System.out.println("Hash: " + hash);
    }
}
