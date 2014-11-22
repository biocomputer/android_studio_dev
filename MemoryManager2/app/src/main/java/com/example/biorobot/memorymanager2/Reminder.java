package com.example.biorobot.memorymanager2;


public class Reminder {


    private String type;
    private String description;
    private double time;

    public Reminder(String type, String description, double time)
    {
        super();
        this.type = type;
        this.description = description;
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
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


}