package com.example.myapplication.data.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.data.entity.Album;
import com.example.myapplication.data.interfaces.IMediable;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("ALL")
public class AlbumHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MediaDB.sqlite";
    private static final String TABLE_ALBUMS = "albums";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ALTERNATE = "alternate";
    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_TYPE,KEY_ALTERNATE};

    public AlbumHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ALBUM_TABLE =
                "CREATE TABLE albums (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type TEXT, alternate TEXT);\n" ;
        db.execSQL(CREATE_ALBUM_TABLE);

        CREATE_ALBUM_TABLE =
                "CREATE TABLE animes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, author TEXT, idmedia INTEGER);\n" ;
        db.execSQL(CREATE_ALBUM_TABLE);

        CREATE_ALBUM_TABLE =
                "CREATE TABLE excerptions (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, author TEXT, idmedia INTEGER);\n" ;
        db.execSQL(CREATE_ALBUM_TABLE);

        CREATE_ALBUM_TABLE =
                "CREATE TABLE films (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, author TEXT, idmedia INTEGER);\n" ;
        db.execSQL(CREATE_ALBUM_TABLE);

        CREATE_ALBUM_TABLE =
                "CREATE TABLE images (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, author TEXT, idmedia INTEGER);\n" ;
        db.execSQL(CREATE_ALBUM_TABLE);

        CREATE_ALBUM_TABLE =
                "CREATE TABLE media (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type TEXT, tags TEXT, inside TEXT, outside TEXT, albums TEXT);\n" ;
        db.execSQL(CREATE_ALBUM_TABLE);

        CREATE_ALBUM_TABLE =
                "CREATE TABLE musics (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type TEXT, idmedia INTEGER);\n";
        db.execSQL(CREATE_ALBUM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS albums");

        this.onCreate(db);
    }

    public Album getById(int id){
        return new Album();
    }

    public Album get(int id){
        return new Album();
    }

    public List<Album> getAll(String id){
        return new LinkedList<>();
    }

    public <T extends IMediable> void add(T t) {

    }

    public <T extends IMediable> int update(T t) {
        return 0;
    }

    public <T extends IMediable> void delete(T t) {

    }

    public long addAlbum(Album album){
        long Id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_ALBUMS, // a. table
                        COLUMNS, // b. column names
                        " upper(name) = ? OR upper(alternate) GLOB ?", // c. selections
                        new String[] {album.getName().toUpperCase(), album.getName().toUpperCase()}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor!=null){
            db = this.getWritableDatabase();
            cursor.moveToFirst();
            if(cursor.getCount()==0) {
                ContentValues values = new ContentValues();
                values.put(KEY_NAME, album.getName()); // get title
                values.put(KEY_TYPE, album.getType()); // get author
                Id = db.insert(TABLE_ALBUMS, // table
                        null, //nullColumnHack
                        values); // key/value -> keys = column names/ values = column values

            }else
                Id = cursor.getInt(0);
                db.close();
            cursor.close();
        }
        return Id;
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


        Album album = new Album();
        if (cursor != null){
            cursor.moveToFirst();
            album.setId(Integer.parseInt(cursor.getString(0)));
            album.setName(cursor.getString(1));
            album.setType(cursor.getString(2));
            cursor.close();
        }
        Log.d("getBook("+id+")", album.toString());
        return album;
    }

    public List<Album> getAllAlbums(String type) {
        List<Album> albums = new LinkedList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ALBUMS + " WHERE `type` = '" + type + "'";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Album album;
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
        cursor.close();
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

    public List<Album> findbystr(String str){
        List<Album> albums = new LinkedList<>();

        String query = "SELECT  * FROM " + TABLE_ALBUMS + " WHERE `name` LIKE '%" + str + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Album album;
        if (cursor.moveToFirst()) {
            do {
                album = new Album();
                album.setId(Integer.parseInt(cursor.getString(0)));
                album.setName(cursor.getString(1));
                album.setType(cursor.getString(2));
                albums.add(album);
            } while (cursor.moveToNext());
        }

        Log.d("getAllAlbums()", albums.toString());
        cursor.close();
        return albums;
    }

}