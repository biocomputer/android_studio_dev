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

    //somehow it didn't work well database_Version = 1;
    //using variable names rather than direct input for clarity.
    public static final int DATABASE_VERSION = 2;
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
            ReminderEntry.COLUMN_NAME_TIME + " text);";

    /**
     * Example would be Reminder (1 INTEGER PRIMARY KEY,
     *                            "Input Type here". text,
     *                            "input Desc here". text,
     *                            "input time here". time);
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
