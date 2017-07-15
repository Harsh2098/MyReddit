package com.hmproductions.myreddit.utils;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hmproductions.myreddit.data.PostContract;
import com.hmproductions.myreddit.data.PostContract.PostEntry;
import com.hmproductions.myreddit.data.RedditPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harsh Mahajan on 15/7/2017.
 */

public class OfflinePostsAsyncTask extends AsyncTask<Void,Void,Void> {

    private Context mContext;
    private List<RedditPost> mList = new ArrayList<>();

    public OfflinePostsAsyncTask(Context context, List<RedditPost> list) {
        mContext = context;
        mList = list;
    }

    @Override
    protected final Void doInBackground(Void... params) {

        for (int i = 0; i < mList.size(); ++i) {

            RedditPost currentPost = mList.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PostEntry.COLUMN_TITLE, currentPost.getTitle());
            contentValues.put(PostEntry.COLUMN_POSTID, currentPost.getRedditID());
            contentValues.put(PostEntry.COLUMN_DATE, currentPost.getDate());
            contentValues.put(PostEntry.COLUMN_NOOFCOMMENTS, currentPost.getNumOfComments());

            Log.v(":::", currentPost.getTitle());

            mContext.getContentResolver().insert(PostContract.CONTENT_URI, contentValues);
        }

        return null;
    }
}
