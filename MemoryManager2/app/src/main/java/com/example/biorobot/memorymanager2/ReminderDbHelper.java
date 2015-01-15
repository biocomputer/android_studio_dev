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
    /**
     * realizes the ReminderContract class.
     */

    //changed database to use int for time and also added alarm column
            // so therefore the version is now++ to 3
    //using variable names rather than direct input for clarity.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Reminder.db";

    //constructor
    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //no this.context = context?
        }

    String createSQL = "" +
            "CREATE TABLE Reminder (" +
            ReminderEntry.COLUMN_NAME_REMINDER_ID + " INTEGER PRIMARY KEY," +
            ReminderEntry.COLUMN_NAME_TYPE + " text," +
            ReminderEntry.COLUMN_NAME_DESCRIPTION + " text," +
            //changed in the Alarm build cycle. Need this to be saved as int
            ReminderEntry.COLUMN_NAME_TIME + " integer," +
            ReminderEntry.COLUMN_NAME_ALARM + " integer);";

    /**
     * Example would be Reminder (1 INTEGER PRIMARY KEY,
     *                            "Input Type here". text,
     *                            "input Desc here". text,
     *                            "12345". time),
     *                            0 or 1. alarm);
     *
     *       note: time is now seconds so time = 1000 is one thousand seconds. It's not time of day yet.
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("SQL CODE:", "\n\n" + createSQL + "\n\n");
        //sends create command.
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("inside ReminderDbHelper", " -- onUpgrade");
        Log.i("old version = ", ""+oldVersion);
        Log.i("new version = ", ""+newVersion);
        //deletes the Table Reminder. that is, it deletes the old table so a new one can be made.
        db.execSQL("DROP TABLE Reminder;");
        db.execSQL(createSQL);
    }
}
