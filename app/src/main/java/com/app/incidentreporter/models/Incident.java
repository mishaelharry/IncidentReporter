package com.app.incidentreporter.models;

import android.net.Uri;

/**
 * Created by Mishael on 2/2/2018.
 */

public class Incident {

    private String uid;
    private String description;
    private String image;
    private String location;

    public Incident() {
    }

    public Incident(String uid, String description, String image, String location) {
        this.uid = uid;
        this.description = description;
        this.image = image;
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
