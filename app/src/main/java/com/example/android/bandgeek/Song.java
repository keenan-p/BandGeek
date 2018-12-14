package com.example.android.bandgeek;

public class Song {

    private String mTitle;
    private String mArtist;
    private String mImageUrl;
    private String mRank;
    private String mSongUrl;

    public Song(String mTitle, String mArtist, String mImageUrl, String mRank, String mSongUrl) {
        this.mTitle = mTitle;
        this.mArtist = mArtist;
        this.mImageUrl = mImageUrl;
        this.mRank = mRank;
        this.mSongUrl = mSongUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getRank() {
        return mRank;
    }

    public void setRank(String mRank) {
        this.mRank = mRank;
    }

    public String getSongUrl() {
        return mSongUrl;
    }

    public void setSongUrl(String mSongUrl) {
        this.mSongUrl = mSongUrl;
    }
}
