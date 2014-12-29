package com.example.biorobot.memorymanager2;


public class Reminder {


    private String type;
    private String description;
    private String time;

    public Reminder(String type, String description, String time)
    {
        super();
        this.type = type;
        this.description = description;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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
}