package br.com.zup.movieshelf.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import br.com.zup.movieshelf.model.Movie;

/**
 * Created by ivo on 29/12/16.
 */

public class MovieDbHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "movieshelf";
    // Movie table name
    private static final String TABLE_MOVIES = "movies";
    // Movie table columns names
    private static final String KEY_IMDBID = "imdbID";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PLOT = "plot";
    private static final String KEY_YEAR = "year";
    private static final String KEY_RUNTIME = "runtime";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_DIRECTOR = "director";
    private static final String KEY_WRITER = "writer";
    private static final String KEY_ACTORS = "actors";
    private static final String KEY_AWARDS = "awards";
    private static final String KEY_POSTER = "poster";
    private static final String KEY_IMDBRATING = "imdbRating";
    private static final String KEY_TYPE = "type";

    public MovieDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIES + "(" +
                KEY_IMDBID + " TEXT PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_PLOT + " TEXT," +
                KEY_YEAR + " TEXT," +
                KEY_RUNTIME + " TEXT," +
                KEY_GENRE + " TEXT," +
                KEY_DIRECTOR + " TEXT," +
                KEY_WRITER + " TEXT," +
                KEY_ACTORS + " TEXT," +
                KEY_AWARDS + " TEXT," +
                KEY_POSTER + " TEXT," +
                KEY_IMDBRATING + " TEXT," +
                KEY_TYPE + " TEXT" +
                ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        // Creating tables again
        onCreate(db);
    }

    // Adding new movie
    public void addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMDBID, movie.getImdbID());
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_PLOT, movie.getPlot());
        values.put(KEY_YEAR, movie.getYear());
        values.put(KEY_RUNTIME, movie.getRuntime());
        values.put(KEY_GENRE, movie.getGenre());
        values.put(KEY_DIRECTOR, movie.getDirector());
        values.put(KEY_WRITER, movie.getWriter());
        values.put(KEY_ACTORS, movie.getActors());
        values.put(KEY_AWARDS, movie.getAwards());
        values.put(KEY_POSTER, movie.getPoster());
        values.put(KEY_IMDBRATING, movie.getImdbRating());
        values.put(KEY_TYPE, movie.getType());

        // Inserting Row
        db.insert(TABLE_MOVIES, null, values);
        db.close(); // Closing database connection
    }

    // Getting one movie
    public Movie getMovie(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIES, new String[]{
                KEY_IMDBID, KEY_TITLE, KEY_PLOT, KEY_YEAR, KEY_RUNTIME, KEY_GENRE, KEY_DIRECTOR, KEY_WRITER, KEY_ACTORS, KEY_AWARDS, KEY_POSTER, KEY_IMDBRATING, KEY_TYPE}, KEY_IMDBID + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Movie movie = null;
        if(cursor != null && cursor.getCount() > 0) {
            movie = new Movie();
            movie.setImdbID(cursor.getString(0));
            movie.setTitle(cursor.getString(1));
            movie.setPlot(cursor.getString(2));
            movie.setYear(cursor.getString(3));
            movie.setRuntime(cursor.getString(4));
            movie.setGenre(cursor.getString(5));
            movie.setDirector(cursor.getString(6));
            movie.setWriter(cursor.getString(7));
            movie.setActors(cursor.getString(8));
            movie.setAwards(cursor.getString(9));
            movie.setPoster(cursor.getString(10));
            movie.setImdbRating(cursor.getString(11));
            movie.setType(cursor.getString(12));
        }
        return movie;
    }

    // Getting All Movies
    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movieList = new ArrayList<Movie>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MOVIES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setImdbID(cursor.getString(0));
                movie.setTitle(cursor.getString(1));
                movie.setPlot(cursor.getString(2));
                movie.setYear(cursor.getString(3));
                movie.setRuntime(cursor.getString(4));
                movie.setGenre(cursor.getString(5));
                movie.setDirector(cursor.getString(6));
                movie.setWriter(cursor.getString(7));
                movie.setActors(cursor.getString(8));
                movie.setAwards(cursor.getString(9));
                movie.setPoster(cursor.getString(10));
                movie.setImdbRating(cursor.getString(11));
                movie.setType(cursor.getString(12));
                // Adding movie to list
                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        // return movie list
        return movieList;
    }

    // Deleting a movie
    public void deleteMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, KEY_IMDBID + " = ?",
        new String[] { String.valueOf(movie.getImdbID()) });
        db.close();
    }
    
}
