package com.challenge.shaadiapp.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "USER_INFO";

    // Table columns
    public static final String _ID = "_id";
    public static final String USERNAME = "user_name";
    public static final String DATE = "date";
    public static final String DESC = "description";
    public static final String PROFILEPIC = "picture";
    public static final String STATUS = "status";

    // Database Information
    static final String DB_NAME = "SHAADI_TEST.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    /*private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME + " TEXT NOT NULL, " + DESC + " TEXT);";*/

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " (" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME + " TEXT NOT NULL, " + DESC + " TEXT, "+ DATE + " TEXT, "
            +PROFILEPIC + " TEXT, " + STATUS+" TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}