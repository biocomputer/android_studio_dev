package com.example.biorobot.memorymanager2;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by biorobot on 2014-11-27.
 */
public class CreateReminderFragment extends Fragment {

    View createReminderView;
    EditText makeType, makeDesc ;
    CheckBox makeAlarm;

    //variables for the new timePicker
    int year, month, day;

    TextView dateDisplay, makeDate, makeTime;

    static final int DATE_DIALOG_ID = 0;

    Button addReminderButton, createReminderTimeButton, setDateButton;

    //setting the button to which of the AB tests
    public int ABLayoutMode = 0;

    private void updateLayoutForABTest() {
        if(ABLayoutMode == 1) {
            // Case A
            addReminderButton.setText("Create Reminder");
        } else if(ABLayoutMode == 2) {
            // Case B
            addReminderButton.setText("Store Reminder");
        }

    }

    public CreateReminderFragment() {
            super();
        }


    TimePickerFragment timePickerFragment;
    DatePickerFragment datePickerFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null) {

            createReminderView = (View) inflater.inflate(R.layout.create_reminder_fragment, container, false);

            makeType = (EditText) createReminderView.findViewById(R.id.create_reminder_make_type);
            makeDesc = (EditText) createReminderView.findViewById(R.id.create_reminder_make_desc);
            makeTime = (TextView) createReminderView.findViewById(R.id.create_reminder_make_time);
            makeAlarm = (CheckBox) createReminderView.findViewById(R.id.create_reminder_alarm);
            makeDate = (TextView) createReminderView.findViewById(R.id.create_reminder_date);

            addReminderButton = (Button) createReminderView.findViewById(R.id.create_reminder_add_button);
            updateLayoutForABTest();


            setDateButton = (Button) createReminderView.findViewById(R.id.setDateButton);



            addReminderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //validate
                    if(makeTime.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please provide a time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(makeDate.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please provide a date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(makeType.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please provide a type", Toast.LENGTH_SHORT).show();
                        return;
                    }if(makeDesc.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please provide a description", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long time;
                    String timeString = makeTime.getText().toString();
                    String [] tokens = timeString.split(":");
                    int hour = Integer.valueOf(tokens[0]);
                    int minute = Integer.valueOf(tokens[1]);

                    if(currentCalendar != null) {
                        currentCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        currentCalendar.set(Calendar.MINUTE, minute);
                        time = currentCalendar.getTimeInMillis();
                    }
                    else {
                        Toast.makeText(getActivity(), "Set date first", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean isChecked = ((CheckBox) createReminderView.findViewById(R.id.create_reminder_alarm)).isChecked();
                                                                                                                            //this might filter it. From string to int..
                    Reminder reminder = new Reminder(
                            makeType.getText().toString().trim(),
                            makeDesc.getText().toString().trim(),
                            time,
                            isChecked
                    );


                    Toast.makeText(getActivity(), "Your Reminder has been created!", Toast.LENGTH_LONG).show();
                    Communicator comm = (Communicator) getActivity();
                    comm.transferReminder(reminder);

                }
            });

            timePickerFragment = new TimePickerFragment();
            createReminderTimeButton = (Button) createReminderView.findViewById(R.id.create_reminder_time_button);

            createReminderTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePickerFragment.show(getFragmentManager(), "timePicker");
                }
            });

            makeTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePickerFragment.show(getFragmentManager(), "timePicker");
                }
            });


            datePickerFragment = new DatePickerFragment();


            makeDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerFragment.show(getFragmentManager(), "datePicker");
                }
            });



            setDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerFragment.show(getFragmentManager(), "datePicker");
                }
            });

        }
        return createReminderView;

    }


    public void updateTime(int hourOfDay, int minute) {
        makeTime.setText(hourOfDay + ":" + minute);
    }

    public Calendar currentCalendar;

    public void updateDate(int year, int month, int day) {
        currentCalendar = Calendar.getInstance();
        currentCalendar.set(year, month, day);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        makeDate.setText(sf.format(currentCalendar.getTime()));
    }

}