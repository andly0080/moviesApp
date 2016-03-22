package com.example.android.mymoiveapp;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidyu on 8/22/2015.
 */
public class MyMovie implements Parcelable {



    //parcel keys
    private static final String KEY_ID = "id";
    private static final String KEY_ORIGINALTITLE = "originalTitle";
    private static final String KEY_POSTERURL = "posterUrl";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_VOTEAVERAGE = "voteAverage";
    private static final String KEY_RELEASE = "release";

    private String  id;
    private String originalTitle;
    private String posterUrl;
    private String title;
    private String overview;
    private String voteAverage;

    private String release;



    public MyMovie()
    {


    }


    public MyMovie(String id, String originalTitle, String posterUrl, String title, String overview, String voteAverage, String release) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterUrl = posterUrl;
        this.title = title;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.release = release;
    }



    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }



    @Override
    public String toString() {
        return "MyMovie{" +
                "id='" + id + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", voteAverage='" + voteAverage + '\'' +
                ", release='" + release + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        //create a bundle for the key value pairs
        Bundle bundle = new Bundle();

        //inset the key value pairs to the bundle
        bundle.putString(KEY_ID,id);
        bundle.putString(KEY_ORIGINALTITLE,originalTitle);
        bundle.putString(KEY_POSTERURL,posterUrl);
        bundle.putString(KEY_TITLE,title);
        bundle.putString(KEY_OVERVIEW,overview);
        bundle.putString(KEY_VOTEAVERAGE,voteAverage);
        bundle.putString(KEY_RELEASE,release);



        //write the key value pairs to the parcel
        dest.writeBundle(bundle);

    }


    /**
     * Creator required for class implementing the parcelable interface
     *
     */

    public static final Parcelable.Creator<MyMovie> CREATOR = new Creator<MyMovie>() {
        @Override
        public MyMovie createFromParcel(Parcel source) {

            //read the bundle containing key value pairs from the parcel
            Bundle bundle = source.readBundle();

            return new MyMovie(bundle.getString(KEY_ID),bundle.getString(KEY_ORIGINALTITLE),bundle.getString(KEY_POSTERURL),bundle.getString(KEY_TITLE),bundle.getString(KEY_OVERVIEW),bundle.getString(KEY_VOTEAVERAGE),bundle.getString(KEY_RELEASE));


        }

        @Override
        public MyMovie[] newArray(int size) {
            return new MyMovie[size];
        }
    };






}
