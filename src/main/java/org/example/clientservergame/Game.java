package org.example.clientservergame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    Model m = BModel.build();
    int speed1 = 1;
    int speed2 = 2;
    Set<Point> point_to_set;
    DB db = DdBuild.build();

    void GameStart(){
        m.setState(GameState.STOP);
        new Thread(this::run).start();
    }

    void run(){
        while (true) {
            if (m.getState() == GameState.STOP) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Thread error");
                }
                int i = 0;
                if (!m.getAllPlayer().isEmpty()) {
                    for (Player p : m.getAllPlayer()) {
                        if (p.getState() == PlayerState.READY) i++;
                    }
                    if (i == m.getAllPlayer().size()) {
                        m.setState(GameState.START);
                    }
                }
            }
            else if (m.getState() == GameState.PAUSE) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Pause Error");
                    }
                    m.setState(GameState.START);
                }
            }
            else if (m.getState() == GameState.START) {
                point_to_set = Collections.newSetFromMap(new ConcurrentHashMap<>());
                start:
                for (Point p : m.getAllPoints()) {
                    if (p.getX() < 650 - 5) {
                        if (ishit(p, m.getTarget1(), 22)) {
                            for (Player player : m.getAllPlayer()) {
                                if (player.getPos() == p.getOwner()) {
                                    player.incrScore();
                                    if(player.getScore() >6){
                                        player.incr_won();
                                        stop();
                                        break start;
                                    }
                                }
                            }
                            continue;
                        }
                        if (ishit(p, m.getTarget2(), 11)) {
                            for (Player player : m.getAllPlayer()) {
                                if (player.getPos() == p.getOwner()) {
                                    player.incrScore();
                                    if(player.getScore() > 6){
                                        player.incr_won();
                                        stop();
                                        break start;
                                    }
                                }
                            }
                            continue;
                        }
                        point_to_set.add(new Point(p.getX() + 1, p.getY(), p.getOwner()));
                    }
                }
                m.set(point_to_set);
                m.setTarget2(new Point(m.getTarget2X(), m.getTarget2Y() + speed2));
                m.setTarget1(new Point(m.getTarget1X(), m.getTarget1Y() + speed1));

                if (m.getTarget1Y() + speed1 + 22> 332) {
                    speed1 = -1;
                }
                if (m.getTarget1Y() + speed1 - 22 < 0) {
                    speed1 = 1;
                }
                if (m.getTarget2Y() + speed2 + 11 > 332) {
                    speed2 = -1;
                }
                if (m.getTarget2Y() + speed2 - 11 < 0) {
                    speed2 = 1;
                }


                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Error");
                }
            }
        }
    }
    int playerInitiatePause = -1;
    void resume(int initiate){
        if(playerInitiatePause == initiate & m.getState() == GameState.PAUSE){
            synchronized (this) {
                this.notifyAll();
            }
        }
    }
    void pause(int initiate){
        if(m.getState() == GameState.START) {
            playerInitiatePause = initiate;
            m.setState(GameState.PAUSE);
        }
    }
    void stop(){
        m.setState(GameState.STOP);
        for(Player p:m.getAllPlayer()){
            db.UpdateScore(p);
            p.setState(PlayerState.CONNECT);
            p.setScore(0);
            p.setShots(0);
        }
        m.clear();
        m.setTarget1(new Point(470, 166));
        m.setTarget2(new Point(600, 166));
        point_to_set.clear();
        speed1 = 1;
        speed2 = 2;
    }

    boolean ishit(Point bullet, Point target, int target_rd) {
        if (((bullet.getX() > target.getX() - target_rd) & (bullet.getX() < target.getX() + target_rd))) {
            if ((bullet.getY() > target.getY() - target_rd) & (bullet.getY() < target.getY() + target_rd)) {
                return true;
            }
        }
        return false;
    }
}
