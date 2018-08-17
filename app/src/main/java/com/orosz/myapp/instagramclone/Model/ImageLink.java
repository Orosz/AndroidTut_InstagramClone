package com.orosz.myapp.instagramclone.Model;

public class ImageLink {

    private String Url;
    private String Data;
    private String UserID;

    public ImageLink() {
    }

    public ImageLink(String userID, String url, String data) {
        Url = url;
        Data = data;
        UserID = userID;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    @Override
    public String toString() {
        return this.Url;
    }
}
