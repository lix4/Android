package edu.rosehulman.lix4.comicviewer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by phillee on 6/27/2017.
 */

public class ComicWrapper implements Parcelable {
    private int xkcdIssue;
    private int color;
    private Comic comic;
    private String joke;

    public ComicWrapper() {

    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    protected ComicWrapper(Parcel in) {

    }

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    public static final Creator<ComicWrapper> CREATOR = new Creator<ComicWrapper>() {
        @Override
        public ComicWrapper createFromParcel(Parcel in) {
            return new ComicWrapper(in);
        }

        @Override
        public ComicWrapper[] newArray(int size) {
            return new ComicWrapper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public int getXkcdIssue() {
        return xkcdIssue;
    }

    public void setXkcdIssue(int xkcdIssue) {
        this.xkcdIssue = xkcdIssue;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
