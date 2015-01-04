package com.example.biorobot.memorymanager2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
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
import java.util.List;

public class ListViewFragment extends Fragment {
    /**
     * reassign list in savedInstanceState?
     * or simply move it there?
     */
    ListView item_list;
    View itemView;
    //make new reminder
    Button createReminderButton;

    Button clearButton;

    //saves position when user clicks on a listitem.
    //assigned to zero just to be sure.
    int mPosition = 0;

    List<Reminder> reminder_list = new ArrayList<Reminder>();
    Communicator comm;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * need to save communicator in saveinstancestate?
         */
        comm = (Communicator) getActivity();
    }

    public void getReminder(Reminder reminderGet) {
        Log.i("inside reminder", reminderGet.getType());
        reminder_list.add(reminderGet);
    }
    public void replaceReminder(Reminder pushReminder) {
        //"returns" a Reminder from the Event fragment by replacing the old one in the list
        reminder_list.set(mPosition, pushReminder);

        Log.i("inside replaceReminder. What's the reminder? ", reminder_list.get(mPosition).getType());
    }
    public void removeReminder() {
        //deletes the reminder at position
        reminder_list.remove(mPosition);
        Log.i("inside removeReminder", "removed reminder at position: " + mPosition+"");
        //need to do this still even as we now have SQL database?
        item_list.deferNotifyDataSetChanged();
        //doesn't remove the listview item. its still an empty lisview item.
        //needs to remove from database too.
        ReminderDbHelper mDbHelper = new ReminderDbHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("DELETE FROM Reminder WHERE reminderid = " + mPosition);
    }

    @Override
    public void onPause() {
        /**
         * Inside here is the database runtime logic.
         */
        //super runs the original unedited class onPause() first, then we can run our special override code
        super.onPause();
        Log.i("Inside onPause", "PAUSE");
        //save those reminders in the database onPause so the app doesn't wipe the data onquit.
        //data now lives on even if the app is shut down. Data also lives on for new versions of the app..
        ReminderDbHelper mDbHelper = new ReminderDbHelper(getActivity());
        //create a reference to the database object..
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //deletes all records there are, but keeps the table ?? or?
        //cleans out the old database, and resaves it all again
        //
        //

        db.execSQL("DELETE FROM Reminder;");
        /**
         * need to do this since we don't have a unique constraint. Otherwise the old Reminders would
         * not be able to be stored back into the database.
         *
         */

        //now we add all new reminders from the temporary list.
        for(int x = 0; x < reminder_list.size(); x++) {
            /**
             * Log everything in DB now that the old database is cleansed
             */
            //get reminder from the list, and divide up it's data for pushing to the database.
            Reminder r = reminder_list.get(x);
            ContentValues values = new ContentValues();
            values.put(ReminderEntry.COLUMN_NAME_REMINDER_ID, x);
            values.put(ReminderEntry.COLUMN_NAME_TYPE, r.getType());
            values.put(ReminderEntry.COLUMN_NAME_DESCRIPTION, r.getDescription());
            values.put(ReminderEntry.COLUMN_NAME_TIME, r.getTime());
            try {
                db.insert(
                        ReminderEntry.TABLE_NAME,
                        null,
                        values);
                Log.v("inside try clause .. ", r.toString());
            } catch (Exception e) {
                Log.e("reminder_save", e.getMessage());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Inside onResume", "RESUME");
        try
        {
            //don't need to wipe the list
            if(reminder_list != null) {
                //reminder_list.clear();
            }
            else {
                reminder_list = new ArrayList<Reminder>();
            }
            //Database needs to be onResume so it can load up the saved data.
            ReminderDbHelper mDbHelper = new ReminderDbHelper(getActivity());
            //helper is the definition of the structure of the database..
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            //defines which columns are read from the database.
            //all columns are currently set to be read. This is formality to work with a database.
            /**
             * An alternative is
             */
            String [] projection = {
                    ReminderEntry.COLUMN_NAME_TYPE,
                    ReminderEntry.COLUMN_NAME_DESCRIPTION,
                    ReminderEntry.COLUMN_NAME_TIME,
                    ReminderEntry.COLUMN_NAME_REMINDER_ID
            };

            // reminder position in ascending order
            String sortOrder =
                    ReminderEntry.COLUMN_NAME_REMINDER_ID + " ASC";

            //the query will look as such: SELECT type, description, time, reminderid FROM Reminder
            //ORDER BY reminderid ASC
            Cursor c = db.query(
                    ReminderEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );

            //move cursor to the first row..
            //cursor is like an iterator for
            if(c.moveToFirst())
            {
                do {
                    //gets data from the database at the cursor location.
                    int typeColIndex = c.getColumnIndex(ReminderEntry.COLUMN_NAME_TYPE);
                    String type = c.getString(typeColIndex);

                    int descColIndex = c.getColumnIndex(ReminderEntry.COLUMN_NAME_DESCRIPTION);
                    String desc = c.getString(descColIndex);

                    int timeColIndex = c.getColumnIndex(ReminderEntry.COLUMN_NAME_TIME);
                    String time = c.getString(timeColIndex);

                    Reminder r = new Reminder(type,desc,time);


                    int reminderIdColIndex = c.getColumnIndex(ReminderEntry.COLUMN_NAME_REMINDER_ID);

                    /**
                     * TODO: Add id to reminder so we can see which reminder is where and it doesn't
                     * add new reminder for no reason.
                     */
                    //cursor gets the id from the database. Id used formally to identify Reminders in the database.
                    //Next up is to add as value in the Reminder.java class?
                    int reminderID = c.getInt(reminderIdColIndex);

                    /**
                     * The database has a id for every row, called key. Doesn't exist in the UI
                     */

                    //reminders have ID 1 2 3 4 depending on position in list. We don't want reminders that are
                    //not in the list, so check for reminders that are not ID larger than the list.size()
                    if (reminderID >= reminder_list.size()) {
                        /**
                         * An alternative solution is to have an ID integer value in the Reminder class.
                         * Now the ID is in the database. Everything works nice now however so maybe not needed.
                         */
                        reminder_list.add(r);
                    }
                    /**
                     * How do we know that we want a reminder which is not already in the list?
                     * we need a unique "divider"
                     */
                    //don't need this.. it didn't do anything.. delete?
                    //item_list.deferNotifyDataSetChanged();
                } while(c.moveToNext()); //runs while there are rows in the cursor..

                c.close();
                // coninute to the next row
            }
        } catch (Exception e) {
            Log.e("ListViewFragment_OnResume", e.getMessage());
        }

    }

    /**
     * onSaveInstanceState to handle orientation changes
     *
     * All it needs to save is the reminder_list
     *
     * counter == reminder_list
     */
    @Override
    public void onSaveInstanceState(Bundle saveData) {
        super.onSaveInstanceState(saveData);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            //data assignment?
        }
        else {
            //data loading??
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null)
        {
            /**
             * list refresh
             */
            if (item_list != null){
                /**
                 * just crap, does nothing..
                 */
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
                /**
                 * this is the CLEAR BUTTON
                 */
                @Override
                public void onClick(View view) {
                    try {
                        ReminderDbHelper mDbHelper = new ReminderDbHelper(getActivity());
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();

                        db.execSQL("DELETE FROM Reminder;");
                        reminder_list.clear();
                        /**
                         * this doesn't work somehow. Clears list all the time?
                         * Never clears onbuttonclick?
                         */

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
                     /**
                     * this is the LIST ITEM
                     */
                    String toast_text = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getActivity().getApplicationContext(), toast_text, Toast.LENGTH_LONG).show();

                    Log.v("inside ListViewFragment onItemClick. Position == ", position + "");
                    EventCommunicator eventComm = (EventCommunicator) getActivity();

                    /**
                     * need to get activity maybe?
                     */
                    Reminder ful_reminder = reminder_list.get(position);
                    mPosition = position;
                    eventComm.readReminder(ful_reminder);
                }
            });

        }
        return itemView;
    }

    private void addReminders() {
        reminder_list.add(new Reminder("Lunch", "Eat Lunch", "12.00"));
        reminder_list.add(new Reminder("Walk", "Take a late walk..", "20.40"));
        reminder_list.add(new Reminder("Early Train", "Get on the early train in time!", "4.20"));
        reminder_list.add(new Reminder("Gym", "go to the Gym!", "15.5"));
        reminder_list.add(new Reminder("Present", "Buy present for wife", "15.55"));
    }
    /**
     * this class is for pushing every generated item xml to the list..
     */
    private class MyListAdapter extends ArrayAdapter<Reminder> {
        public MyListAdapter() {
            super(getActivity(), R.layout.item_view, reminder_list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null){
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            Reminder currentReminder = reminder_list.get(position);
            System.out.println(currentReminder.getType());

            TextView typeText = (TextView) itemView.findViewById(R.id.item_textType);
            typeText.setText(currentReminder.getType());

            TextView descText = (TextView) itemView.findViewById(R.id.item_textDesc);
            descText.setText(currentReminder.getDescription());

            TextView timeText = (TextView) itemView.findViewById(R.id.item_textTime);
            timeText.setText("" + currentReminder.getTime());

            return itemView;
        }
    }
    /**
     * now we call the listview
     *
     *can't populate like this anymore. need to have an update method instead..
     */
    private ListView populateListView(View itemView) {
        /**
         * this is probably outdated
         */
        ArrayAdapter<Reminder> adapter = new MyListAdapter();
        ListView list = (ListView) itemView.findViewById(R.id.eventListView);
        list.setAdapter(adapter);

        return list;
    }

}
