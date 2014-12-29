package com.example.biorobot.memorymanager2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.biorobot.memorymanager2.ReminderContract.ReminderEntry;

/**
 * Created by biorobot on 2014-12-27.
 */
public class ReminderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Reminder.db";


    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    String createSQL = "" +
            "CREATE TABLE Reminder (" +
            ReminderEntry.COLUMN_NAME_REMINDER_ID + " INTEGER PRIMARY KEY," +
            ReminderEntry.COLUMN_NAME_TYPE + " text," +
            ReminderEntry.COLUMN_NAME_DESCRIPTION + " text," +
            ReminderEntry.COLUMN_NAME_TIME + " text);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("SQL CODE:", "\n\n" + createSQL + "\n\n");
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Reminder;");
        db.execSQL(createSQL);
    }
}
