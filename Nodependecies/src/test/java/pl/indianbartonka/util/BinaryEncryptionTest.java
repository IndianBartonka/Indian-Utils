package pl.indianbartonka.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.encrypt.BinarnyEncryptionUtil;

public class BinaryEncryptionTest {

    @Test
    public void encrypt() {
        Assertions.assertEquals("01011010 01100001 01110011 01111010 01111001 01100110 01110010 01101111 01110111 01100001 01101110 01111001 00100000 01110100 01100101 01101011 01110011 01110100", BinarnyEncryptionUtil.encrypt("Zaszyfrowany tekst"));
    }

    @Test
    public void decrypt() {
        Assertions.assertEquals("Zaszyfrowany tekst", BinarnyEncryptionUtil.decrypt("01011010 01100001 01110011 01111010 01111001 01100110 01110010 01101111 01110111 01100001 01101110 01111001 00100000 01110100 01100101 01101011 01110011 01110100"));

    }
}
