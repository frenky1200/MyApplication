package com.example.myapplication.data.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.data.Film;
import com.example.myapplication.data.Music;

import java.util.LinkedList;
import java.util.List;

public class MusicHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MusicDB";

    public MusicHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_MUSIC_TABLE = "CREATE TABLE musics ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "type TEXT, "+
                "idmedia INTEGER )";

        // create books table
        db.execSQL(CREATE_MUSIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS musics");

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Books table name
    private static final String TABLE_MUSICS = "musics";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_IDMEDIA = "idmedia";

    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_TYPE,KEY_IDMEDIA};

    public void addMusic(Music music){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, music.getName()); // get title
        values.put(KEY_TYPE, music.getType()); // get author
        values.put(KEY_IDMEDIA, music.getIdmedia());
        // 3. insert
        db.insert(TABLE_MUSICS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Music getMusic(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_MUSICS, // a. table
                        COLUMNS, // b. column names
                        " idmedia = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Music music = new Music();
        music.setId(Integer.parseInt(cursor.getString(0)));
        music.setName(cursor.getString(1));
        music.setType(cursor.getString(2));
        music.setIdmedia(Integer.parseInt(cursor.getString(3)));

        Log.d("getBook("+id+")", music.toString());

        // 5. return book
        return music;
    }

    // Get All Books
    public List<Music> getAllMusic() {
        List<Music> musics = new LinkedList<Music>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MUSICS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Music music = null;
        if (cursor.moveToFirst()) {
            do {
                music = new Music();
                music.setId(Integer.parseInt(cursor.getString(0)));
                music.setName(cursor.getString(1));
                music.setType(cursor.getString(2));
                music.setIdmedia(Integer.parseInt(cursor.getString(3)));
                // Add book to books
                musics.add(music);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", music.toString());

        // return books
        return musics;
    }

    // Updating single book
    public int updateMusic(Music music) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", music.getName()); // get title
        values.put("type", music.getType()); // get author
        values.put("idmedia", music.getIdmedia());

        // 3. updating row
        int i = db.update(TABLE_MUSICS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(music.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single book
    public void deleteMusic(Music music) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_MUSICS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(music.getId()) });

        // 3. close
        db.close();

        Log.d("deleteBook", music.toString());

    }
}