package com.example.myapplication.data.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.data.entity.Film;

import java.util.LinkedList;
import java.util.List;

public class FilmHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MediaDB.sqlite";

    public FilmHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE books ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "author TEXT, "+
                "idmedia INTEGER )";
        // create books table
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS books");

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Books table name
    private static final String TABLE_BOOKS = "films";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_IDMEDIA = "idmedia";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_AUTHOR,KEY_IDMEDIA};

    public void addFilm(Film film){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, film.getName()); // get title
        values.put(KEY_AUTHOR, film.getAuthor()); // get author
        values.put(KEY_IDMEDIA, film.getIdmedia());
        // 3. insert
        db.insert(TABLE_BOOKS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Film getFilm(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_BOOKS, // a. table
                        COLUMNS, // b. column names
                        " idmedia = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        Film film = new Film();
        // 3. if we got results get the first one
        if (cursor != null) {
            cursor.moveToFirst();
            film.setId(Integer.parseInt(cursor.getString(0)));
            film.setTitle(cursor.getString(1));
            film.setAuthor(cursor.getString(2));
            film.setIdmedia(Integer.parseInt(cursor.getString(3)));
            cursor.close();
        }
        Log.d("getBook("+id+")", film.toString());

        return film;
    }

    // Get All Books
    public List<Film> getAllFilms() {
        List<Film> films = new LinkedList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_BOOKS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build film and add it to list
        Film film;
        if (cursor.moveToFirst()) {
            do {
                film = new Film();
                film.setId(Integer.parseInt(cursor.getString(0)));
                film.setTitle(cursor.getString(1));
                film.setAuthor(cursor.getString(2));
                film.setIdmedia(cursor.getInt(3));
                films.add(film);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", films.toString());
        cursor.close();

        return films;
    }

    // Updating single film
    public void updateFilm(Film film) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", film.getName()); // get title
        values.put("author", film.getAuthor()); // get author
        values.put("idmedia", film.getIdmedia());

        // 3. updating row
        db.update(TABLE_BOOKS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(film.getId()) }); //selection args

        // 4. close
        db.close();
    }

    // Deleting single film
    public void deleteFilm(Film film) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(film.getId()) });
        db.close();

        Log.d("deleteBook", film.toString());

    }
}