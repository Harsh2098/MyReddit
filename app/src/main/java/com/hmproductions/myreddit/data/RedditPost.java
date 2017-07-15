package com.hmproductions.myreddit.data;

/**
 * Created by Harsh Mahajan on 15/7/2017.
 */

public class RedditPost {

    private String mThumbnailURL, mTitle, mNumOfComments, mDate, mContentURL;
    private String mRedditID;

    public RedditPost(String thumbnailURL, String title, String numOfComments, String date, String contentURL, String redditID) {
        mThumbnailURL = thumbnailURL;
        mTitle = title;
        mNumOfComments = numOfComments;
        mDate = date;
        mContentURL = contentURL;
        mRedditID = redditID;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getNumOfComments() {
        return mNumOfComments;
    }

    public String getDate() {
        return mDate;
    }

    public String getContentURL() {
        return mContentURL;
    }

    public String getRedditID() {
        return mRedditID;
    }
}
