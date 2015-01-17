package com.example.biorobot.memorymanager2;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biorobot.memorymanager2.Event;
import com.example.biorobot.memorymanager2.R;
import com.example.biorobot.memorymanager2.Reminder;

import com.example.biorobot.memorymanager2.ReminderContract.ReminderEntry;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ListViewFragment extends Fragment {
    /**
     * reassign list in savedInstanceState?
     * or simply move it there?
     */
    ListView item_list;
    View itemView;

    Button createReminderButton;

    Button clearButton;

    //saves position when user clicks on a list item.
    int mPosition = 0;

    List<Reminder> reminder_list = new ArrayList<Reminder>();
    Communicator comm;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        comm = (Communicator) getActivity();
    }

    public void getReminder(Reminder reminderGet) {
        if (reminderGet.isDoSetAlarm() == true) {

            Long time = reminderGet.getTime();
            Intent intentAlarm = new Intent(getActivity(), Alarm.class);

            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(getActivity(), 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        }

        reminder_list.add(reminderGet);
    }
    public void replaceReminder(Reminder pushReminder) {
        reminder_list.set(mPosition, pushReminder);

    }
    public void removeReminder() {
        reminder_list.remove(mPosition);
        item_list.deferNotifyDataSetChanged();
        ReminderDbHelper mDbHelper = new ReminderDbHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Reminder WHERE reminderid = " + mPosition);
        db.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        ReminderDbHelper mDbHelper = new ReminderDbHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM Reminder;");

        for(int x = 0; x < reminder_list.size(); x++) {
            Reminder r = reminder_list.get(x);
            ContentValues values = new ContentValues();
            values.put(ReminderEntry.COLUMN_NAME_REMINDER_ID, x);
            values.put(ReminderEntry.COLUMN_NAME_TYPE, r.getType());
            values.put(ReminderEntry.COLUMN_NAME_DESCRIPTION, r.getDescription());
            values.put(ReminderEntry.COLUMN_NAME_TIME, r.getTime());
            values.put(ReminderEntry.COLUMN_NAME_ALARM, r.isDoSetAlarm());
            try {
                db.insert(
                        ReminderEntry.TABLE_NAME,
                        null,
                        values);
            } catch (Exception e) {
                Log.e("reminder_save", e.getMessage());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try
        {
            if(reminder_list == null) {
                reminder_list = new ArrayList<Reminder>();
            }

            ReminderDbHelper mDbHelper = new ReminderDbHelper(getActivity());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String [] projection = {
                    ReminderEntry.COLUMN_NAME_TYPE,
                    ReminderEntry.COLUMN_NAME_DESCRIPTION,
                    ReminderEntry.COLUMN_NAME_TIME,
                    ReminderEntry.COLUMN_NAME_REMINDER_ID,
                    ReminderEntry.COLUMN_NAME_ALARM
            };

            String sortOrder =
                    ReminderEntry.COLUMN_NAME_REMINDER_ID + " ASC";

            Cursor c = db.query(
                    ReminderEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );

            if(c.moveToFirst())
            {
                do {
                    int typeColIndex = c.getColumnIndex(ReminderEntry.COLUMN_NAME_TYPE);
                    String type = c.getString(typeColIndex);

                    int descColIndex = c.getColumnIndex(ReminderEntry.COLUMN_NAME_DESCRIPTION);
                    String desc = c.getString(descColIndex);


                    int timeColIndex = c.getColumnIndex(ReminderEntry.COLUMN_NAME_TIME);
                    long time = c.getLong(timeColIndex);

                    int alarmColIndex = c.getColumnIndex(ReminderEntry.COLUMN_NAME_ALARM);

                    int valueAlarm = c.getInt(alarmColIndex);
                    boolean alarm = false;

                    if (valueAlarm == 0) {
                        alarm = false;
                    }
                    else if (valueAlarm == 1) {
                        alarm = true;
                    }
                    else if (valueAlarm == -1) {
                        Log.e("inside cursor in listViewFragment.", "FATAL: alarm == -1 error");

                    }
                    else {
                        Log.e("listView cursor for alarm is wrong! valueAlarm = ", valueAlarm + "");
                    }

                    Reminder r = new Reminder(type,desc,time,alarm);


                    int reminderIdColIndex = c.getColumnIndex(ReminderEntry.COLUMN_NAME_REMINDER_ID);
                    int reminderID = c.getInt(reminderIdColIndex);

                    if (reminderID >= reminder_list.size()) {

                        reminder_list.add(r);
                    }

                } while(c.moveToNext()); //runs while there are rows in the cursor..

                c.close();
                // continue to the next row
            }
        } catch (Exception e) {
            Log.e("ListViewFragment_OnResume", e.getMessage());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle saveData) {
        super.onSaveInstanceState(saveData);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
        }
        else {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null)
        {
            if (item_list != null) {

                item_list.deferNotifyDataSetChanged();
            }

            itemView = inflater.inflate(R.layout.list_view_fragment, container, false);
            createReminderButton = (Button) itemView.findViewById(R.id.addReminderButton);
            clearButton = (Button) itemView.findViewById(R.id.clearButton);

            createReminderButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * this is the ADD BUTTON
                 */
                @Override
                public void onClick(View view) {
                    AddReminderButtonCommunicator a = (AddReminderButtonCommunicator)getActivity();
                    a.onCreateReminderClicked();
                    }
            });

            clearButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    try {
                        ReminderDbHelper mDbHelper = new ReminderDbHelper(getActivity());
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();

                        db.execSQL("DELETE FROM Reminder;");
                        reminder_list.clear();

                        item_list = populateListView(itemView);
                    }
                    catch (Exception e) {
                        Log.e("clearButton clicked Error! -- ", e.getMessage());
                    }
                }
            });

            item_list = populateListView(itemView);
            item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String toast_text = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getActivity().getApplicationContext(), toast_text, Toast.LENGTH_LONG).show();

                    Log.v("inside ListViewFragment onItemClick. Position == ", position + "");
                    EventCommunicator eventComm = (EventCommunicator) getActivity();

                    Reminder ful_reminder = reminder_list.get(position);
                    mPosition = position;
                    eventComm.readReminder(ful_reminder);
                }
            });

        }
        return itemView;
    }

    private class MyListAdapter extends ArrayAdapter<Reminder> {
        public MyListAdapter() {
            super(getActivity(), R.layout.item_view, reminder_list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            Reminder currentReminder = reminder_list.get(position);
            System.out.println(currentReminder.getType());

            TextView typeText = (TextView) itemView.findViewById(R.id.item_textType);
            typeText.setText(currentReminder.getType());

            TextView descText = (TextView) itemView.findViewById(R.id.item_textDesc);
            descText.setText(currentReminder.getDescription());


            TextView timeText = (TextView) itemView.findViewById(R.id.item_textTime);
            timeText.setText("" + currentReminder.getFormatedDateAndTime());

            return itemView;
        }
    }
    private ListView populateListView(View itemView) {

        ArrayAdapter<Reminder> adapter = new MyListAdapter();
        ListView list = (ListView) itemView.findViewById(R.id.eventListView);
        list.setAdapter(adapter);

        return list;
    }

}
