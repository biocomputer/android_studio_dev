package com.example.biorobot.memorymanager2;

import android.provider.BaseColumns;

/**
 * Created by biorobot on 2014-12-27.
 */

/**
 * This is backend like an ADT.
 */
public final class ReminderContract {

    public ReminderContract() {}


    // This is our Schema!
    public static abstract class ReminderEntry implements BaseColumns {
        public static final String TABLE_NAME = "reminder";
        public static final String COLUMN_NAME_REMINDER_ID = "reminderid";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TIME = "time";

    }

    //private String type;
    //private String description;
    //private String time;

}
