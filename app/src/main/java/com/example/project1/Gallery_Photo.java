package com.example.project1;

public class Gallery_Photo {

    private String Photo_Name;
    private int Thumbnail;

    public Gallery_Photo() {
    }

    public Gallery_Photo(String photo_Name, int thumbnail) {
        Photo_Name = photo_Name;
        Thumbnail = thumbnail;
    }

    public String getPhoto_Name() {
        return Photo_Name;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setPhoto_Name(String photo_Name) {
        Photo_Name = photo_Name;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }
}
