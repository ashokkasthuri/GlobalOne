package com.ustglobal.myustapp.Dashboard;

import java.io.Serializable;

/**
 * Created by Shubham Singhal.
 */

public class LinkedInProfileOBJ implements Serializable {

    private int id;
    private String name;
    private String email_id;
    private String picture_url;
    private String linkedin_id;
    private String public_profile;

    public LinkedInProfileOBJ() {

    }

    public LinkedInProfileOBJ(int id, String name, String email_id,String picture_url,String linkedin_id,String public_profile) {
        this.id = id;
        this.name = name;
        this.email_id = email_id;
        this.picture_url = picture_url;
        this.linkedin_id = linkedin_id;
        this.public_profile = public_profile;

    }

    public LinkedInProfileOBJ(String name, String email_id,String picture_url,String linkedin_id,String public_profile) {
        this.name = name;
        this.email_id = email_id;
        this.picture_url = picture_url;
        this.linkedin_id = linkedin_id;
        this.public_profile = public_profile;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getLinkedin_id() {
        return linkedin_id;
    }

    public void setLinkedin_id(String linkedin_id) {
        this.linkedin_id = linkedin_id;
    }

    public String getPublic_profile() {
        return public_profile;
    }

    public void setPublic_profile(String public_profile) {
        this.public_profile = public_profile;
    }
}
