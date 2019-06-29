package com.example.project1;

import android.os.Parcel;
import android.os.Parcelable;

public class Gallery_Photo implements Parcelable {

    private int Thumbnail;

    public Gallery_Photo(String photo_Name, int thumbnail) {
        Thumbnail = thumbnail;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(Thumbnail);
    }

    public Gallery_Photo(Parcel in) {
        Thumbnail = in.readInt();
    }

    public static final Parcelable.Creator CREATOR= new Parcelable.Creator<Gallery_Photo>() {
        public Gallery_Photo createFromParcel(Parcel in) {
            return new Gallery_Photo(in);
        }

        @Override
        public Gallery_Photo[] newArray(int size) {
            return new Gallery_Photo[size];
        }
    };
}

