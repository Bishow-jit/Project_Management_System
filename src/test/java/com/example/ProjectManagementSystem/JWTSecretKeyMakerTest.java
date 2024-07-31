package com.example.ProjectManagementSystem;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JWTSecretKeyMakerTest {

    @Test
    public void generateKey(){
        SecretKey secretKey = Jwts.SIG.HS256.key().build();
        String encodedKey = DatatypeConverter.printHexBinary(secretKey.getEncoded());
        System.out.println("encodedKey->"+encodedKey);
    }
}

