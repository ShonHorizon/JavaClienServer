package org.example.clientservergame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainController implements IObserver{

    Model m = BModel.build();
    DB db = DdBuild.build();
    SocketClient scl = null;
    @FXML
    Pane viewPoints;
    @FXML
    Pane StatPane;
    @FXML
    TextField nickNameField;

    int port = 3124;
    InetAddress ip = null;

    @FXML
    public void initialize(){
        m.addObserver(this);
    }

    @FXML
    void MouseEvnt(MouseEvent evnt){
        if (scl != null){
            ArrayList<Point> allp = new ArrayList();
            allp.add(new Point((int) evnt.getX(), (int) evnt.getY()));
            scl.sendMsg(new Msg(allp, MsgAction.ADD));
        }
        else {
            m.add(new Point((int) evnt.getX(), (int) evnt.getY()));
        }
    }

    @FXML
    void StartGame(){
        if (scl == null) return;

        scl.sendMsg(new Msg(null, MsgAction.START));
    }
    @FXML
    void PauseGame(){
        if (scl == null) return;

        scl.sendMsg(new Msg(null, MsgAction.PAUSE));
    }
    @FXML
    void ResumeGame(){
        if (scl == null) return;

        scl.sendMsg(new Msg(null, MsgAction.RESUME));
    }

    @FXML
    void connect(){
        if (scl != null) return;
        String nickname;
        if (!nickNameField.getText().isEmpty()){
            nickname = nickNameField.getText();
        }else{
            return;
        }
        Socket cs;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("Error ip");
        }
        try {
            cs = new Socket(ip, port);
            scl = new SocketClient(cs, false);

            scl.sendMsg(new Msg(null, MsgAction.GET, nickname));
        } catch (IOException e) {
            System.out.println("Error Socet");
        }
        System.out.println("Client Connected");
    }

    @FXML
    void viewAllPlayers(){
        if(scl == null){return;}
        Scene scene = null;
        Stage stage = new Stage();
        Pane PlayersPane = new Pane();
        PlayersPane.setPrefHeight(400.0);
        PlayersPane.setPrefWidth(200.0);
        scene = new Scene(PlayersPane, 200, 400);
        stage.setTitle("Db");
        stage.setScene(scene);
        stage.show();
        Platform.runLater(
                () ->{double y = 0.0;
                    for(Player p: db.getAllPlayers()){
                        DialogPane content = new DialogPane();
                        content.setLayoutY(y);
                        y+=72.0;
                        content.prefHeight(50);
                        content.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN)));
                        content.setContentText("nickname: " + p.getNickname() + "\nscore: " + p.getNum_of_won());
                        PlayersPane.getChildren().add(content);
                    }}
        );
    }
    @Override
    public void event(Model m) {
        Platform.runLater(
                () ->{
                    viewPoints.getChildren().removeAll(viewPoints.getChildren());
                    StatPane.getChildren().removeAll(StatPane.getChildren());

                    if(m.getAllPoints() != null) {
                        for (Point p : m.getAllPoints()) {
                            Circle cr = new Circle(p.getX(), p.getY(), 5, Color.BLACK);
                            viewPoints.getChildren().add(cr);
                        }
                    }
                    if(m.getTarget1X() != -1 & m.getTarget1Y() != -1) {
                        viewPoints.getChildren().add(new Circle(m.getTarget1X(), m.getTarget1Y(), 22));
                    }
                    if(m.getTarget2X() != -1 & m.getTarget2Y() != -1) {
                        viewPoints.getChildren().add(new Circle(m.getTarget2X(), m.getTarget2Y(), 11));
                    }
                    if (m.getAllPlayer() != null) {
                        double x = 0, y = 0;
                        if(m.getAllPlayer() != null) {
                            for (Player player : m.getAllPlayer()) {
                                DialogPane content = new DialogPane();
                                content.setLayoutY(y);
                                y += 100.0;
                                content.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN)));
                                content.setContentText("shots: " + player.getShots() + "\n score: " + player.getScore() + "\n games won: " + player.getNum_of_won());
                                content.setHeaderText(player.getNickname());
                                StatPane.getChildren().add(content);
                            }
                        }
                    }
                }
        );
    }
}
