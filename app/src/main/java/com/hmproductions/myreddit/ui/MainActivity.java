package com.hmproductions.myreddit.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.hmproductions.myreddit.R;
import com.hmproductions.myreddit.adapters.RedditPostsRecyclerAdapter;
import com.hmproductions.myreddit.data.RedditPost;
import com.hmproductions.myreddit.utils.OfflinePostsAsyncTask;
import com.hmproductions.myreddit.utils.RedditPostsLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<RedditPost>> {

    private static final String REDDIT_TOP_POSTS_API = "https://www.reddit.com/r/todayilearned/top.json?count=10";
    private static final int LOADER_ID = 100;

    RelativeLayout connectionErrorLayout;
    RecyclerView topPosts_recyclerView;
    ProgressBar postsProgressBar;
    TextView loading_textView;

    List<RedditPost> mMainList = new ArrayList<>();
    boolean mConnected;

    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionErrorLayout = (RelativeLayout)findViewById(R.id.connection_error_layout);
        topPosts_recyclerView = (RecyclerView)findViewById(R.id.posts_recyclerView);
        postsProgressBar = (ProgressBar)findViewById(R.id.posts_progressBar);
        loading_textView = (TextView) findViewById(R.id.loading_textView);

        layoutManager = new LinearLayoutManager(this);
        topPosts_recyclerView.setLayoutManager(layoutManager);
        topPosts_recyclerView.setAdapter(new RedditPostsRecyclerAdapter(this, mMainList));
        topPosts_recyclerView.addOnScrollListener(createInfiniteScrollListener());
        topPosts_recyclerView.setHasFixedSize(false);

        // Checks if Internet Connectivity is Available
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        mConnected = networkInfo != null && networkInfo.isConnected();

        if (mConnected) {
            topPosts_recyclerView.setVisibility(View.VISIBLE);
            connectionErrorLayout.setVisibility(View.GONE);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
        else
        {
            topPosts_recyclerView.setVisibility(View.GONE);
            connectionErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<RedditPost>> onCreateLoader(int id, Bundle args) {
        postsProgressBar.setVisibility(View.VISIBLE);
        loading_textView.setVisibility(View.VISIBLE);
        return new RedditPostsLoader(MainActivity.this, REDDIT_TOP_POSTS_API);
    }

    @Override
    public void onLoadFinished(Loader<List<RedditPost>> loader, List<RedditPost> data) {

        postsProgressBar.setVisibility(View.GONE);
        loading_textView.setVisibility(View.GONE);

        OfflinePostsAsyncTask saveAsyncTask = new OfflinePostsAsyncTask(MainActivity.this, data);
        saveAsyncTask.execute();

        mMainList.addAll(data);
        topPosts_recyclerView.setAdapter(new RedditPostsRecyclerAdapter(this, mMainList));
    }

    @Override
    public void onLoaderReset(Loader<List<RedditPost>> loader) {
        // #YOLO
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!mConnected) menu.findItem(R.id.search_post_item).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.offline_posts_item)
        {
            startActivity(new Intent(MainActivity.this, OfflineActivity.class));
        } else if (item.getItemId() == R.id.search_post_item) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(24, layoutManager) {
            @Override
            public void onScrolledToEnd(int firstVisibleItemPosition) {
                getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                refreshView(topPosts_recyclerView, new RedditPostsRecyclerAdapter(MainActivity.this, mMainList), firstVisibleItemPosition);
            }
        };
    }
}
