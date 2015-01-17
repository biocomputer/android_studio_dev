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

    //setting the button to which of the AB tests we are using..
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

            Log.i("now inside creaRemidnerFragment", "LAUNCHED!");
            createReminderView = (View) inflater.inflate(R.layout.create_reminder_fragment, container, false);
            //linking the code with the xml for editing data values later..

            makeType = (EditText) createReminderView.findViewById(R.id.create_reminder_make_type);
            makeDesc = (EditText) createReminderView.findViewById(R.id.create_reminder_make_desc);
            makeTime = (TextView) createReminderView.findViewById(R.id.create_reminder_make_time);
            makeAlarm = (CheckBox) createReminderView.findViewById(R.id.create_reminder_alarm);
            makeDate = (TextView) createReminderView.findViewById(R.id.create_reminder_date);

            addReminderButton = (Button) createReminderView.findViewById(R.id.create_reminder_add_button);
            updateLayoutForABTest();


            setDateButton = (Button) createReminderView.findViewById(R.id.setDateButton);

            makeType.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    addReminderButton.setEnabled(!makeType.getText().toString().trim().isEmpty());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            addReminderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Your Contact has been created!", Toast.LENGTH_LONG).show();

                    //Datatype check so we won't get empty string to convert to int == error
//                    String timeText = makeTime.getText().toString().trim();
//                    int time;
//
//                    if (timeText.length() < 0) {
//                        time = Integer.parseInt(timeText);
//                    }
//                    else {
//                        time = 0;
//                    }
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
                        Toast.makeText(getActivity(), "Set date first", Toast.LENGTH_SHORT);
                        return;
                    }

                    boolean isChecked = ((CheckBox) createReminderView.findViewById(R.id.create_reminder_alarm)).isChecked();
                    //why doesn't this work. how do I make it add as int? Shouldn't it just be able to add since I changed the constructor to accept int??
                                                                                                                                //this might filter it. From string to int..
                    Reminder reminder = new Reminder(
                            makeType.getText().toString().trim(),
                            makeDesc.getText().toString().trim(),
                            time,
                            isChecked
                    );

                    /**
                     * TODO: Needs to add isDoSetAlarm and set Reminder ID and such.
                     */

                    //is this actually pushing the reminder? Yes.
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


            // get the current date
            /*final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            //display the current date
            updateDisplay();

            private void updateDisplay() {
            this.dateDisplay.setText(
                    new StringBuilder()
            )
        }*/
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