package com.example.myapplication.data.helpers;

/**
 * Created by Андрей on 11.05.2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.data.Album;
import com.example.myapplication.data.interfaces.IHelper;
import com.example.myapplication.data.interfaces.IMediable;

import java.util.LinkedList;
import java.util.List;



public class AlbumHelper extends SQLiteOpenHelper implements IHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AlbumDB";

    public AlbumHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALBUM_TABLE = "CREATE TABLE albums ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "type TEXT )";

        db.execSQL(CREATE_ALBUM_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS albums");

        this.onCreate(db);
    }

    private static final String TABLE_ALBUMS = "albums";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_TYPE};

    @Override
    public Album getById(int id){
        return new Album();
    }

    @Override
    public Album get(int id){
        Album album = new Album();
        return album;
    }

    @Override
    public List<Album> getAll(String id){
        List<Album> albums = new LinkedList<Album>();
        return albums;
    }

    @Override
    public <T extends IMediable> void add(T t) {

    }

    @Override
    public <T extends IMediable> int update(T t) {
        return 0;
    }

    @Override
    public <T extends IMediable> void delete(T t) {

    }

    public void addAlbum(Album album){
        Log.d("addAlbum", album.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_ALBUMS, // a. table
                        COLUMNS, // b. column names
                        " name = ?", // c. selections
                        new String[] {album.getName()}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor!=null){
            db = this.getWritableDatabase();

            // 2. create ContentValues to add key "column"/value
            cursor.moveToFirst();
            if(cursor.getCount()==0) {

                ContentValues values = new ContentValues();
                values.put(KEY_NAME, album.getName()); // get title
                values.put(KEY_TYPE, album.getType()); // get author

                // 3. insert
                db.insert(TABLE_ALBUMS, // table
                        null, //nullColumnHack
                        values); // key/value -> keys = column names/ values = column values

                // 4. close
            }
                db.close();
        }
    }

    public Album getAlbum(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_ALBUMS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Album album = new Album();
        album.setId(Integer.parseInt(cursor.getString(0)));
        album.setName(cursor.getString(1));
        album.setType(cursor.getString(2));

        Log.d("getBook("+id+")", album.toString());

        // 5. return book
        return album;
    }

    public List<Album> getAllAlbums(String type) {
        List<Album> albums = new LinkedList<Album>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ALBUMS + " WHERE `type` = '" + type + "'";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Album album = null;
        if (cursor.moveToFirst()) {
            do {
                album = new Album();
                album.setId(Integer.parseInt(cursor.getString(0)));
                album.setName(cursor.getString(1));
                album.setType(cursor.getString(2));

                // Add book to books
                albums.add(album);
            } while (cursor.moveToNext());
        }

        Log.d("getAllAlbums()", albums.toString());

        // return books
        return albums;
    }

    public int updateAlbum(Album album) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", album.getName()); // get title
        values.put("type", album.getType()); // get author

        // 3. updating row
        int i = db.update(TABLE_ALBUMS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(album.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    public void deleteAlbum(Album album) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ALBUMS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(album.getId()) });

        // 3. close
        db.close();

        Log.d("deleteAlbum", album.toString());

    }
    @Override
    public List<Album> findbystr(String str){
        List<Album> albums = new LinkedList<Album>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ALBUMS + " WHERE `name` LIKE '%" + str + "%'";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Album album = null;
        if (cursor.moveToFirst()) {
            do {
                album = new Album();
                album.setId(Integer.parseInt(cursor.getString(0)));
                album.setName(cursor.getString(1));
                album.setType(cursor.getString(2));

                // Add book to books
                albums.add(album);
            } while (cursor.moveToNext());
        }

        Log.d("getAllAlbums()", albums.toString());

        // return books
        return albums;
    }

}