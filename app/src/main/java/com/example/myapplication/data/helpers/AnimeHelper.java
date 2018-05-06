package com.example.myapplication.data.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.data.entity.Anime;

import java.util.LinkedList;
import java.util.List;

public class AnimeHelper extends SQLiteOpenHelper{

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "AnimeDB";

    public AnimeHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_ANIME_TABLE = "CREATE TABLE animes ( " +
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
        db.execSQL("DROP TABLE IF EXISTS animes");

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Books table name
    private static final String TABLE_ANIMES = "animes";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "title";
    private static final String KEY_TYPE = "author";
    private static final String KEY_IDMEDIA = "idmedia";

    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_TYPE,KEY_IDMEDIA};

    public void addAnime(Anime anime){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, anime.getName()); // get title
        values.put(KEY_TYPE, anime.getType()); // get author
        values.put(KEY_IDMEDIA, anime.getIdmedia());

        // 3. insert
        db.insert(TABLE_ANIMES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Anime getAnime(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_ANIMES, // a. table
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
        Anime anime = new Anime();
        anime.setId(Integer.parseInt(cursor.getString(0)));
        anime.setName(cursor.getString(1));
        anime.setType(cursor.getString(2));
        anime.setIdmedia(Integer.parseInt(cursor.getString(3)));

        Log.d("getBook("+id+")", anime.toString());
        cursor.close();
        // 5. return book
        return anime;
    }

    // Get All Books
    public List<Anime> getAllAnime() {
        List<Anime> animes = new LinkedList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ANIMES;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Anime anime = null;
        if (cursor.moveToFirst()) {
            do {
                anime = new Anime();
                anime.setId(Integer.parseInt(cursor.getString(0)));
                anime.setName(cursor.getString(1));
                anime.setType(cursor.getString(2));
                anime.setIdmedia(Integer.parseInt(cursor.getString(3)));

                // Add book to books
                animes.add(anime);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", anime.toString());
        cursor.close();
        // return books
        return animes;
    }

    // Updating single book
    public int updateAnime(Anime anime) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", anime.getName()); // get title
        values.put("author", anime.getType()); // get author
        values.put("idmedia", anime.getIdmedia());

        // 3. updating row
        int i = db.update(TABLE_ANIMES, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(anime.getId()) }); //selection args

        // 4. close
        db.close();
        return i;

    }

    // Deleting single book
    public void deleteAnime(Anime anime) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ANIMES,
                KEY_ID+" = ?",
                new String[] { String.valueOf(anime.getId()) });

        // 3. close
        db.close();

        Log.d("deleteBook", anime.toString());

    }
}
