package com.example.tms;

import java.io.Serializable;

public class Fio implements Serializable {
    private long id;
    private String fio;

    public Fio(long id, String fio){
        this.id = id;
        this.fio=fio;
    }

    public long getId (){
        return id;
    }

    public String getFio (){
        return fio;
    }
}
