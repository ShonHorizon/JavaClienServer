package org.example.clientservergame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Model implements Iterable<Point>{

    DAO dao = new DAO();
    ArrayList<IObserver> allO = new ArrayList<>();
    Point target1 = new Point(470, 166);
    Point target2 = new Point(600, 166);

    public GameState getState(){
        return dao.getState();
    }

    public void setState(GameState state){
        dao.setState(state);
        event();
    }

    public Point getTarget1() {
        return target1;
    }

    public Point getTarget2() {
        return target2;
    }

    void event(){
        for (IObserver o: allO){
            o.event(this);
        }
    }


    public ArrayList<Player> getAllPlayer() {
        return dao.getAllPlayer();
    }


    public void addPlayer(Player p){
        dao.addPlayer(p);
        event();
    }
    public void removePlayer(Player p){
        dao.removePlayer(p);
        event();
    }

    public void setAllPlayer(ArrayList<Player> allPlayers){
        dao.setAllPlayer(allPlayers);
        event();
    }
    public int getTarget1X() {
        if(target1 == null){return -1;}
        return target1.getX();
    }

    public int getTarget1Y() {
        if(target1 == null){return -1;}
        return target1.getY();
    }

    public int getTarget2X() {
        if(target2 == null){return -1;}
        return target2.getX();
    }

    public int getTarget2Y() {
        if(target2 == null){return -1;}
        return target2.getY();
    }

    public void setTarget1(Point target1) {
        this.target1 = target1;
        event();
    }

    public void setTarget2(Point target2) {
        this.target2 = target2;
        event();
    }

    public void addObserver(IObserver o){
        allO.add(o);
    }

    @Override
    public Iterator<Point> iterator() {
        return dao.iterator();
    }
    public void clear(){
        dao.clear();
        event();
    }

    public void add(Point p){
        dao.add(p);
        event();
    }

    public void remove(Point p){
        dao.remove(p);
        event();
    }

    void set(Set<Point> allPoint){
        dao.set(allPoint);
        event();
    }

    public Set<Point> getAllPoints(){
        return dao.getAllPoints();
    }
}
