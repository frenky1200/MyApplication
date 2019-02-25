package com.example.myapplication.data.entity;

import com.example.myapplication.data.interfaces.IMediable;

public class Film implements IMediable {

    private int id;
    private String title;
    private String author;
    private Integer idmedia;

    public Film(){}

    public Film(String title, Integer a) {
        super();
        this.title = title;
        this.author = "";
        this.idmedia = a;
    }

    //getters & setters

    @Override
    public String toString() {
        return "Film [id=" + id + ", title=" + title + ", author=" + author
                + "]";
    }


    public String getName() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    @Override
    public void setName(String name) {
        this.title = name;
    }

    public int getIdmedia(){
        return idmedia;
    }

    public void setIdmedia (int idmedia){
        this.idmedia = idmedia;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }




}
