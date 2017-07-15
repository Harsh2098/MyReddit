package com.hmproductions.myreddit.utils;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.hmproductions.myreddit.data.PostContract;
import com.hmproductions.myreddit.data.PostContract.PostEntry;
import com.hmproductions.myreddit.data.RedditPost;
import com.hmproductions.myreddit.ui.MainActivity;

import java.util.List;

/**
 * Created by Harsh Mahajan on 15/7/2017.
 */

public class OfflinePostsAsyncTask extends AsyncTask<Void,Void,Void> {

    private Context mContext;

    public OfflinePostsAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected final Void doInBackground(Void... params) {

        List<RedditPost> data = MainActivity.globalList;
        for(int i=0 ; i<data.size() ; ++i) {

            RedditPost currentPost = data.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(PostEntry.COLUMN_TITLE, currentPost.getTitle());
            contentValues.put(PostEntry.COLUMN_POSTID, currentPost.getRedditID());
            contentValues.put(PostEntry.COLUMN_DATE, currentPost.getDate());
            contentValues.put(PostEntry.COLUMN_NOOFCOMMENTS, currentPost.getNumOfComments());

            mContext.getContentResolver().insert(PostContract.CONTENT_URI, contentValues);
        }

        return null;
    }
}
