package ru.mrrex.betterium.utils.hash;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static byte[] getHash(Path filePath, HashAlgorithm algorithm) throws IOException, NoSuchAlgorithmException {
        return getHash(Files.readAllBytes(filePath), algorithm);
    }

    public static boolean isChecksumValid(Path filePath, HashAlgorithm algorithm, String checksum) throws IOException, NoSuchAlgorithmException {
        return toHexString(getHash(filePath, algorithm)).equals(checksum);
    }
}
