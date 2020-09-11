package com.example.tms;

public class Corporations {
    public static String[] corp = {"Credicor", "Ecoline", "Helion", "Mining Guild", "Interplanetary Cinematics", "Inventrix", "Phobolog", "Tharsis Republic", "Thorgate", "United Nations Mars Initiative", "Teractor", "Saturn Systems"};
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
