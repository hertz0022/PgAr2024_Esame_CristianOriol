package it.pgar.esame;

import java.util.ArrayList;

public class Player {
    private String name;
    private int type;
    private String role;
    private int pf;
    private ArrayList<Card> deck;
    private Card gun;

    public Player(String name, int type, int pf, ArrayList<Card> deck) {
        this.name = name;
        this.type = type;
        this.role = Utils.ROLES[type];
        this.pf   = pf;
        this.deck = deck;
        this.gun = null;
    }

    public String get_name() {
        return name;
    }

    public int get_type() {
        return type;
    }

    public String get_role() {
        return role;
    }

    public int get_pf() {
        return pf;
    }

    public ArrayList<Card> get_deck() {
        return deck;
    }

    public Card get_gun() {
        return gun;
    }

    public void set_gun(Card gun) {
        this.gun = gun;
    }

    public void add_card(Card to_add) {
        deck.add(to_add);
    }

    public Boolean has_guns() {
        for (Card card : deck)
            if (card.get_type() != 0 && card.get_type() != 1)
                return true;
        return false;
    }

    public ArrayList<Card> guns_available() {
        ArrayList<Card> guns_list = new ArrayList<>();
        for (Card card : deck)
            if (card.get_type() != 0 && card.get_type() != 1)
                guns_list.add(card);
        return guns_list;
    }

    public Boolean has_ammo() {
        for (Card card : deck)
            if (card.get_type() == 0)
                return true;
        return false;
    }

    public Boolean has_defence() {
        for (Card card : deck)
            if (card.get_type() == 1)
                return true;
        return false;
    }

    public Card get_card_by_type(int type) {
        for (Card card : deck)
            if (card.get_type() == type) {
                deck.remove(card);
                return card;
            }
        return null;
    }

    public Card get_remove_card(int index) {
        Card to_remove = deck.get(index);
        deck.remove(index);
        return to_remove;
    }

    public void damage() {
        pf = pf - 1;
    }

    public boolean is_dead() {
        if (pf <= 0)
            return true;
        return false;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n\nDati giocatore\n");
        result.append("\nPunti ferita: " + pf);
        result.append("\nRuolo: " + role);
        if (gun == null)
            result.append("\nArma equipaggiata: Colt .45");
        else
            result.append("\nArma equipaggiata: " + gun);
        result.append("\nCarte in mano: " + deck);
        result.append("\nDistanze: ");

        return result.toString();
    }


}
