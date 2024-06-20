package org.example.clientservergame;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainServer {

    int port = 3124;
    Game game = BGame.build();
    Model m = BModel.build();
    DB db = DdBuild.build();
    InetAddress ip = null;
    ArrayList<Player> allPlayers;

    void StartServer(){
        ServerSocket ss;
        Socket cs;
        int numplayer = 0;

        game.GameStart();

        try {
            ip = InetAddress.getLocalHost();
            ss = new ServerSocket(port, 0, ip);
            System.out.println("Server start\n");

            while (true){
                cs = ss.accept();
                numplayer += 1;
                System.out.println("Client connect ("+ cs.getPort() +")");
                m.addPlayer(new Player("", numplayer, PlayerState.CONNECT, 0));

                SocketClient scl = new SocketClient(cs, true, numplayer);
                m.addObserver(
                        (model) ->{
                            Resp r = new Resp(m.getAllPoints(), m.getTarget1(), m.getTarget2(), m.getAllPlayer(), m.getState());
                            scl.sendResp(r);
                        }
                );
            }
        } catch (UnknownHostException e) {
            System.out.println("Error InetAddress.getLocalHost();");
        } catch (IOException e) {
            System.out.println("Error ServerSocket(port, 0, ip);");
        }

    }


    public static void main(String[] args) {
        MainServer ms = new MainServer();
        ms.StartServer();
    }
}
