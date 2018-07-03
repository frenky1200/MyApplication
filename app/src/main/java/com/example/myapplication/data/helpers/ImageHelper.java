package com.example.myapplication.data.helpers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.data.entity.Image;

import java.util.LinkedList;
import java.util.List;

public class ImageHelper extends SQLiteOpenHelper{

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MediaDB.sqlite";

    public ImageHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_ANIME_TABLE = "CREATE TABLE images ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "author TEXT, "+
                "idmedia INTEGER )";

        // create books table
        db.execSQL(CREATE_ANIME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS images");

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Books table name
    private static final String TABLE_IMAGES = "images";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "title";
    private static final String KEY_TYPE = "author";
    private static final String KEY_IDMEDIA = "idmedia";

    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_TYPE,KEY_IDMEDIA};

    public void addImage(Image image){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, image.getName()); // get title
        values.put(KEY_TYPE, image.getType()); // get author
        values.put(KEY_IDMEDIA, image.getIdmedia());

        // 3. insert
        db.insert(TABLE_IMAGES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Image getImage(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_IMAGES, // a. table
                        COLUMNS, // b. column names
                        " idmedia = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        Image image = new Image();
        if (cursor != null) {
            cursor.moveToFirst();
            image.setId(Integer.parseInt(cursor.getString(0)));
            image.setName(cursor.getString(1));
            image.setType(cursor.getString(2));
            image.setIdmedia(Integer.parseInt(cursor.getString(3)));
            cursor.close();
        }
        Log.d("getBook("+id+")", image.toString());
        return image;
    }

    // Get All Books
    public List<Image> getAllImage() {
        List<Image> images = new LinkedList<>();

        String query = "SELECT  * FROM " + TABLE_IMAGES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Image image = new Image();
        if (cursor.moveToFirst()) {
            do {
                image.setId(Integer.parseInt(cursor.getString(0)));
                image.setName(cursor.getString(1));
                image.setType(cursor.getString(2));
                image.setIdmedia(Integer.parseInt(cursor.getString(3)));
                images.add(image);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", image.toString());
        cursor.close();

        return images;
    }

    // Updating single book
    public void updateImage(Image image) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", image.getName()); // get title
        values.put("author", image.getType()); // get author
        values.put("idmedia", image.getIdmedia());

        db.update(TABLE_IMAGES, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(image.getId()) }); //selection args

        db.close();
    }

    // Deleting single book
    public void deleteImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_IMAGES,
                KEY_ID+" = ?",
                new String[] { String.valueOf(image.getId()) });

        db.close();

        Log.d("deleteBook", image.toString());

    }
}

