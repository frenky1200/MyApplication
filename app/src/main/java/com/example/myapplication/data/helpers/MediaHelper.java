package com.example.myapplication.data.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.data.entity.Media;

import java.util.LinkedList;
import java.util.List;

public class MediaHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MediaDB";

    public MediaHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_MEDIA_TABLE = "CREATE TABLE media ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "type TEXT, "+
                "tags TEXT, "+
                "albums TEXT )";

        // create books table
        db.execSQL(CREATE_MEDIA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS media");

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    // Books table name
    private static final String TABLE_MEDIA = "media";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_ALBUMS = "albums";

    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_TYPE,KEY_TAGS,KEY_ALBUMS};

    public int addMedia(Media media){
        Log.d("addAlbum", media.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, media.getName()); // get title
        values.put(KEY_TYPE, media.getType()); // get author
        values.put(KEY_TAGS, media.getTags());
        values.put(KEY_ALBUMS, media.getAlbum());

        // 3. insert
        long id =  db.insert(TABLE_MEDIA, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close

        db.close();
        return (int)id;
    }

    public Media getMedia(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_MEDIA, // a. table
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
        Media media = new Media();
        media.setId(Integer.parseInt(cursor.getString(0)));
        media.setName(cursor.getString(1));
        media.setType(cursor.getString(2));
        media.setTags(cursor.getString(3));
        media.setAlbum(cursor.getString(4));

        Log.d("getBook("+id+")", media.toString());

        // 5. return book
        return media;
    }

    // Get All Books
    public List<Media> getAllMedia(String album) {
        List<Media> medias = new LinkedList<Media>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MEDIA + " WHERE `albums` LIKE '" + album + "'";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Media media = null;
        if (cursor.moveToFirst()) {
            do {
                media = new Media();
                media.setId(Integer.parseInt(cursor.getString(0)));
                media.setName(cursor.getString(1));
                media.setType(cursor.getString(2));
                media.setTags(cursor.getString(3));
                media.setAlbum(cursor.getString(4));

                // Add book to books
                medias.add(media);
            } while (cursor.moveToNext());
        }

        Log.d("getAllAlbums()", "");

        // return books
        return medias;
    }

    // Updating single book
    public int updateMedia(Media media) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", media.getName()); // get title
        values.put("type", media.getType()); // get author
        values.put("tags", media.getTags());
        values.put("albums", media.getAlbum());

        // 3. updating row
        int i = db.update(TABLE_MEDIA, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(media.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single book
    public void deleteMedia(Media media) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_MEDIA,
                KEY_ID+" = ?",
                new String[] { String.valueOf(media.getId()) });

        // 3. close
        db.close();

        Log.d("deleteAlbum", media.toString());

    }

    public List<Media> findbystr(String str){
        List<Media> medias = new LinkedList<Media>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MEDIA + " WHERE `tags` GLOB '*" + str + "*'";
        //String query = "SELECT  * FROM " + TABLE_MEDIA + " WHERE `tags` IN '" + str + "'";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Media media = null;
        if (cursor.moveToFirst()) {
            do {
                media = new Media();
                media.setId(Integer.parseInt(cursor.getString(0)));
                media.setName(cursor.getString(1));
                media.setType(cursor.getString(2));
                media.setTags(cursor.getString(3));
                media.setAlbum(cursor.getString(4));

                // Add book to books
                medias.add(media);
            } while (cursor.moveToNext());
        }

        Log.d("getAllAlbums()", medias.toString());

        // return books
        return medias;
    }
}
