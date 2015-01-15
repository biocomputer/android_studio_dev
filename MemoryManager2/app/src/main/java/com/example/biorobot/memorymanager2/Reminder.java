package com.example.biorobot.memorymanager2;


import java.sql.Timestamp;
import java.util.Calendar;

public class Reminder {


    private String type;
    private String description;

    private int time;

    //do set alarm is the checkbox
    private boolean doSetAlarm;
    //for internal use in the reminder
    private int reminderID;

    public Reminder(String type, String description, int time, boolean isAlarm)
    {
        super();
        this.type = type;
        this.description = description;
        this.time = time;
        this.setDoSetAlarm(isAlarm);
        //default -1 for error message
        this.setReminderID(-1);
    }


    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() { return time;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type + " -- " + description + " -- " + time;
    }

    public boolean isDoSetAlarm() {
        return doSetAlarm;
    }

    public void setDoSetAlarm(boolean doSetAlarm) {
        this.doSetAlarm = doSetAlarm;
    }

    public int getReminderID() {
        return reminderID;
    }

    public void setReminderID(int reminderID) {
        this.reminderID = reminderID;
    }
}