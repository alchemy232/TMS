package com.example.tms;

import java.io.Serializable;

public class Games implements Serializable {
    public static Integer[] possiblePlayers = new Integer [] {2,3,4,5};
    private long id;
    private String date;
    private int gameNum;
    private int points;
    private int numOfPlayers;
    private  String fio;
    private String corp;

    public Games (long id, String date, int gameNum, int points, int numOfPlayers, String fio, String corp){
        this.id = id;
        this.date=date;
        this.gameNum = gameNum;
        this.points=points;
        this.numOfPlayers=numOfPlayers;
        this.fio= fio;
        this.corp = corp;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getGameNum() {
        return gameNum;
    }

    public int getPoints() {
        return points;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public String getFio() {
        return fio;
    }

    public String getCorp() {
        return corp;
    }
}
