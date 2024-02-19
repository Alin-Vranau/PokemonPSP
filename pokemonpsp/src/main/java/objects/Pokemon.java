package objects;

import java.util.ArrayList;

public class Pokemon {
    private String name;
    private int health;
    private int actualHealth;
    private ArrayList<Move> moves;

    public Pokemon(String name, int health, ArrayList<Move> moves) {
        this.name = name;
        this.health = health;
        this.actualHealth = health;
        this.moves = moves;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getActualHealth() {
        return actualHealth;
    }

    public void setActualHealth(int actualHealth) {
        if (actualHealth < 0) {
            this.actualHealth = 0;
        } else {
            this.actualHealth = actualHealth;
        }
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }

}
