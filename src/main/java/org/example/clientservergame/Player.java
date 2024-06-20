package org.example.clientservergame;

public class Player{
    String nickname;
    int shots;
    int score;
    int pos;
    PlayerState state;
    int num_of_won;

    public String getNickname() {
        return nickname;
    }

    public Player(String nickname, int pos, PlayerState state, int num_of_won) {
        this.nickname = nickname;
        this.pos = pos;
        this.state = state;
        this.num_of_won = num_of_won;
    }

    public Player(String nickname, int num_of_won) {
        this.nickname = nickname;
        this.num_of_won = num_of_won;
    }


    public int getNum_of_won() {
        return num_of_won;
    }

    public void setNum_of_won(int num_of_won) {
        this.num_of_won = num_of_won;
    }

    public void incr_won(){
        num_of_won +=1;
    }

    public PlayerState getState() {

        return state;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public Player() {
    }

    public void incrScore(){
        score +=1;
    }
    public void incrShots(){
        shots+=1;
    }

    public int getPos() {
        return pos;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickname='" + nickname + '\'' +
                '}';
    }

}
