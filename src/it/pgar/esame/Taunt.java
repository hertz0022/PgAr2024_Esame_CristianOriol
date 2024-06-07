package it.pgar.esame;

import java.util.LinkedList;
import java.util.List;

import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.Menu;

public class Taunt {

    private static String encrypt(String clear_word, String key) {
        StringBuilder encrypted = new StringBuilder();
        int keyLength = key.length();

        for (int i = 0; i < clear_word.length(); i++) {
            char char_to_encrypt = clear_word.charAt(i);
            char key_char = key.charAt(i % keyLength);
            char encrypted_char = (char) (((char_to_encrypt - key_char + 26) % 26) + 'a');
            encrypted.append(encrypted_char);
        }

        return encrypted.toString();
    }

    public static void provoke(Match match, Player current) throws InterruptedException {
        List<Player> players_list = new LinkedList<>(match.players);
        
        Menu.clearConsole();
        System.out.println(Constants.CHOOSE_PROVOKE);
        for (int i = 0; i < players_list.size() - 1; i++)  {
            System.out.println(i + ". " + players_list.get(i).get_name());
        }
        int choice = InputData.readIntegerBetween(Constants.CHOOSE_PROVOKE, 1, players_list.size());

        int string_num = InputData.readIntegerWithMinimum(Constants.ASK_NUM_STRINGS, 1);
        StringBuilder encrypted_mess = new StringBuilder();
        for (int i = 0; i < string_num; i++) {
            String clear = InputData.readNonEmptyString(Constants.ASK_CLEAR_STRING + i + ": ", true);
            String key  = InputData.readNonEmptyString(Constants.ASK_KEY + clear + ": ", true);
            encrypted_mess.append(encrypt(clear, key) + " ");
        }
        
        Utils.print_animation(players_list.get(choice - 1).get_name() + Constants.SEND_MSG + current.get_name() + "\n" + encrypted_mess.toString());
        String go_on = InputData.readString(Constants.GO_ON, true);
        Menu.clearConsole();
    }
}
