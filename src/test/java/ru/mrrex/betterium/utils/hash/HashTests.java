package ru.mrrex.betterium.utils.hash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

public class HashTests {

    @Test
    public void getHash() throws NoSuchAlgorithmException {
        String text = "Hello, Betterium!";
        String expectedHashString = "2572799f81148b78e428b1c86055d5788bbbb09c0548279767930ce619c40a85";

        byte[] bytes = Hash.getHash(text, HashAlgorithm.SHA256);
        String hashString = Hash.toHexString(bytes);

        assertEquals(hashString, expectedHashString);
    }
}
