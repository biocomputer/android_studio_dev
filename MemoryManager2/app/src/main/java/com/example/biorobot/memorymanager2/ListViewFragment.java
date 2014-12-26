package com.example.biorobot.memorymanager2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
        //Gonna need to overload this..
        //saveData.putReminderArrayList("reminder_list", reminder_list);
        /**
         * overload saveData and make new function?
         */
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
            List<Reminder> reminder_list = new ArrayList<Reminder>();
            /**
             * list refresh
             */
            if (item_list != null){
                item_list.deferNotifyDataSetChanged();
            }

            itemView = inflater.inflate(R.layout.list_view_fragment, container, false);
            createReminderButton = (Button) itemView.findViewById(R.id.addReminderButton);

            createReminderButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * this is the BUTTON
                 */
                @Override
                public void onClick(View view) {
                    Log.i("BUTTONclicked!", "true");
                    AddReminderButtonCommunicator a = (AddReminderButtonCommunicator)getActivity();
                    a.onCreateReminderClicked();
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
