package org.example.clientservergame;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ConcurrentModificationException;
import java.util.Objects;

public class SocketClient {

    Socket cs;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;
    Gson gson = new Gson();
    boolean isServer = true;
    Model m = BModel.build();
    Game game = BGame.build();
    DB db = DdBuild.build();
    int player;

    public SocketClient(Socket cs, boolean isServer, int player) {
        this.cs = cs;
        this.isServer = isServer;
        this.player = player;

        try {
            os = cs.getOutputStream();
            dos = new DataOutputStream(os);
        } catch (IOException e) {
            System.out.println("Error cs.getOutputStream()");
        }

        new Thread(this::run).start();
    }

    public SocketClient(Socket cs, boolean isServer) {
        this.cs = cs;
        this.isServer = isServer;

        try {
            os = cs.getOutputStream();
            dos = new DataOutputStream(os);
        } catch (IOException e) {
            System.out.println("Error cs.getOutputStream()");
        }

        new Thread(this::run).start();
    }

    void run(){
        try {
            is = cs.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            System.out.println("Error cs.getInputStream()");
        }

        while(true){
            if(isServer){
                Msg msg = readMsg();
                switch (msg.getAction()){
                    case ADD -> {
                        for(Point p: msg.getPoints()){
                            for(Player pl:m.getAllPlayer()){
                                if (pl.getPos() == player){
                                    pl.incrShots();
                                }
                            }
                            p.setOwner(player);
                            m.add(p);
                        }
                    }
                    case GET -> {
                        boolean unique = true;
                        db.addPlayer(msg.getNickname());
                        for(Player p: m.getAllPlayer()){
                            if (Objects.equals(msg.getNickname(), p.getNickname())){
                                unique = false;
                            }
                        }
                        if(unique){
                            for (Player p : m.getAllPlayer()) {
                                if (p.getPos() == player) {
                                    p.setNickname(msg.getNickname());
                                    for (Player p_db : db.getAllPlayers()) {
                                        if (Objects.equals(p.getNickname(), p_db.getNickname())) {
                                            p.setNum_of_won(p_db.getNum_of_won());
                                            break;
                                        }
                                    }
                                }
                            }
                        }else {
                            sendResp(new Resp(true));
                            Thread.currentThread().interrupt();
                        }
                        Resp r = new Resp(m.getAllPoints(), m.getTarget1(), m.getTarget2(), m.getAllPlayer(), m.getState());
                        sendResp(r);
                    }
                    case START -> {
                        try {
                            for(Player pl:m.getAllPlayer()){
                                if (pl.getPos() == player){
                                    pl.setState(PlayerState.READY);
                                }
                                if(Objects.equals(pl.getNickname(), "")){
                                    m.removePlayer(pl);
                                }
                            }
                        }catch (ConcurrentModificationException e){
                            System.out.println("e");
                        }

                    }
                    case PAUSE -> {
                        game.pause(player);
                    }
                    case RESUME -> {
                        game.resume(player);
                    }
                }
            }
            else{
                Resp r = readResp();
                m.set(r.getPoints());
                m.setTarget1(r.getTarget1());
                m.setTarget2(r.getTarget2());
                m.setAllPlayer(r.getAllPlayer());
                m.setState(r.getState());
            }



        }
    }

    public void sendMsg(Msg msg){
        if(!Thread.interrupted()){
            String strMsg = gson.toJson(msg);
            try {
                dos.writeUTF(strMsg);
            } catch (IOException e) {
                System.out.println("Send Error msg");
            }
        }

    }

    public void sendResp(Resp resp){
        if(!Thread.interrupted()){
            String strResp = gson.toJson(resp);
            try {
                dos.writeUTF(strResp);
            } catch (IOException e) {
                System.out.println("Send Error resp");
            }
        }
    }
    public Resp readResp(){
        if(!Thread.interrupted()){
            Resp r = null;
            try {
                String respStr = dis.readUTF();
                r = gson.fromJson(respStr, Resp.class);
            } catch (IOException e) {
                System.out.println("Error read resp");
            }
            return r;
        }else {
            return null;
        }
    }

    public Msg readMsg(){
        if(!Thread.interrupted()){
            Msg msg = null;
            try {
                String respStr = dis.readUTF();
                msg = gson.fromJson(respStr, Msg.class);
            } catch (IOException e) {
                System.out.println("Error read msg");
            }
            return msg;
        }else {
            return null;
        }
    }
}
