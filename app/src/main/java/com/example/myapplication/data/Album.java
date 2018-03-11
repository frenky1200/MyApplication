package com.example.myapplication.data;

import com.example.myapplication.data.interfaces.IMediable;

/**
 * Created by Андрей on 11.05.2017.
 */

public class Album implements IMediable {
    private int id;
    private String name;
    private String type;

    public Album(){}

    public Album(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }
    @Override
    public String toString() {
        return name ;
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

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
