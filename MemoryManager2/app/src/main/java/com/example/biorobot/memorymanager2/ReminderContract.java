package com.example.biorobot.memorymanager2;

import android.provider.BaseColumns;

/**
 * Created by biorobot on 2014-12-27.
 */
public final class ReminderContract {

    public ReminderContract() {}

    /**
     * Define a Schema.
     * Create a sublass of SQLiteOpenHelper implementsin oncreate(SQLiteDatabase,
     * onUpdrae(SQLiteDatabase, int, int)
     *
     * This class takes care of opening the database if its exists,
     * creating it if it does not, and upgrading it as necessary.
     */
    // klass för att representera en rad i vår tabell
    public static abstract class ReminderEntry implements BaseColumns {
        public static final String TABLE_NAME = "reminder";
        public static final String COLUMN_NAME_REMINDER_ID = "reminderid";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TIME = "time";
        /**
         * new table for every Reminder object?
         */
    }

    //private String type;
    //private String description;
    //private String time;

}
