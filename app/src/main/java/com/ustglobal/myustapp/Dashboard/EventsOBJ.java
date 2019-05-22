package com.ustglobal.myustapp.Dashboard;

import java.io.Serializable;

/**
 * Created by Shubham Singhal.
 */

public class EventsOBJ implements Serializable {

    private String Priority;
    private String Title;
    private String Subtitile;
    private String Date;
    private String ImageURL;
    private String Description;
    private String RegistrationLink;
    private String type;


    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubtitile() {
        return Subtitile;
    }

    public void setSubtitile(String subtitile) {
        Subtitile = subtitile;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRegistrationLink() {
        return RegistrationLink;
    }

    public void setRegistrationLink(String registrationLink) {
        RegistrationLink = registrationLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
