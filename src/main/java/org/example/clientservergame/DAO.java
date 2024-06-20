package org.example.clientservergame;

import javafx.scene.effect.Light;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DAO implements Iterable<Point>{

    Set<Point> allPoints = Collections.newSetFromMap(new ConcurrentHashMap<>());
    ArrayList<Player> allPlayer = new ArrayList<>();

    public void clear(){
        allPoints.clear();
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    GameState state;

    public void setAllPoints(Set<Point> allPoints) {
        this.allPoints = allPoints;
    }

    public ArrayList<Player> getAllPlayer() {
        return allPlayer;
    }

    public void removePlayer(Player player){
        allPlayer.remove(player);
    }

    public void addPlayer(Player player){
        allPlayer.add(player);
    }

    public void setAllPlayer(ArrayList<Player> allPlayer) {
        this.allPlayer = allPlayer;
    }

    void add(Point p){
        allPoints.add(p);
    }

    void remove(Point p){
        allPoints.remove(p);
    }

    void set(Set<Point> allPoints){
        this.allPoints = allPoints;
    }

    public Set<Point> getAllPoints() {
        return allPoints;
    }

    @Override
    public Iterator<Point> iterator() {
        return allPoints.iterator();
    }
}
