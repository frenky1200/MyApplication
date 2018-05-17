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
    private static final String DATABASE_NAME = "MediaDB.sqlite";

    public MediaHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MEDIA_TABLE = "CREATE TABLE media ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "type TEXT, "+
                "tags TEXT, "+
                "inside TEXT, "+
                "outside TEXT, "+
                "albums TEXT )";

        db.execSQL(CREATE_MEDIA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS media");

        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    private static final String TABLE_MEDIA = "media";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_INSIDE = "inside";
    private static final String KEY_OUTSIDE = "outside";
    private static final String KEY_ALBUMS = "albums";

    private static final String[] COLUMNS = {KEY_ID, KEY_NAME, KEY_TYPE, KEY_TAGS, KEY_INSIDE, KEY_OUTSIDE, KEY_ALBUMS};

    public int addMedia(Media media){

        Log.d("addAlbum", media.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, media.getName()); // get title
        values.put(KEY_TYPE, media.getType()); // get author
        values.put(KEY_TAGS, media.getTags()); // get title
        values.put(KEY_INSIDE, media.getInsideUri()); // get author
        values.put(KEY_OUTSIDE, media.getOutsideUri());
        values.put(KEY_ALBUMS, media.getAlbum());
        long id =  db.insert(TABLE_MEDIA, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();
        return (int)id;
    }

    public Media getMedia(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_MEDIA, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        Media media = new Media();

        if (cursor != null){
            cursor.moveToFirst();
            media.setId(Integer.parseInt(cursor.getString(0)));
            media.setName(cursor.getString(1));
            media.setType(cursor.getString(2));
            media.setTags(cursor.getString(3));
            media.setInsideUri(cursor.getString(4));
            media.setOutsideUri(cursor.getString(5));
            media.setAlbum(cursor.getString(6));
            cursor.close();
        }
        Log.d("getBook("+id+")", media.toString());
        return media;
    }

    // Get All Books
    public List<Media> getAllMedia(String album) {

        List<Media> medias = new LinkedList<>();
        String query = "SELECT  * FROM " + TABLE_MEDIA + " WHERE `albums` LIKE '" + album + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Media media;
        if (cursor.moveToFirst()) {
            do {
                media = new Media();
                media.setId(Integer.parseInt(cursor.getString(0)));
                media.setName(cursor.getString(1));
                media.setType(cursor.getString(2));
                media.setTags(cursor.getString(3));
                media.setInsideUri(cursor.getString(4));
                media.setOutsideUri(cursor.getString(5));
                media.setAlbum(cursor.getString(6));
                medias.add(media);
            } while (cursor.moveToNext());
        }
        Log.d("getAllAlbums()", "");
        cursor.close();
        return medias;
    }

    // Updating single book
    public int updateMedia(Media media) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", media.getName()); // get title
        values.put("type", media.getType()); // get author
        values.put("tags", media.getTags());
        values.put(KEY_INSIDE, media.getInsideUri()); // get author
        values.put(KEY_OUTSIDE, media.getOutsideUri());
        values.put(KEY_ALBUMS, media.getAlbum());
        int i = db.update(TABLE_MEDIA, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(media.getId()) }); //selection args
        db.close();
        return i;
    }

    // Deleting single book
    public void deleteMedia(Media media) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDIA,
                KEY_ID+" = ?",
                new String[] { String.valueOf(media.getId()) });
        db.close();
        Log.d("deleteAlbum", media.toString());
    }

    public List<Media> findByStr(String str){
        List<Media> medias = new LinkedList<>();

        String query = "SELECT  * FROM " + TABLE_MEDIA + " WHERE `tags` GLOB '*" + str + "*'";
        //String query = "SELECT  * FROM " + TABLE_MEDIA + " WHERE `tags` IN '" + str + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Media media;
        if (cursor.moveToFirst()) {
            do {
                media = new Media();
                media.setId(Integer.parseInt(cursor.getString(0)));
                media.setName(cursor.getString(1));
                media.setType(cursor.getString(2));
                media.setTags(cursor.getString(3));
                media.setInsideUri(cursor.getString(4));
                media.setOutsideUri(cursor.getString(5));
                media.setAlbum(cursor.getString(6));
                medias.add(media);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("getAllAlbums()", medias.toString());
        return medias;
    }

    public Media findByName(String name, String album){

        String query = "SELECT  * FROM " + TABLE_MEDIA + " " +
                "WHERE upper(name) GLOB upper(\"*" + name +"*\") and" +
                "  " + "upper(albums) GLOB upper(\"*" + album + "*\")";

        String query2 = "SELECT * FROM " + TABLE_MEDIA + " WHERE upper(media.name) GLOB upper(\"*"+name+
                "*\") AND media.albums==(select name from albums WHERE upper(albums.alternate) GLOB upper(\"*"+album+
                "*\") OR upper(albums.name) GLOB upper(\"*"+album+"*\"))";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query2, null);

        // 3. go over each row, build book and add it to list
        Media media = new Media();
        if (cursor.moveToFirst()) {
            do {
                media = new Media();
                media.setId(Integer.parseInt(cursor.getString(0)));
                media.setName(cursor.getString(1));
                media.setType(cursor.getString(2));
                media.setTags(cursor.getString(3));
                media.setInsideUri(cursor.getString(4));
                media.setOutsideUri(cursor.getString(5));
                media.setAlbum(cursor.getString(6));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return media;
    }
}
