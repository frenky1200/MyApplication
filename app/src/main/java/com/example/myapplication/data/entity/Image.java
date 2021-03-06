package com.example.myapplication.data.entity;

import com.example.myapplication.data.interfaces.IMediable;

public class Image implements IMediable {
    private int id;
    private String name;
    private String type;
    private  Integer idmedia;

    public Image() {
    }

    public Image(String title, Integer a) {
        super();
        this.name = title;
        this.type = "";
        this.idmedia = a;
    }

    //getters & setters

    @Override
    public String toString() {
        return id + ". " + name;
    }

    public int getIdmedia(){
        return idmedia;
    }

    public void setIdmedia (int idmedia){
        this.idmedia = idmedia;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
