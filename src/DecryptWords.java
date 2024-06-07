import java.util.Scanner;

public class DecryptWords {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Leggi il numero di casi di test
        int N = sc.nextInt();
        sc.nextLine(); // Consume newline

        // Leggi tutte le parole cifrate e le chiavi
        String[] encryptedWords = new String[N];
        String[] keys = new String[N];

        for (int i = 0; i < N; i++) {
            encryptedWords[i] = sc.next();
        }

        for (int i = 0; i < N; i++) {
            keys[i] = sc.next();
        }

        // Decifra ogni parola usando la chiave corrispondente
        for (int i = 0; i < N; i++) {
            String decryptedWord = decrypt(encryptedWords[i], keys[i]);
            System.out.println(decryptedWord);
        }

        sc.close();
    }

    // Funzione di decifrazione
    private static String decrypt(String encryptedWord, String key) {
        StringBuilder decrypted = new StringBuilder();
        int keyLength = key.length();

        for (int i = 0; i < encryptedWord.length(); i++) {
            char encryptedChar = encryptedWord.charAt(i);
            char keyChar = key.charAt(i % keyLength);
            // Decifra il carattere
            char decryptedChar = (char) (((encryptedChar - keyChar + 26) % 26) + 'a');
            decrypted.append(decryptedChar);
        }

        return decrypted.toString();
    }
}
