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

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MediaDB.sqlite";
    private static final String TABLE_ANIMES = "animes";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "title";
    private static final String KEY_TYPE = "author";
    private static final String KEY_IDMEDIA = "idmedia";
    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_TYPE,KEY_IDMEDIA};

    public AnimeHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ANIME_TABLE = "CREATE TABLE animes ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "author TEXT, "+
                "idmedia INTEGER )";

        db.execSQL(CREATE_ANIME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS animes");
        this.onCreate(db);

    }

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

        Anime anime = new Anime();
        if (cursor != null) {
            cursor.moveToFirst();
            anime.setId(Integer.parseInt(cursor.getString(0)));
            anime.setName(cursor.getString(1));
            anime.setType(cursor.getString(2));
            anime.setIdmedia(Integer.parseInt(cursor.getString(3)));
            cursor.close();
        }
        Log.d("getBook("+id+")", anime.toString());
        return anime;
    }

    public List<Anime> getAllAnime() {
        List<Anime> animes = new LinkedList<>();

        String query = "SELECT  * FROM " + TABLE_ANIMES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Anime anime = new Anime();
        if (cursor.moveToFirst()) {
            do {
                anime.setId(Integer.parseInt(cursor.getString(0)));
                anime.setName(cursor.getString(1));
                anime.setType(cursor.getString(2));
                anime.setIdmedia(Integer.parseInt(cursor.getString(3)));
                animes.add(anime);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", anime.toString());
        cursor.close();

        return animes;
    }

    public void updateAnime(Anime anime) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", anime.getName()); // get title
        values.put("author", anime.getType()); // get author
        values.put("idmedia", anime.getIdmedia());

        db.update(TABLE_ANIMES, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(anime.getId()) }); //selection args

        db.close();
    }

    public void deleteAnime(Anime anime) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ANIMES,
                KEY_ID+" = ?",
                new String[] { String.valueOf(anime.getId()) });

        db.close();

        Log.d("deleteBook", anime.toString());

    }
}
