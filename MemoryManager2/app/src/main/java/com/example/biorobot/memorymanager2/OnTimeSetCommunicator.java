package com.example.biorobot.memorymanager2;

import android.widget.TimePicker;

/**
 * Created by biorobot on 2015-01-17.
 */
public interface OnTimeSetCommunicator {
    public void onTimeSet(TimePicker view, int hourOfDay, int minute);
}
