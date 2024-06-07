package it.pgar.esame;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int num_players = Utils.welcome();
        Match match = Match.init_match(num_players);

        match.main_action();

        Utils.print_animation(Constants.BYE);
    }

}
