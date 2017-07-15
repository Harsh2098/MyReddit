package com.hmproductions.myreddit.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.hmproductions.myreddit.R;
import com.hmproductions.myreddit.adapters.RedditPostsRecyclerAdapter;
import com.hmproductions.myreddit.data.PostContract;
import com.hmproductions.myreddit.data.PostContract.PostEntry;
import com.hmproductions.myreddit.data.RedditPost;

import java.util.ArrayList;
import java.util.List;

public class OfflineActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID = 101;
    RecyclerView offlineVideos_recyclerView;
    RedditPostsRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        setTitle("Offline Posts");

        if (getSupportActionBar() != null)
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        offlineVideos_recyclerView = (RecyclerView)findViewById(R.id.offlineVideos_recyclerView);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                PostEntry.COLUMN_ID,
                PostEntry.COLUMN_TITLE,
                PostEntry.COLUMN_NOOFCOMMENTS,
                PostEntry.COLUMN_DATE
        };
        
        return new CursorLoader(OfflineActivity.this, PostContract.CONTENT_URI, projection, null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        List<RedditPost> list = new ArrayList<>();

        if(cursor != null) {

            while (cursor.moveToNext())
            {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(PostEntry.COLUMN_TITLE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(PostEntry.COLUMN_DATE));
                String numOfComments = cursor.getString(cursor.getColumnIndexOrThrow(PostEntry.COLUMN_NOOFCOMMENTS));
                list.add(new RedditPost(null, title, numOfComments, date, null, null));
            }
            mAdapter = new RedditPostsRecyclerAdapter(OfflineActivity.this, list);
            offlineVideos_recyclerView.setLayoutManager(new LinearLayoutManager(this));
            offlineVideos_recyclerView.setAdapter(mAdapter);
            offlineVideos_recyclerView.setHasFixedSize(true);
        }

        else
            Toast.makeText(this,"No offline videos found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // #YOLO
    }
}
