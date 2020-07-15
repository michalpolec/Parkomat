package com.example.parkomat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MainSQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "database";
    public static final String VEHICLE_TABLE_NAME = "vehicle";
    public static final String VEHICLE_COLUMN_ID = "_id";
    public static final String VEHICLE_COLUMN_NR = "nr";
    public static final String VEHICLE_COLUMN_DATE = "date";
    public static final String VEHICLE_COLUMN_COST = "cost";

    public MainSQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + VEHICLE_TABLE_NAME + " (" +
                VEHICLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VEHICLE_COLUMN_NR + " TEXT, " +
                VEHICLE_COLUMN_DATE + " TEXT, " +
                VEHICLE_COLUMN_COST + " DOUBLE" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + VEHICLE_TABLE_NAME);
        onCreate(db);
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + VEHICLE_TABLE_NAME + " where _id="+id+"", null );
        return res;
    }
}
