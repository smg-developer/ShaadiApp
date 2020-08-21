package com.challenge.shaadiapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.challenge.shaadiapp.modal.UserInfo;

public class DatabaseOpenHelper {
     private DatabaseHelper dbHelper;

    private Context context;

        private SQLiteDatabase database;

        public DatabaseOpenHelper(Context c) {
            context = c;
        }

        public DatabaseOpenHelper open() throws SQLException {
            dbHelper = new DatabaseHelper(context);
            database = dbHelper.getWritableDatabase();
            return this;
        }

        public void close() {
            dbHelper.close();
        }

        public void insert(UserInfo userInfo) {

            //Currently checking it with name as no unique id available in response of random user api.
            if(!checkIfUserInserted(userInfo.u_name)) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(DatabaseHelper.USERNAME, userInfo.u_name);
                contentValue.put(DatabaseHelper.DESC, userInfo.description);
                contentValue.put(DatabaseHelper.DATE, userInfo.date);
                contentValue.put(DatabaseHelper.PROFILEPIC, userInfo.profile_pic);
                contentValue.put(DatabaseHelper.STATUS, userInfo.status);
                database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
            }
        }

        public Cursor fetchInfo() {
            String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.USERNAME, DatabaseHelper.DESC, DatabaseHelper.DATE,
                    DatabaseHelper.PROFILEPIC, DatabaseHelper.STATUS };
            Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, "_id DESC", "10");

            return cursor;
        }

    public boolean checkIfUserInserted(String userName) {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.USERNAME, DatabaseHelper.DESC, DatabaseHelper.DATE,
                DatabaseHelper.PROFILEPIC, DatabaseHelper.STATUS };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, DatabaseHelper.USERNAME + " = '"+userName.trim()+"'", null, null, null, null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
                return true;
        }
        return false;
    }

        public int updateStatus(String name, String status) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.STATUS, status);
            int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.USERNAME + " = '"+name.trim()+"'", null);
            return i;
        }

}
