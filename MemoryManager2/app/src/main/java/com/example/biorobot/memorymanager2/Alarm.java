package com.example.biorobot.memorymanager2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by biorobot on 2015-01-06.
 */
public class Alarm extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        /**
         * what do we want to send?
         */
        //Toast.makeText(context, "recieved click", Toast.LENGTH_SHORT).show();
        Log.i("Alarm", "inside onReceive!");

        // Your code to execute when the alarm triggers
        // and the broadcast is received.

        /**
         * use intents to push the alarm? Alarm signal is sent from listViewFragment?
         */
        //Intent iC = new Intent(context, ActivityC.class);
        //den ska veta vad back knappen ska leda till.

        //iC.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(iC);
    }
}