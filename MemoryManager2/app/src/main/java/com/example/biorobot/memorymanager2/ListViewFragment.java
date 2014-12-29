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

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Inside onResume", "RESUME");
        try
        {
            // kolla så listan finns, annars skapa om den
            if(reminder_list != null) {
                //reminder_list.clear();
            }
            else {
                reminder_list = new ArrayList<Reminder>();
            }
            // läs in reminders
            ReminderDbHelper mDbHelper = new ReminderDbHelper(getActivity());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            // kolumner som vi vill ha värdet på
            String [] projection = {
                    ReminderEntry.COLUMN_NAME_TYPE,
                    ReminderEntry.COLUMN_NAME_DESCRIPTION,
                    ReminderEntry.COLUMN_NAME_TIME,
                    ReminderEntry.COLUMN_NAME_REMINDER_ID
            };

            // reminder position i stigande ordning (ASCENDING)
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
                    String time = c.getString(timeColIndex);

                    Reminder r = new Reminder(type,desc,time);

                    //fullösning för reminder check så den inte multiplicerar listan hela tiden
                    /**
                     * TODO: Add id to reminder so we can see which reminder is where and it doesn't
                     * add new reminder for no reason.
                     */
                    int reminderIdColIndex = c.getColumnIndex(ReminderEntry.COLUMN_NAME_REMINDER_ID);

                    //cursor gets the id from the database.
                    int reminderID = c.getInt(reminderIdColIndex);

                    /**
                     * databasen har ett skiljeid för att identifiera varje rad unik. Kallas nyckel i databas
                     * men det finns inte i gränssnittet i appen, så den bara pushar på varje gång utan att veta.
                     */

                    // lägg till remindern så den dyker upp i gränssnittet
                    //första reminder har id 1, sen 2 3 4 5, den kommer kolla så den inte pushar id som inte finns.
                    if (reminderID >= reminder_list.size()) {
                        //ett och två finns i listan, vi har redan i listan så den pushar inte igen.
                        /**
                         * funkar!!! är en fullösning dock! Ändra sen!.
                         *
                         * För att ändra om det här till en snygglösning. Ändra så att Reminder klassen innehåller Reminder id.
                         * Problemet är att då när man skapar en ny Reminder, så vet vi inte vilken Reminder som finns i databasen.
                         *
                         *
                         * För det här ändamålet: den här lösningen funkar. Men den kommer ställa till problem om man måste ta bort grejor.
                         *
                         */
                        reminder_list.add(r);
                    }
                    //hur vet vi att en reminder vi vill ha inte redan finns i listan?
                    //vi behöver ha en unik skiljebrytare.
                    //ful lösning först
                    //item_list.deferNotifyDataSetChanged();
                } while(c.moveToNext()); //medan vi har rader kvar i cursorn

                c.close();
                // fortsätt till nästa rad
            }
        } catch (Exception e) {
            Log.e("ListViewFragment_OnResume", e.getMessage());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Inside onPause", "PAUSE");
        // spara reminders i databasen
        ReminderDbHelper mDbHelper = new ReminderDbHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM Reminder;");

        for(int x = 0;x< reminder_list.size(); x++) {
            /**
             * perhaps it is this that makes the listitems double on every new add.
             * it runs through the code for each listitem, of course if you do it like that
             * it will double every time.
             *
             * Why is there even a loop here? Couldn't this be done just straight?
             *
             * Or it's to recreate a dead list?
             *
             *
             */
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

                        //sends query?
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

                    EventCommunicator eventComm = (EventCommunicator) getActivity();

                    /**
                     * need to get activity maybe?
                     */
                    Reminder ful_reminder = reminder_list.get(position);
                    mPosition = position;
                    Log.i("inside ListViewFragment onItemClick. Position == ", position + "");
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
