package com.example.nayle.movieapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nayle.movieapplication.data.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nayle on 2/7/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "Movie_Database.db";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "Movie_Table";
    private static final String ID = "_id";
    private static final String TITLE = "title";
    private static final String Poster_path = "poster_path";
    private static final String Overveiw = "overview";
    private static final String Release_date = "release_date";
    private static final String Trailer = "trailer";
    private static final String Review = "review";

    SQLiteDatabase Movie_Database;

    public DBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String quary_table = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                //    TITLE + " TEXT NOT NULL ," +
                Poster_path + " TEXT  " +
//                Overveiw + "TEXT NOT NULL ," +
//                Release_date + "TEXT NOT NULL ," +
//                Trailer + "TEXT NOT NULL ," +
//                Review + "TEXT NOT NULL ," +
                ")";
        sqLiteDatabase.execSQL(quary_table);

//CREATE TABLE Movie_Table(_id INTEFER PRIMARY KEY AUTOINCREMENT, poster_path TEXT NOT NULL )
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDB() {
        Movie_Database = getWritableDatabase();
    }

    public void closeDB() {
        if (Movie_Database != null && Movie_Database.isOpen()) {
            Movie_Database.close();
        }
    }

    public long insert(int id, String PosterPath) {
        ContentValues values = new ContentValues();
        if (id != -1)
            values.put(ID, id);
        // values.put(TITLE,Title);
        values.put(Poster_path, PosterPath);
//        values.put(Release_date,ReleaseDate);
//        values.put(Overveiw,overview);
//        values.put(Trailer,trailer);
//        values.put(Review,review);
        long count=getWritableDatabase().insert(TABLE_NAME, null, values);
        return count;
    }


    public boolean checkIFexist(int id, String posterPath) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{ID}, ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }

    }


    public long delete(int id, String posterPath) {

        String where = ID + "=" + id;
        return Movie_Database.delete(TABLE_NAME, where, null);

        // where = Poster_path + " _ " + posterPath;
        //return Movie_Database.delete(TABLE_NAME,where, null);
    }

    public Cursor getAllRecords() {
        openDB();
        Movie_Database.query(TABLE_NAME, null, null, null, null, null, null);

        String query = "SELECT * FROM " + TABLE_NAME;
        return Movie_Database.rawQuery(query, null);
    }



    public List<Result> getAllMovie() {
        List<Result> result = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String posterPath = cursor.getString(1);

                // Adding contact to list
                //MovieMain movie = new MovieMain(ID, null, posterPath);
                Result r = new Result();
                r.setId(id);
                r.setPosterPath(posterPath);

                result.add(r);
            } while (cursor.moveToNext());
        }

        return result;
    }
}
