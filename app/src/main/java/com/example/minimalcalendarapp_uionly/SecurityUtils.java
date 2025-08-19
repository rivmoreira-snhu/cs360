package com.example.minimalcalendarapp_uionly;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    /**
     * Hashes a password using SHA-256.
     * This method should be used before storing or comparing passwords.
     *
     * @param password The plain-text password.
     * @return The SHA-256 hashed version of the password in hex format.
     *
     * Got the code from Stackoverflow
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available for password hashing", e);
        }
    }
}
