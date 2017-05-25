package pl.edu.tirex.blazingsquad.web.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils
{
    public static String md5(String password) throws NoSuchAlgorithmException
    {
        return hash("MD5", password);
    }

    public static String hash(String hash, String password) throws NoSuchAlgorithmException
    {
        MessageDigest md5 = MessageDigest.getInstance(hash);
        md5.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md5.digest();
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }
}
