import java.util.Scanner;

public class BinarnyEncryptionUtil {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj kod binarny (np. 01000001 01101000): ");
        String binarny = scanner.nextLine();

        String tekst = binarnyNaTekst(binarny);
        System.out.println("Odszyfrowany tekst: " + tekst);
    }

    public static String binarnyNaTekst(String binarny) {
        String[] binarneZnaki = binarny.split(" ");
        StringBuilder tekst = new StringBuilder();

        for (String bin : binarneZnaki) {
            int znak = Integer.parseInt(bin, 2);
            tekst.append((char) znak);
        }

        return tekst.toString();
    }
    
    public static String (){
            StringBuilder binaryOutput = new StringBuilder();

        for (char c : input.toCharArray()) {
            String binaryChar = String.format("%8s", Integer.toBinaryString(c))
                                      .replace(' ', '0'); // Dopisuje zera z przodu
            binaryOutput.append(binaryChar).append(" ");
        }

        System.out.println("Kod binarny: " + binaryOutput.toString().trim());
   
   }
}
