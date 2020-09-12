package com.example.tms;
// класс описания корпорации настольной игры
// задел на перевод наименований корпораций в базу данных с описанием их достоинств и недостатков
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
