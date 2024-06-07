package it.pgar.esame;

import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.Menu;

public class Utils {
    public static String[] ROLES = {"Sheriff", "Fuorilegge", "Vice", "Rinnegato"};
    public static String[] CARDS = {"BANG!", "Mancato!", "Schoﬁeld", "Remington", "Rev. Carabine", "Winchester"};

    public static void print_animation(String msg) throws InterruptedException {
        final int DELAY = 40;

        for (int i = 0; i < msg.length(); i++) {
            System.out.print(msg.charAt(i));
            Menu.wait(DELAY);
        }
    }

    public static void wait_flush(int time) throws InterruptedException {
        Menu.wait(time);
        Menu.clearConsole();
    }

    public static void print_flush(String msg, int milliseconds) throws InterruptedException {
        print_animation(msg);
        wait_flush(milliseconds);
    }

    public static int welcome() throws InterruptedException {

        print_flush(Constants.WELCOME, 2000);

        int num_players = InputData.readIntegerBetween(Constants.ASK_N_PLAYERS, 4, 7);
        Menu.clearConsole();
        return num_players;
    }
}
