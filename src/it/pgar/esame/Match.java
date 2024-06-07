package it.pgar.esame;

import java.util.ArrayList;
import java.util.Collections;
//import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.Menu;
import it.kibo.fp.lib.RandomDraws;

public class Match {
    public Queue<Player> players;
    private Stack<Card> match_deck;
    private ArrayList<Card> discarded_deck;

    public Match(Queue<Player> players, Stack<Card> match_deck) {
        this.players = players;
        this.match_deck = match_deck;
        this.discarded_deck = new ArrayList<>();
    }

    public static Match init_match(int num_players) throws InterruptedException {
        final int INIT_PF = 4;

        Queue<Player> players = new LinkedList<>();
        Stack<Card> match_deck = init_card_draw();

        Utils.print_flush(Constants.WHO_SHERIFF, 2000);
        ArrayList<Integer> extracted = player_type_draw(num_players); 

        String player_name = InputData.readNonEmptyString(Constants.ASK_SHERIFF, true);
        Menu.clearConsole();
        int type = 0;
        ArrayList<Card> player_deck = new ArrayList<>();
        int cards_extracted = 0;
        for (int i = 0; i < INIT_PF + 1; i++)
            player_deck.add(match_deck.pop());
        Player sheriff  = new Player(player_name, type, INIT_PF + 1, player_deck);
        players.add(sheriff);

        int count = 2;
        do {
            player_name = InputData.readNonEmptyString(Constants.ASK_NAME + count + ": ", true);
            type = RandomDraws.drawInteger(0, extracted.size() - 1);
            
            player_deck.clear();
            cards_extracted = 0;
            do {
                player_deck.add(match_deck.pop());
                cards_extracted++;
            } while (cards_extracted < INIT_PF);

            Player player = new Player(player_name, extracted.get(type), INIT_PF, player_deck);
            players.add(player);
            count++;
        } while (players.size() < num_players);

        Menu.clearConsole();
        Utils.print_flush(Constants.MATCH_CREATED, 2000);

        return new Match(players, match_deck);
    }

    public static ArrayList<Integer> player_type_draw(int num_players) {
        ArrayList<Integer> extracted = new ArrayList<>();
        extracted.add(3);
        switch (num_players) {
            case 4:
                extracted.add(1);
                extracted.add(1);
                break;
            case 5:
                extracted.add(1);
                extracted.add(1);
                extracted.add(2);
                break;
            case 6:
                extracted.add(1);
                extracted.add(1);
                extracted.add(1);
                extracted.add(2);
                break;
            case 7:
                extracted.add(1);
                extracted.add(1);
                extracted.add(1);
                extracted.add(2);
                extracted.add(2);
                break;
        }
        Collections.shuffle(extracted);
        return extracted;
    }

    public static Stack<Card> init_card_draw() {
        Stack<Card> match_deck = new Stack<>();

        for (int i = 0; i < 50; i++)
            match_deck.push(new Card(0));

        for (int i = 0; i < 24; i++)
            match_deck.push(new Card(1));
        
        for (int i = 0; i < 3; i++)
            match_deck.push(new Card(2));

        match_deck.push(new Card(3));
        match_deck.push(new Card(4));
        match_deck.push(new Card(5));

        Collections.shuffle(match_deck);
        return match_deck;
    }

    private int find_distance(int pos, List<Player> temp_list) {
        return (Math.abs(pos - temp_list.size() + temp_list.size()) % temp_list.size()) + 1;
    }

    private void print_player_dist(Player current) {
        //Iterator<Player> iterator = players.iterator();
        int count = 1;
        int distance = 1;
        /*while (iterator.hasNext()) {
            if (iterator.next() == current)
                break;
            else if (count == players.size() - 2)
                count = 1;
            System.out.println(count + ". Distanza da " + iterator.next() + ": " + count);
            count++;
        }*/
        List<Player> temp_list = new LinkedList<>(players);
        for (int i = 0; i < temp_list.size() - 1; i++) {
            distance = find_distance(i, temp_list);
            System.out.println("Distanza da " + temp_list.get(i).get_name() + ": " + distance);
            count++;
        }
    }

    private void refill_deck_if_empty() {
        if (match_deck.isEmpty()) {
            Collections.shuffle(discarded_deck);
            match_deck.addAll(discarded_deck);
        }
    }

    private Boolean check_alive(int type)  {
        List<Player> temp_list = new LinkedList<>(players);
        for (int i = 0; i < temp_list.size(); i++)
            if (temp_list.get(i).get_type() == type)
                return true;
        return false;
    }

    private Boolean check_match_fin(Player dead_player) throws InterruptedException {
        if (dead_player.get_type() == 0 && check_alive(3)) {
            Utils.print_flush(Constants.WIN_RENEGADE, 2000);
            return true;
        }
        else if (dead_player.get_type() == 0 && !check_alive(3)) {
            Utils.print_flush(Constants.WIN_OUTLAWS, 2000);
            return true;
        }
        else if (!check_alive(3) && !check_alive(1)) {
            Utils.print_flush(Constants.WIN_SHERIFF, 2000);
            return true;
        }

        return false;
    }

    private Boolean check_dead(Player current, Player aimed_player) throws InterruptedException  {
        if (aimed_player.is_dead()) {
            players.remove(aimed_player);
            Utils.print_flush(aimed_player.get_name() + Constants.DEATH, 2000);
            if (check_match_fin(aimed_player))
                return true;
            if (current.get_type() == 0 && aimed_player.get_type() == 2) {
                for (Card card : current.get_deck()) {
                    discarded_deck.add(card);
                    current.get_deck().remove(card);
                }
                if (current.get_gun() != null) {
                    discarded_deck.add(current.get_gun());
                    current.set_gun(null);
                }
                Utils.print_flush(Constants.PENALITY, 2000);

            }
            else if (aimed_player.get_type() == 1) {
                for (int i = 0; i < 3; i++) {
                    refill_deck_if_empty();
                    current.add_card(match_deck.pop());
                }
                Utils.print_flush(Constants.OUTLAW_KILL + aimed_player.get_name(), 2000);
            }
        }
        return false;
    }

    private Boolean shoot(Player current) throws InterruptedException {
        Menu.clearConsole();
        boolean penality = false;
        boolean reward = false;
        discarded_deck.add(current.get_card_by_type(0));
        List<Player> players_list = new LinkedList<>(players);

        print_player_dist(current);
        int choice = InputData.readIntegerBetween(Constants.AIM + current.get_name() + "? ", 1, players.size());
        Player aimed_player = players_list.get(choice - 1);

        if ((current.get_gun() == null && find_distance(choice - 1, players_list) > 1) || (current.get_gun() != null && current.get_gun().get_effect() < find_distance(choice - 1, players_list)))
            Utils.print_flush(Constants.TOO_DISTANT, 2000);

        else if (!aimed_player.has_defence()) {
            aimed_player.damage();
            Utils.print_flush(Constants.HIT_NO_DEFENCE, 2000);
        }

        else {
            boolean defence = InputData.readYesOrNo(aimed_player.get_name() + Constants.ASK_DEFENCE);
            if (defence) {
                discarded_deck.add(aimed_player.get_card_by_type(1));
                Utils.print_flush(Constants.DEFENCED, 2000);
            }
            else {
                aimed_player.damage();
                Utils.print_flush(aimed_player.get_name() + Constants.HIT, 2000);
            }
        }
        return check_dead(current, aimed_player);
    }

    private void set_player_gun(Player current) throws InterruptedException {
        Menu.clearConsole();
        ArrayList<Card> guns_list = current.guns_available();
        for (int i = 0; i < guns_list.size(); i++)
            System.out.println(i + "# arma " + guns_list.get(i));
        int choice = InputData.readIntegerBetween(Constants.EQUIP_GUN, 1, guns_list.size());
        discarded_deck.add(current.get_gun());
        current.set_gun(guns_list.get(choice));
        Utils.print_flush(current.get_gun() + Constants.GUN_EQUIPPED, 2000);
    }

    private void fin_turn(Player current) throws InterruptedException {
        Menu.clearConsole();
        if (current.get_deck().size() > current.get_pf()) {
            int to_discard = current.get_deck().size() - current.get_pf();
            System.out.println(Constants.TO_DISCARD + current.get_deck());
            for (int i = 0; i < to_discard; i++) {
                int choice = InputData.readIntegerBetween(Constants.ASK_WHICH_DISCARD + i + " : ", 1, current.get_deck().size());
                discarded_deck.add(current.get_remove_card(choice - 1));
            }
        }
        else
            Utils.print_flush(Constants.NO_DISCARD, 2000);
    }

    private int choose(Player current, Boolean shot) throws InterruptedException {
        int choice;
        do {
            choice = InputData.readIntegerBetween(Constants.CHOOSE, 1, 3);
            if (choice == 1 && shot)
                Utils.print_animation(Constants.ALREADY_SHOT);
            else if (choice == 1 && !current.has_ammo())
                Utils.print_animation(Constants.NO_AMMO);
            else if (choice == 2 && !current.has_guns())
                Utils.print_animation(Constants.NO_GUNS_LEFT);
        } while ((choice == 1 && shot || !current.has_ammo()) || (choice == 2 && !current.has_guns()));
        return choice;
    }

    public void main_action() throws InterruptedException {
        boolean fin = false;
        do {
            Player current = players.poll();
            players.offer(current);
            Utils.print_animation(Constants.CURRENT_TURN + current.get_name());
            if (current.get_type() == 0)
                Utils.print_animation(Constants.SHERIFF_TURN);
            else
                Utils.print_animation(Constants.WARNING + current.get_name());
            Menu.wait(2000);
            for (int i = 0; i < 2; i++) {
                refill_deck_if_empty();
                current.add_card(match_deck.pop());
            }
            //Utils.print_animation(Constants.INIT_PICK);

            int choice;
            boolean shot = false;

            do {
                System.out.println(current);
                print_player_dist(current);
                System.out.println(Constants.FIRST_OPTION);
                System.out.println(Constants.SECOND_OPTION);
                System.out.println(Constants.THIRD_OPTION);
                System.out.println(Constants.FOURTH_OPTION);

                choice = choose(current, shot);

                switch (choice) {
                    case 1:
                        shot = true;
                        fin = shoot(current);
                        break;
                    case 2:
                        set_player_gun(current);
                        break;
                    case 3:
                        Taunt.provoke(this, current);
                        break;
                    default:
                        fin_turn(current);
                        break;
                }
            } while (choice != 4 && !fin);
        } while (!fin);
    }
}
