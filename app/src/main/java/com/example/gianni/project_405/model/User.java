package com.example.gianni.project_405.model;

/**
 * Created by Frederik on 20/03/2018.
 */

public class User {

    private String id;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setnull(){id=null; name=null;}
}
