package com.fs.auth_service.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@RestController
public class KeysController {

    private final KeyPair keyPair;

    public KeysController(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @GetMapping("/api/auth/publicKey")
    public String publicKey() {
        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
        String encoded = Base64.getEncoder().encodeToString(pub.getEncoded());
        return "-----BEGIN PUBLIC KEY-----\n" + encoded + "\n-----END PUBLIC KEY-----";
    }
}
