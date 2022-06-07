package edu.app.gridpuzzlegame.models;

public class Game {
    private String game_id;
    private String user_id;
    private String date;
    private String result;
    private String win;

    public  Game(String user_id, String date, String result, String win) {
        this.user_id = user_id;
        this.date = date;
        this.result = result;
        this.win = win;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }
}
