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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hmproductions.myreddit.R;
import com.hmproductions.myreddit.adapters.RedditPostsRecyclerAdapter;
import com.hmproductions.myreddit.data.PostContract;
import com.hmproductions.myreddit.data.RedditPost;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 102;
    Button searchButton;
    EditText search_editText;
    ProgressBar searchProgressBar;

    RedditPostsRecyclerAdapter mAdapter;
    RecyclerView searchedPost_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        search_editText = (EditText) findViewById(R.id.search_editText);
        searchButton = (Button) findViewById(R.id.search_button);
        searchProgressBar = (ProgressBar) findViewById(R.id.search_progressBar);

        searchedPost_recyclerView = (RecyclerView) findViewById(R.id.searchedPost_recyclerView);

        SearchButtonClickListener();
    }

    private void SearchButtonClickListener() {

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(search_editText.getText().toString().isEmpty() || search_editText.getText().toString().equals(""))) {
                    getSupportLoaderManager().restartLoader(LOADER_ID, null, SearchActivity.this);
                }

            }
        });
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
                PostContract.PostEntry.COLUMN_ID,
                PostContract.PostEntry.COLUMN_TITLE,
                PostContract.PostEntry.COLUMN_DATE,
                PostContract.PostEntry.COLUMN_NOOFCOMMENTS,
                PostContract.PostEntry.COLUMN_THUMBNAIL_URL
        };
        return new CursorLoader(this, PostContract.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<RedditPost> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndexOrThrow(PostContract.PostEntry.COLUMN_TITLE)).contains(search_editText.getText().toString())) {
                    list.add(new RedditPost(
                            cursor.getString(cursor.getColumnIndexOrThrow(PostContract.PostEntry.COLUMN_THUMBNAIL_URL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(PostContract.PostEntry.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(PostContract.PostEntry.COLUMN_NOOFCOMMENTS)),
                            cursor.getString(cursor.getColumnIndexOrThrow(PostContract.PostEntry.COLUMN_DATE)),
                            null, null));
                }
            }
        }

        mAdapter = new RedditPostsRecyclerAdapter(this, list);
        searchedPost_recyclerView.setAdapter(mAdapter);
        searchedPost_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchedPost_recyclerView.setHasFixedSize(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // #YOLO
    }
}
