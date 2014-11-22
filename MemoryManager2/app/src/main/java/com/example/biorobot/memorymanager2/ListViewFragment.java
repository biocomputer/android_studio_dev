package com.example.biorobot.memorymanager2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biorobot.memorymanager2.Event;
import com.example.biorobot.memorymanager2.R;
import com.example.biorobot.memorymanager2.Reminder;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;


public class ListViewFragment extends Fragment{
    ListView item_list;
    View itemView;

    List<Reminder> reminder_list = new ArrayList<Reminder>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        addReminders();

        itemView = inflater.inflate(R.layout.list_view_fragment, container, false);
        item_list = populateListView(itemView);

        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String toast_text = parent.getItemAtPosition(position).toString();
                //parent is that list..

                Toast.makeText(getActivity().getApplicationContext(), toast_text, Toast.LENGTH_LONG).show();


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_layout, new Event());
                fragmentTransaction.commit();

            }
        });

        return itemView;
    }

    private void addReminders() {
        //will be replaced by user addable function
        reminder_list.add(new Reminder("Lunch", "Eat Lunch", 12.00));
        reminder_list.add(new Reminder("Walk", "Take a late walk..", 20.40));
        reminder_list.add(new Reminder("Early Train", "Get on the early train in time!", 4.20));
        reminder_list.add(new Reminder("Gym", "go to the Gym!", 15.5));
        reminder_list.add(new Reminder("Present", "Buy present for wife", 15.55));
    }


    private ListView populateListView(View itemView) {
        ArrayAdapter<Reminder> adapter = new MyListAdapter();
        //hämta inte längre ut getActivity för hämtar inte ut activity main utan item_view istället..
        ListView list = (ListView) itemView.findViewById(R.id.eventListView);
        list.setAdapter(adapter);

        return list;
    }

    private class MyListAdapter extends ArrayAdapter<Reminder> {
        public MyListAdapter(){
            super(getActivity(), R.layout.item_view, reminder_list);
        }

        //ArrayAdapter<String> itemsAdapter =
        //        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null){
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            //find the car to work with.
            Reminder currentReminder = reminder_list.get(position);
            System.out.println("Remind!!!");
            System.out.println(currentReminder.getType());

            //fill the view
            /*ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
            imageView.setImageResource(currentCar.getIconID());*/

            TextView typeText = (TextView) itemView.findViewById(R.id.item_textType);
            typeText.setText(currentReminder.getType());

            TextView descText = (TextView) itemView.findViewById(R.id.item_textDesc);
            descText.setText(currentReminder.getDescription());

            TextView timeText = (TextView) itemView.findViewById(R.id.item_textTime);
            timeText.setText("" + currentReminder.getTime());
            //empty string for recasting double value


            return itemView;
            //return super.getView(position, convertView, parent);
        }
    }
}
