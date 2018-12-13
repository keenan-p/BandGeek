package com.example.android.bandgeek;

public class Event {

    private String mImageUrl;
    private String mTitle;
    private String mDate;
    private String mVenue;
    private String mTicketsUrl;

    public Event(String mImageUrl, String mTitle, String mDate, String mVenue, String mTicketsUrl) {
        this.mImageUrl = mImageUrl;
        this.mTitle = mTitle;
        this.mDate = mDate;
        this.mVenue = mVenue;
        this.mTicketsUrl = mTicketsUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getVenue() {
        return mVenue;
    }

    public void setVenue(String mVenue) {
        this.mVenue = mVenue;
    }

    public String getTicketsUrl() {
        return mTicketsUrl;
    }

    public void setTicketsUrl(String mTicketsUrl) {
        this.mTicketsUrl = mTicketsUrl;
    }
}
