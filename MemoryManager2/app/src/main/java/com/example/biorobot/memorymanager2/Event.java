package com.example.biorobot.memorymanager2;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by biorobot on 2014-11-21.
 */
public class Event extends Fragment {

    //textviews and inputs from the other data for linking with xml.

    TextView eventDataType, eventDataDesc, eventDataTime;

    //new for alarm build!
    CheckBox eventDataAlarm;

    String inputType, inputDesc, inputTime;

    Reminder localReminder;
    //communicator
    EventCommunicator commEvent;

    Button pushReminderButton;
    Button deleteReminderButton;

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
            eventDataAlarm = (CheckBox) testView.findViewById(R.id.eventDataAlarm);

            eventDataType.setText(localReminder.getType());
            eventDataDesc.setText(localReminder.getDescription());
            //if this is nothing then there's a problem since it is now an int rather than string
            //strings can be empty but integers can't.
            //thus it generates a Rescource not found exception

            //it needs, however, to be fixed when the Reminder is created in the create fragment.
            //Apparently it can assign without a problem but cannot read it .toString()
            Log.i("inside Event onCreateView. reminder.time()... is = ", Integer.toString(localReminder.getTime()) + "");
             //can't even Log.i the whole expression since it always will evaluate to null.. ?
            String tempDataTime = Integer.toString(localReminder.getTime());

            //eventDataTime.setText(localReminder.getTime());
            eventDataTime.setText(tempDataTime);
            //actually it always assigns emtpy?
            //always segfaults onClick for listItem. Okay it's because of time assignment is changed to
            //The TimePicker rather than the EditText field.

            /**
             * Ok now instead the database gets messed up. New bug introduced.
             * It used to be able to
             */

            //Nope that didn't work. It gets rescource not found exception all the time..

            pushReminderButton = (Button) testView.findViewById(R.id.pushChangeButton);

            pushReminderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "You changed reminder!", Toast.LENGTH_LONG).show();

                    //reassigns the local reminder to be sent back
                    localReminder = new Reminder(
                            eventDataType.getText().toString().trim(),
                            eventDataDesc.getText().toString().trim(),
                            Integer.parseInt(eventDataTime.getText().toString().trim()),
                            eventDataAlarm.isChecked()
                    );
                    //is this actually pushing the reminder? Yes.
                    //Communicator comm = (Communicator) getActivity();
                    commEvent.returnReminder(localReminder);

                }
            });
            /**
             * here is new functionality for deleting Reminders.
             */
            deleteReminderButton = (Button) testView.findViewById(R.id.deleteButton);
            deleteReminderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "You deleted a reminder!", Toast.LENGTH_LONG).show();

                    commEvent.deleteReminder();


                }
            });
        }
        return testView;
    }
}