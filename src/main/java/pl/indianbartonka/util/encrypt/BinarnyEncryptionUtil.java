package pl.indianbartonka.util.encrypt;

public final class BinarnyEncryptionUtil {

    public static String decrypt(final String binarny) {
        final String[] binarneZnaki = binarny.split(" ");
        final StringBuilder tekst = new StringBuilder();

        for (final String bin : binarneZnaki) {
            tekst.append((char) Integer.parseInt(bin, 2));
        }

        return tekst.toString();
    }

    public static String encrypt(final String input) {
        final StringBuilder binaryOutput = new StringBuilder();

        for (final char c : input.toCharArray()) {
            binaryOutput.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0')).append(" ");
        }

        return binaryOutput.toString().trim();
    }
}