package com.example.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.item.DownloadList;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MyBook";

    // book download table name
    private static final String TABLE_NAME_DOWNLOAD = "book_download";

    // book Table Columns names
    private static final String ID = "auto_id";
    private static final String KEY_BOOK_ID = "book_id";
    private static final String KEY_BOOK_TITLE = "book_title";
    private static final String KEY_BOOK_IMAGE = "image";
    private static final String KEY_BOOK_FILE_URL = "book_file_url";
    private static final String KEY_BOOK_AUTHOR_NAME = "book_author_name";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_DOWNLOAD = "CREATE TABLE " + TABLE_NAME_DOWNLOAD + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_BOOK_ID + " TEXT,"
                + KEY_BOOK_TITLE + " TEXT," + KEY_BOOK_IMAGE + " TEXT,"
                + KEY_BOOK_AUTHOR_NAME + "" + " TEXT," + KEY_BOOK_FILE_URL + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_DOWNLOAD);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DOWNLOAD);
        onCreate(db);
    }

    //-------------Download Table-------------------//

    // Adding new book detail
    public void addDownload(DownloadList downloadList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BOOK_ID, downloadList.getId());
        values.put(KEY_BOOK_TITLE, downloadList.getTitle());
        values.put(KEY_BOOK_IMAGE, downloadList.getImage());
        values.put(KEY_BOOK_AUTHOR_NAME, downloadList.getAuthor());
        values.put(KEY_BOOK_FILE_URL, downloadList.getUrl());

        // Inserting Row
        db.insert(TABLE_NAME_DOWNLOAD, null, values);
        db.close(); // Closing database connection
    }

    // Getting All book
    public List<DownloadList> getDownload() {
        List<DownloadList> downloadLists = new ArrayList<DownloadList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_DOWNLOAD;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DownloadList list = new DownloadList();
                list.setId(cursor.getString(1));
                list.setTitle(cursor.getString(2));
                list.setImage(cursor.getString(3));
                list.setAuthor(cursor.getString(4));
                list.setUrl(cursor.getString(5));

                // Adding book to list
                downloadLists.add(list);
            } while (cursor.moveToNext());
        }

        // return book list
        return downloadLists;
    }

    // Deleting single book
    public void deleteDownloadBook(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_DOWNLOAD, KEY_BOOK_ID + "=" + id, null);
    }

    //check book id in database or not
    public boolean checkIdDownloadBook(String id) {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_DOWNLOAD + " WHERE " + KEY_BOOK_ID + "=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor.getCount() == 0;
    }

    //-------------Download Table-------------------//


}
