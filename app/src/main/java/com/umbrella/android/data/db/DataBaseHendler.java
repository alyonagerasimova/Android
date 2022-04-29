package com.umbrella.android.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

import com.umbrella.android.data.neuralNetwork.network.Network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataBaseHendler extends SQLiteOpenHelper {
    public static final String COLUMN_ID = "_id";
    private static Context context;
    private static String DB_PATH;// полный путь к базе данных
    private static String DB_NAME = "NetworkDB";
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE = "Network"; // название таблицы в бд
    public static final String COLUMN_NETWORK = "NetworkData";
    public static final String COLUMN_NAME = "network";
    public int i = 0;
    public void saveNewNetwork(Network network) {
        i++;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NETWORK, String.valueOf(network));
        values.put("Нейронная сеть " + i, String.valueOf(network));
        db.insert(TABLE, null, values);
        db.close();
    }
    // below is the method for deleting our course.
    public void deleteCourse(Network network) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE, "name=?", new String[]{network.toString()});
        db.close();
    }

    // we have created a new method for reading all the courses.
    public ArrayList<Network> readCourses() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE, null);

        // on below line we are creating a new array list.
        ArrayList<Network> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new Network(cursorCourses.getString(1)));
            } while (cursorCourses.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();
        return courseModalArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public DataBaseHendler(Context context) {
        super(context, DB_NAME, null, SCHEMA);
    }

    // below is the method for updating our courses
    public void updateCourse(Network network) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(COLUMN_NETWORK, String.valueOf(network));

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(TABLE, values, "name=?", new String[]{String.valueOf(network)});
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE + " ("
                + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NETWORK + " BLOB)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }
}