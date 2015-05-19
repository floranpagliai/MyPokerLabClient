package Model;

import java.util.Arrays;

public class Action {
    private Player player;
    private eAction action;
    private eRound round;

    //optional
    private Double amount;
    private String[] cards;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public eAction getAction() {
        return action;
    }

    public void setAction(eAction action) {
        this.action = action;
    }

    public eRound getRound() {
        return round;
    }

    public void setRound(eRound round) {
        this.round = round;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String[] getCards() {
        return cards;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
    }

    public void update() {
        if (this.amount != null && player != null) {
            if (this.action == eAction.collected)
                this.player.setStack(this.player.getStack() + this.amount);
            else
                this.player.setStack(this.player.getStack() - this.amount);
        }
    }

    @Override
    public String toString() {
        return "Action{" +
                "player=" + player +
                ", action=" + action +
                ", round=" + round +
                ", amount=" + amount +
                ", cards=" + Arrays.toString(cards) +
                '}';
    }
}
