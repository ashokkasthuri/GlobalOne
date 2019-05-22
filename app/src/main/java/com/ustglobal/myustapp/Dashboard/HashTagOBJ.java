package com.ustglobal.myustapp.Dashboard;

import java.io.Serializable;

/**
 * Created by Shubham Singhal.
 */

public class HashTagOBJ implements Serializable {

    private String created_at;
    private String id;
    private String text;


    public HashTagOBJ(){
    }

    public HashTagOBJ(String id){
        id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private static int lastContactId = 0;

}
