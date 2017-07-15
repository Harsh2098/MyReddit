package com.hmproductions.myreddit.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.hmproductions.myreddit.data.RedditPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Harsh Mahajan on 15/7/2017.
 */

public class RedditPostsLoader extends AsyncTaskLoader<List<RedditPost>> {

    private static final String LOG_TAG = RedditPostsLoader.class.getSimpleName();
    private static String after = null;
    private String mStringURL;

    public RedditPostsLoader(Context context, String url) {

        super(context);
        mStringURL = url;

        if (after != null)
            mStringURL = mStringURL + "&after=" + after;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<RedditPost> loadInBackground() {
        String jsonString = "";
        URL url = null;

        try {
            url = createURL(mStringURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (url != null) {
            try {
                jsonString = makeHttpConnection(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!jsonString.equals("")) {
            return extractDetailsFromJSON(jsonString);
        } else return null;
    }

    private List<RedditPost> extractDetailsFromJSON(String jsonString) {
        List<RedditPost> mData = new ArrayList<>();
        String thumbnailURL, title, noOfComments, date, contentURL, id;
        long dateInMillis;

        try {
            JSONObject root = new JSONObject(jsonString);
            JSONObject data = root.getJSONObject("data");

            after = data.getString("after");
            JSONArray children = data.getJSONArray("children");

            for(int position=0 ; position<children.length() ; ++position)
            {
                JSONObject currentPost = children.getJSONObject(position);
                JSONObject childData = currentPost.getJSONObject("data");

                thumbnailURL = childData.getString("thumbnail");
                title = childData.getString("title");
                id = childData.getString("id");

                dateInMillis = childData.getLong("created");
                date = new SimpleDateFormat("dd-MMM hh:mm a", Locale.US).format(new Date(dateInMillis * 1000));

                noOfComments = String.valueOf(childData.getInt("num_comments"));
                contentURL = childData.getString("url");

                mData.add(new RedditPost(thumbnailURL, title, noOfComments, date, contentURL, id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mData;
    }

    private URL createURL(String stringURL) throws IOException {
        try {
            return new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.d(LOG_TAG, "Malformed URL");
            return null;
        }
    }

    private String makeHttpConnection(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null)
            return jsonResponse;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            jsonResponse = readFromInputStream(inputStream);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());

        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = bufferedReader.readLine();

            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }

        return builder.toString();
    }
}
