package org.example.clientservergame;

import java.util.ArrayList;

public class Msg {

    ArrayList<Point> points;

    MsgAction action;

    String nickname;


    public Msg(ArrayList<Point> points, MsgAction action) {
        this.points = points;
        this.action = action;
    }

    public Msg(ArrayList<Point> points, MsgAction action, String nickname) {
        this.points = points;
        this.action = action;
        this.nickname = nickname;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public MsgAction getAction() {
        return action;
    }

    public String getNickname() {
        if(!nickname.isEmpty()) {
            return nickname;
        }else {
            return "";
        }
    }

    @Override
    public String toString() {
        return "Msg{" +
                "points=" + points +
                ", action=" + action +
                '}';
    }
}
