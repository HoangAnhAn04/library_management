package com.ntdev.library.util;

import com.ntdev.library.config.PasswordConfig;
import com.ntdev.library.enums.StatusCode;
import com.ntdev.library.exception.CustomException;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PasswordUtil {

    private final PasswordConfig passwordConfig;

    public PasswordUtil(PasswordConfig passwordConfig) {
        this.passwordConfig = passwordConfig;
    }

    public String hashPassword(String password, byte[] salt) {
        try {
            String combined = password + passwordConfig.getSecret();
            PBEKeySpec spec = new PBEKeySpec(
                    combined.toCharArray(),
                    salt,
                    passwordConfig.getIterations(),
                    passwordConfig.getKeyLength()
            );
            SecretKeyFactory factory = SecretKeyFactory.getInstance(passwordConfig.getAlgorithm());
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public String generateSaltBase64() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public byte[] decodeSaltBase64(String saltBase64) {
        return Base64.getDecoder().decode(saltBase64);
    }

    public boolean verifyPassword(String inputPassword, String storedPassword) {
        String[] parts = storedPassword.split(":");
        String saltBase64 = parts[0];
        String storedHash = parts[1];

        return hashPassword(inputPassword, decodeSaltBase64(saltBase64)).equals(storedHash);
    }

}
