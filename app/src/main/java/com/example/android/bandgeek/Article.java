package com.example.android.bandgeek;

public class Article {

    private String mThumbnailUrl;
    private String mHeadline;
    private String mDescription;
    private String mArticleUrl;

    public Article (String mThumbnailUrl, String mHeadline, String mDescription, String mArticleUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
        this.mHeadline = mHeadline;
        this.mDescription = mDescription;
        this.mArticleUrl = mArticleUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String mThumbnailUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public void setHeadline(String mHeadline) {
        this.mHeadline = mHeadline;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getArticleUrl() {
        return mArticleUrl;
    }

    public void setArticleUrl(String mArticleUrl) {
        this.mArticleUrl = mArticleUrl;
    }
}
