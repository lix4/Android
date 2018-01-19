package edu.rosehulman.lix4.photobucket;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by phillee on 7/18/2017.
 */

public class Pic implements Parcelable {
    private String caption;
    private String imageUrl;
    private String uid;
    private String key;

    public Pic() {

    }


    public Pic(String caption, String imageUrl, String uid) {
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.uid = uid;
    }

    protected Pic(Parcel in) {
        caption = in.readString();
        imageUrl = in.readString();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static final Creator<Pic> CREATOR = new Creator<Pic>() {
        @Override
        public Pic createFromParcel(Parcel in) {
            return new Pic(in);
        }

        @Override
        public Pic[] newArray(int size) {
            return new Pic[size];
        }
    };

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(imageUrl);
    }

    public void setValues(Pic pic) {
        setCaption(pic.getCaption());
        setImageUrl(pic.getImageUrl());
    }
}
