package org.example.clientservergame;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DB {
    Connection c;

    void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(
                    "jdbc:sqlite:A:\\project\\ClientServerGame\\Database.db"
            );
        }catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public DB() {
        connect();
    }

    public void addPlayer(String nickname){
        PreparedStatement pst = null;
        ArrayList<Player> allPlayers = getAllPlayers();
        for (Player p: allPlayers){
            if (Objects.equals(p.getNickname(), nickname)){
                return;
            }
        }
        try {
            pst = c.prepareStatement("INSERT INTO players VALUES(?,?,0)");
            pst.setString(2, nickname);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert error" + e);
        }
    }

    public ArrayList<Player> getAllPlayers(){
        Statement st = null;
        ArrayList<Player> allPlayers = new ArrayList<>();

        try {
            st = c.createStatement();
            ResultSet r = st.executeQuery(
                    "select * from players");
            while(r.next()){
                Player p = new Player(r.getString("nickname"), r.getInt("num_of_won"));
                allPlayers.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return allPlayers;
    }

    public void UpdateScore(Player p){
        PreparedStatement pst = null;

        try {
            pst = c.prepareStatement("UPDATE players SET num_of_won = "+ p.getNum_of_won() + " WHERE nickname = \"" + p.getNickname() + "\";");
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert error" + e);
        }
    }

}
