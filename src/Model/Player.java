package Model;

/**
 * Model in MyPokerLab
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 15/05/15 at 22:41
 */

public class Player {
    private String name;
    private double stack;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStack() {
        return stack;
    }

    public void setStack(double stack) {
        this.stack = stack;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", stack=" + stack +
                '}';
    }
}
