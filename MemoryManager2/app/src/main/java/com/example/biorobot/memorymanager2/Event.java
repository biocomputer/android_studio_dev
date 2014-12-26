package com.example.biorobot.memorymanager2;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by biorobot on 2014-11-21.
 */
public class Event extends Fragment {

    //textviews and inputs from the other data for linking with xml.
    //will change variable names later..

    TextView eventDataType, eventDataDesc, eventDataTime;
    String inputType, inputDesc, inputTime;

    Reminder localReminder;
    //communicator
    EventCommunicator commEvent;

    Button pushReminderButton;
    Button leaveButton;

    View testView; //for testing how to add things..

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void pullReminder(Reminder getReminder) {
        //assigns the local Reminder variable
        Log.i("We are INSIDE PULLREMINDER", "   --  Inside Event.java!");
        Log.i("THE REMINDER IS :: ", getReminder.getType());

        localReminder = getReminder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * added savedInstance state check for anti-clutter. Still in development.
         * The goal is to
         */
        if (savedInstanceState == null) {

            setRetainInstance(true);
            testView = inflater.inflate(R.layout.event_fragment, container, false);

            commEvent = (EventCommunicator) getActivity();

            //links with activity
            //commEvent = (EventCommunicator) getActivity();

            eventDataType = (TextView) testView.findViewById(R.id.eventDataType);
            eventDataDesc = (TextView) testView.findViewById(R.id.eventDataDesc);
            eventDataTime = (TextView) testView.findViewById(R.id.eventDataTime);

            eventDataType.setText(localReminder.getType());
            eventDataDesc.setText(localReminder.getDescription());
            eventDataTime.setText(localReminder.getTime());

            pushReminderButton = (Button) testView.findViewById(R.id.pushChangeButton);

            pushReminderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "You changed reminder!", Toast.LENGTH_LONG).show();

                    //reassigns the local reminder to be sent back.
                    localReminder = new Reminder(eventDataType.getText().toString().trim(), eventDataDesc.getText().toString().trim(), eventDataTime.getText().toString().trim());
                    //is this actually pushing the reminder? Yes.
                    //Communicator comm = (Communicator) getActivity();
                    commEvent.returnReminder(localReminder);

                }
            });
        }
        return testView;
    }
}