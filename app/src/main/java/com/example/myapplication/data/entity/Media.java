package com.example.myapplication.data.entity;

import com.example.myapplication.data.interfaces.IMediable;

public class Media {
    private int id;
    private String name = "default";
    private String type;
    private String tags;
    private int album;
    private String insideUri;
    private String outsideUri;

    public Media(){

    }

    public Media(String names, String types, int albums) {
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
            case "Film" : return (T) new Film();
            case "Anime" : return (T) new Anime();
            case "Music" : return (T) new Music();
            case "Image" : return (T) new Image();
            case "Excerption" : return (T) new Excerption();
            default: return (T) new Media();
        }
    }

    @Override
    public String toString() {
        return album + " - " + name;
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

    public int getAlbum(){
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

    public void setAlbum(int album){
        this.album = album;
    }

    public String getInsideUri() {
        return insideUri;
    }

    public void setInsideUri(String insideUri) {
        this.insideUri = insideUri;
    }

    public String getOutsideUri() {
        return outsideUri;
    }

    public void setOutsideUri(String outsideUri) {
        this.outsideUri = outsideUri;
    }
}
