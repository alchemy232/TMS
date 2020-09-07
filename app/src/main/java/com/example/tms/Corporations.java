package com.example.tms;

public class Corporations {
    private long id;
    private String corporation;

    public Corporations(long id, String corporation){
        this.id=id;
        this.corporation=corporation;
    }

    public long getId() {
        return id;
    }

    public String getCorporation() {
        return corporation;
    }
}
