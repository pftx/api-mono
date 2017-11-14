package com.x.api.auth.service;

import java.security.SecureRandom;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthServiceTest {

    @Test
    public void testGenerateP() throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12, SecureRandom.getInstanceStrong());
        System.out.println(passwordEncoder.encode("user"));
    }

}
