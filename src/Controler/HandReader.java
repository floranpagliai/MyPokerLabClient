package Controler;


import Model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controler in MyPokerLab
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 15/05/15 at 23:08
 */

public class HandReader implements Runnable {
    ArrayList<Hand> hands = new ArrayList<Hand>();
    final File folder = new File("/Users/floran/Documents/PokerTracker 4/Processed/test/");

    @Override
    public void run() {
        try {
            for (final File fileEntry : folder.listFiles()) {
                BufferedReader reader = new BufferedReader(new FileReader(fileEntry));
                String line;
                String hand = "";
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Winamax Poker") && line.contains("CashGame") && !hand.equals("")) {
                        hands.add(this.getHand(hand));
                        hand = "";
                    }
                    hand += line + "\n";
                }
                if (hand.contains("Winamax Poker") && hand.contains("CashGame") && !hand.equals(""))
                    hands.add(this.getHand(hand));
                reader.close();
            }
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", "test");
            e.printStackTrace();
        }
    }

    public Hand getHand(String handStr) throws ParseException {
        Hand hand = new Hand();
        StringTokenizer st = new StringTokenizer(handStr, "\n");

//        System.out.println(handStr);

        String str = "";
        Boolean isHeader = true;
        eRound round = null;
        while (st.hasMoreElements()) {
            str = st.nextElement().toString();
            round = this.getRound(str, round);
            if (round == null) {
                if (str.contains("HandId:")) {
                    hand.setRoomHandId(this.getNextElement(str, "HandId:"));
                    hand.setTimestamp(this.getDate(str));
                    hand.setStake(this.getStake(str));
                } else if (str.contains("Table:")) {
                    hand.setButtonSeatPos(Character.getNumericValue(str.charAt(str.lastIndexOf("#") + 1)));
                    hand.setTableName(str.substring(str.indexOf("'") + 1, str.lastIndexOf("'")));
                } else if (str.contains("Seat") && !str.contains("Table:")) {
                    Integer pos = Character.getNumericValue(this.getNextElement(str, "Seat").charAt(0));
                    hand.addPlayer(pos, this.getPlayer(str));
                }
            } else {
                if (str.contains("Rake")) {
                    System.out.print("");
                } else if (str.contains("Board:")) {
                    hand.setBoard(this.getCards(str));
                } else {
                    Action action = this.getAction(str, round, hand);
                    if (action != null)
                        hand.addAction(action);
                }
            }

        }
//        System.out.println(hand);
        return hand;
    }

    public String getNextElement(String str, String contains, int elementToSkip) {
        StringTokenizer st = new StringTokenizer(str);

        while (st.hasMoreElements()) {
            String str2 = st.nextElement().toString();
            if (str2.contains(contains)) {
                for (int i = 0 ; i != elementToSkip ; i++)
                    st.nextElement();
                str = st.nextElement().toString();
            }
        }
        return str;
    }

    public String getNextElement(String str, String contains) {
        return this.getNextElement(str, contains, 0);
    }

    public Date getDate(String str) throws ParseException {
        String date = str.substring(str.lastIndexOf("-") + 2, str.length());
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
        return format.parse(date);
    }

    public Player getPlayer(String str) {
        Player player = new Player();

        player.setName(str.substring(str.indexOf(":") + 2, str.lastIndexOf(" ")));
        player.setStack(Double.valueOf(str.substring(str.lastIndexOf(" ") + 2, str.lastIndexOf(")") - 1)));
        return player;
    }

    public String getStake(String str) {
        String stake = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")"));
        stake = stake.substring(stake.indexOf("/") + 1);

        return stake;
    }

    public eRound getRound(String str, eRound round) {

        if (str.contains("*** ANTE/BLINDS ***")) {
            round = eRound.antebinds;
        } else if (str.contains("*** PRE-FLOP *** ")) {
            round = eRound.preflop;
        } else if (str.contains("*** FLOP ***")) {
            round = eRound.flop;
        } else if (str.contains("*** TURN ***")) {
            round = eRound.turn;
        } else if (str.contains("*** RIVER ***")) {
            round = eRound.river;
        } else if (str.contains("*** FLOP ***")) {
            round = eRound.showdown;
        }
        return round;
    }

    public Action getAction(String str, eRound round, Hand hand) {
        Action action = new Action();

        action.setRound(round);
        if (str.contains("posts")) {
            action.setAction(eAction.posts);
            action.setPlayer(new Player(str.substring(0, str.indexOf("posts")-1)));
            action.setAmount(this.getAmount(str, "blind"));
        } else if (str.contains("Dealt")) {
            action.setAction(eAction.dealt);
            action.setPlayer(hand.findPlayer(str.substring(str.indexOf("to")+3, str.indexOf("[")-1)));
            action.setCards(this.getCards(str));
        } else if (str.contains("bets")) {
            action.setAction(eAction.bets);
            action.setPlayer(hand.findPlayer(str.substring(0, str.indexOf("bets") - 1)));
            action.setAmount(this.getAmount(str, "bets"));
        } else if (str.contains("calls")) {
            action.setAction(eAction.calls);
            action.setPlayer(hand.findPlayer(str.substring(0, str.indexOf("calls") - 1)));
            action.setAmount(this.getAmount(str, "calls"));
        } else if (str.contains("raises")) {
            action.setAction(eAction.raises);
            action.setPlayer(hand.findPlayer(str.substring(0, str.indexOf("raises") - 1)));
            action.setAmount(this.getAmount(str, "raises", 2));
        } else if (str.contains("folds")) {
            action.setAction(eAction.folds);
            action.setPlayer(hand.findPlayer(str.substring(0, str.indexOf("folds") - 1)));
        } else if (str.contains("shows")) {
            action.setAction(eAction.shows);
            action.setPlayer(hand.findPlayer(str.substring(0, str.indexOf("shows") - 1)));
            action.setCards(this.getCards(str));
        } else if (str.contains("collected")) {
            action.setAction(eAction.collected);
            action.setPlayer(hand.findPlayer(str.substring(0, str.indexOf("collected") - 1)));
            action.setAmount(this.getAmount(str, "collected"));
        } else {
            return null;
        }
        action.update();
        return action;
    }

    public String[] getCards(String str) {
        return str.substring(str.indexOf("[") + 1, str.lastIndexOf("]")).split(" ");
    }
      
    public Double getAmount(String str, String beforeWord, int toSkip) {
        String amount = this.getNextElement(str, beforeWord, toSkip);
        if (true)//Cash game
            amount = amount.substring(0, amount.indexOf("€"));
        return Double.valueOf(amount);
    }

    public Double getAmount(String str, String beforeWord) {
        return this.getAmount(str, beforeWord, 0);
    }
}
