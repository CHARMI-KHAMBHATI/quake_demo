package com.example.android.quakereport;

/**
 * Created by ADMIN on 5/31/2017.
 */
public class Earthquake {
    private String  mLocation, mDate, mURL;
    private  double mMagnitude;


    public Earthquake(double magnitude, String location, String date, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mDate = date;
        mURL=url;
    }

    public String getURL() {
        return mURL;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public void setMagnitude(double magnitude) {
        mMagnitude = magnitude;
    }
}
