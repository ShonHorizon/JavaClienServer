package org.example.clientservergame;

import java.util.ArrayList;
import java.util.Set;

public class Resp {
    Set<Point> points;
    Point target1;
    Point target2;
    ArrayList<Player> allPlayer;
    GameState state;
    boolean closeGame = false;

    public Resp(Set<Point> points, Point target1, Point target2, ArrayList<Player> allPlayer, GameState state) {
        this.points = points;
        this.target1 = target1;
        this.target2 = target2;
        this.allPlayer = allPlayer;
        this.state = state;
    }

    public Resp(boolean closeGame) {
        this.closeGame = closeGame;
    }

    public GameState getState() {
        return state;
    }

    public Resp() {
    }

    public ArrayList<Player> getAllPlayer() {
        return allPlayer;
    }

    public void setAllPlayer(ArrayList<Player> allPlayer) {
        this.allPlayer = allPlayer;
    }

    public Point getTarget1() {
        return target1;
    }

    public Point getTarget2() {
        return target2;
    }

    public Set<Point> getPoints() {
        return points;
    }
}
