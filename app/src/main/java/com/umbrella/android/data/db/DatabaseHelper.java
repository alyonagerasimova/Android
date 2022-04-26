package com.umbrella.android.data.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import com.umbrella.android.data.neuralNetwork.network.Network;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "network.db"; // название бд
    private static final int VERSION = 1; // версия базы данных
    public static final String TABLE = "network"; // название таблицы в бд
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NETWORK = "network";
    public static final String COLUMN_NUMBER_HIDDEN = "numberHidden";
    public static final String COLUMN_NUMBER_CYCLE = "numberCycle";
    public static final String COLUMN_NUMBER_LEARNING = "numberLearning";
    public static final String COLUMN_NUMBER_ERROR = "numberError";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public void saveNewNetwork(Network network) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, "String.valueOf(network)");
        values.put(COLUMN_NUMBER_HIDDEN, network.getNumberHiddenNeurons());
        values.put(COLUMN_NUMBER_LEARNING, network.getLearningRateFactor());
        values.put(COLUMN_NUMBER_CYCLE, network.getNumberCycles());
        values.put(COLUMN_NUMBER_ERROR, network.getError());
        db.insert(TABLE, null, values);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE network (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT, " + COLUMN_NUMBER_HIDDEN + " INTEGER," + COLUMN_NUMBER_LEARNING
                + " REAL," + COLUMN_NUMBER_CYCLE + " INTEGER,"
                + COLUMN_NUMBER_ERROR + " REAL);");
        db.execSQL("INSERT INTO " + TABLE + " (" + COLUMN_NAME
                + ", " + COLUMN_NUMBER_HIDDEN + "," + COLUMN_NUMBER_LEARNING + ","
                + COLUMN_NUMBER_CYCLE + "," + COLUMN_NUMBER_ERROR + ")  VALUES ('Нейронная сеть 1',0.1, 100, 5, null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}