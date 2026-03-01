package com.aqeel.tomatoguess.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password encryption using salt and SHA-256 hasing.
 *
 * @author Aqeel Jabir
 */
public class PasswordEncryptor {

    /**
     * Generates a random salt.
     *
     * @return the generated salt as a Base64-encoded string.
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes the password using SHA-256 and the provided salt.
     *
     * @param password to be hashed.
     * @param salt     used for hasing.
     * @return the hashed password as a Base64-encoded string.
     */
    public static String hashPassword(String password, String salt) {
        String saltedPassword = salt + password;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(saltedPassword.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
