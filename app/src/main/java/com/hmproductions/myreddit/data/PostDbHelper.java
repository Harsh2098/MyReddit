package com.hmproductions.myreddit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hmproductions.myreddit.data.PostContract.PostEntry;

/**
 * Created by Harsh Mahajan on 15/7/2017.
 */

public class PostDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "post";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + PostContract.PostEntry.TABLE_NAME + " (" +
                    PostEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PostEntry.COLUMN_POSTID + " TEXT NOT NULL," +
                    PostEntry.COLUMN_TITLE + " TEXT," +
                    PostEntry.COLUMN_DATE + " TEXT, " +
                    PostEntry.COLUMN_NOOFCOMMENTS + " TEXT, " +
                    "UNIQUE (" + PostEntry.COLUMN_POSTID + ") ON CONFLICT REPLACE);";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + PostEntry.TABLE_NAME;


    public PostDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
