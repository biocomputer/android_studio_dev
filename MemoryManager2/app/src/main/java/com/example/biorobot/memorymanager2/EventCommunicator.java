package com.example.biorobot.memorymanager2;


/**
 * Created by biorobot on 2014-12-18.
 */
public interface EventCommunicator
{
    public void readReminder(Reminder data);

    public void returnReminder(Reminder data);
}
