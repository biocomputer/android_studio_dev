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

/**
 * perhaps should be called ViewReminderFragment
 */
public class Event extends Fragment {


    TextView eventDataType, eventDataDesc, eventDataTime;

    CheckBox eventDataAlarm;

    String inputType, inputDesc, inputTime;

    Reminder localReminder;

    //communicator
    EventCommunicator commEvent;

    Button pushReminderButton;
    Button deleteReminderButton;

    View testView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void pullReminder(Reminder getReminder) {
        localReminder = getReminder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            setRetainInstance(true);
            testView = inflater.inflate(R.layout.event_fragment, container, false);

            commEvent = (EventCommunicator) getActivity();


            eventDataType = (TextView) testView.findViewById(R.id.eventDataType);
            eventDataDesc = (TextView) testView.findViewById(R.id.eventDataDesc);
            eventDataTime = (TextView) testView.findViewById(R.id.eventDataTime);
            eventDataAlarm = (CheckBox) testView.findViewById(R.id.eventDataAlarm);

            eventDataType.setText(localReminder.getType());
            eventDataDesc.setText(localReminder.getDescription());


            String tempDataTime = localReminder.getFormatedDateAndTime();

            eventDataTime.setEnabled(false);

            eventDataTime.setText(tempDataTime);


            pushReminderButton = (Button) testView.findViewById(R.id.pushChangeButton);

            pushReminderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "You changed reminder!", Toast.LENGTH_LONG).show();

                    //reassigns the local reminder to be sent back
                    localReminder = new Reminder(
                            eventDataType.getText().toString().trim(),
                            eventDataDesc.getText().toString().trim(),
                            localReminder.getTime(),
                            eventDataAlarm.isChecked()
                    );
                    commEvent.returnReminder(localReminder);

                }
            });

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