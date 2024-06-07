package it.pgar.esame;

public class Card {
    private String name;
    private int type;
    private int effect;

    public Card(int type) {
        this.name = Utils.CARDS[type];
        this.type = type;
        switch (type) {
            case 0:
                this.effect = -1;
                break;
            case 1:
                this.effect = 0;
                break;
            case 2:
                this.effect = 2;
                break;
            case 3:
                this.effect = 3;
                break;
            case 4:
                this.effect = 4;
                break;
            case 5:
                this.effect = 5;
                break;
        }
    }

    public int get_type() {
        return type;
    }

    public int get_effect() {
        return effect;
    }

    public String toString() {
        return name;
    }
    
}
