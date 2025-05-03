package me.moderatorman.arpasim.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordUtil
{
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 256;
    private static final int ITERATIONS = 65536;
    private static final SecureRandom random = new SecureRandom();

    public static String hash(String password)
    {
        try
        {
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return null;
        }
    }

    public static boolean checkPassword(String password, String hashedPassword)
    {
        try
        {
            String[] parts = hashedPassword.split("\\$");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] testHash = factory.generateSecret(spec).getEncoded();

            return java.util.Arrays.equals(hash, testHash);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return false;
        }
    }
}