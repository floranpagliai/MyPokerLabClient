package com.Model;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement(name = "hand")
@XmlSeeAlso({Player.class})
@XmlType(propOrder = {"roomName", "roomHandId", "timestamp", "tableName", "stake", "buttonSeatPos", "players", "actions", "board"})
public class Hand {
    private eRoom roomName;
    private String roomHandId;
    private Date timestamp;
    private String tableName;
    private String stake;
    private Map<Integer, Player> players = new HashMap<Integer, Player>();
    private Integer buttonSeatPos;
    private ArrayList<Action> actions = new ArrayList<Action>();
    private String[] board;

    public eRoom getRoomName() {
        return roomName;
    }

    public void setRoomName(eRoom roomName) {
        this.roomName = roomName;
    }

    public String getRoomHandId() {
        return roomHandId;
    }

    public void setRoomHandId(String roomHandId) {
        this.roomHandId = roomHandId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getStake() {
        return stake;
    }

    public void setStake(String stake) {
        this.stake = stake;
    }

    @XmlElementWrapper(name="seats")
    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, Player> players) {
        this.players = players;
    }

    public void addPlayer (Integer pos, Player player_) {
        this.players.put(pos, player_);
    }

    public Player findPlayer(String name) {
        for (Player player : players.values()) {
            if (player.getName().equals(name))
                return player;
        }
        return null;
    }

    public Integer getButtonSeatPos() {
        return buttonSeatPos;
    }

    public void setButtonSeatPos(Integer buttonSeatPos) {
        this.buttonSeatPos = buttonSeatPos;
    }

    @XmlElementWrapper(name="actions")
    @XmlElements({@XmlElement(name="action", type=Action.class)})
    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    @XmlElementWrapper(name="board")
    @XmlElement(name="card")
    public String[] getBoard() {
        return board;
    }

    public void setBoard(String[] board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "roomName=" + roomName +
                ", roomHandId='" + roomHandId + '\'' +
                ", timestamp=" + timestamp +
                ", tableName='" + tableName + '\'' +
                ", stake='" + stake + '\'' +
                ", players=" + players +
                ", buttonSeatPos=" + buttonSeatPos +
                ", actions=" + actions +
                ", board=" + Arrays.toString(board) +
                '}';
    }
}
