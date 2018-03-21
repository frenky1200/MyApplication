package com.example.myapplication.data.entity;

import com.example.myapplication.data.interfaces.IMediable;

public class Media {
    private int id;
    private String name;
    private String type;
    private String tags;
    private String album;

    public Media(){

    }

    public Media(String names, String types, String albums) {
        super();
        this.name = names;
        this.type = types;
        this.tags = "";
        this.album = albums;
    }

    //getters & setters
    @SuppressWarnings("unchecked")
    public <T extends IMediable> T mediaType (){
        switch (type){
            case "Book" : return (T) new Book();
            case "Anime" : return (T) new Anime();
            case "Music" : return (T) new Music();
            case "Exception" : return (T) new Film();
            default: return (T) new Media();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTags() {
        return tags;
    }

    public String getAlbum(){
        return album;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setAlbum(String album){
        this.album = album;
    }

}