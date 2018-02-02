package com.app.incidentreporter.models;

import android.net.Uri;

/**
 * Created by Mishael on 2/2/2018.
 */

public class Incident {

    private String uid;
    private String description;
    private Uri uri;
    private String location;

    public Incident(String uid, String description, Uri uri, String location) {
        this.uid = uid;
        this.description = description;
        this.uri = uri;
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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
