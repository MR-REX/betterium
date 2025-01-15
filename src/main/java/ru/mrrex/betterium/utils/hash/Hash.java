package ru.mrrex.betterium.utils.hash;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public static String toHexString(byte[] bytes) {
        BigInteger number = new BigInteger(1, bytes);
        StringBuilder stringBuilder = new StringBuilder(number.toString(16));

        while (stringBuilder.length() < 64)
            stringBuilder.insert(0, '0');

        return stringBuilder.toString();
    }

    public static byte[] getHash(byte[] bytes, HashAlgorithm algorithm) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm.getName());
        return messageDigest.digest(bytes);
    }

    public static byte[] getHash(String text, HashAlgorithm algorithm) throws NoSuchAlgorithmException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return getHash(bytes, algorithm);
    }
}
