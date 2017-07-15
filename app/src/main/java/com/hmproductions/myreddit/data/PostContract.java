package com.hmproductions.myreddit.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Harsh Mahajan on 15/7/2017.
 */

public class PostContract {

    static final String CONTENT_AUTHORITY = "com.hmproductions.myreddit";
    static final String PATH_CAPTION = "post";

    private static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_CAPTION);

    public class PostEntry implements BaseColumns
    {
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_POSTID = "postId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NOOFCOMMENTS = "noOfComments";
        public static final String COLUMN_DATE = "date";

        static final String TABLE_NAME = "post";
    }
}
