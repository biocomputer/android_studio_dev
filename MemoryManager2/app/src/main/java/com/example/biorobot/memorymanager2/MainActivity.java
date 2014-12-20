package com.example.biorobot.memorymanager2;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
//import com.amazon.insights.*;


public class MainActivity extends Activity implements Communicator, AddReminderButtonCommunicator, EventCommunicator{
    ListViewFragment fragmentListView;
    CreateReminderFragment fragmentCreateReminder;
    Event fragmentEvent;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * segfaultar ändå.. varför..
         */

        fragmentManager = getFragmentManager();

        //fragment variables defined before transaction this time
        fragmentListView = (ListViewFragment) fragmentManager.findFragmentById(R.id.list_view_layout);

        //creating an eventFragment here since it works for listview fragment. This might solve the nullpointer exception.
        fragmentEvent = (Event) fragmentManager.findFragmentById(R.id.event_layout);

        /**
         * Fragment fragmentA = new FragmentA();
         *getFragmentManager().beginTransaction()
         *.replace(R.id.MainFrameLayout,fragmentA,"YOUR_TARGET_FRAGMENT_TAG")
         *.addToBackStack("YOUR_SOURCE_FRAGMENT_TAG").commit();
         *
         * list:
         * List<Fragment> allFragments = getSupportFragmentManager().getFragments();
         */
        ListViewFragment listFragment = new ListViewFragment();
        //refers to the listFragment so it can be used seperately from the listFragment, which is pushed with the manager..
        this.fragmentListView = listFragment;
        getFragmentManager().beginTransaction().add(R.id.main_layout, listFragment, "listFragTag")
                //.addToBackStack("mainFragTag")
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * COMMUNICATORS
     */

    //Communicator
    public void transferReminder(Reminder pushThisReminder) {
        Log.i("man we are inside TRANSFER REMINDER", "PUSHTHISREMINDER");

        Log.i("What is pushThisReminder.getType() : ", pushThisReminder.getType().toString());
        this.fragmentListView.getReminder(pushThisReminder);
        Log.i("end of TRANSFER REMINDER", "reminder pushed?");

        //popbackstack??
        getFragmentManager().popBackStack();

       // getFragmentManager().beginTransaction()
         //       .add(R.id.item_view_layout,this.fragmentListView,"listFragTag")
         //       .commit();

    }
    //eventCommunicator
    public void readReminder(Reminder getReminder) {
        Log.i("WE ARE INSIDE readReminder", "    ================= ");

        Log.i("=!!==!=!=!!=!                ÖÖÖÖÖ   ", "===LOCAL REMINDER FROM READREMINDER ===");

        fragmentEvent = new Event();

        fragmentManager.beginTransaction()
                .replace(R.id.main_layout, fragmentEvent, "eventFragTag")
                .commit();

        fragmentEvent.pullReminder(getReminder);
        Log.i("after readReminder, what is tag for eventFragment? ", fragmentEvent.getTag());
    }

    //AddReminderButtonCommunicator
    public void onCreateReminderClicked() {
        //changes fragment from listview to the reminder creating fragment,
        //this is not for looking at individual Reminders, that is for readReminder and
        //the Event fragment class.
        Log.i("before createReminder Fragment", "okay");
        CreateReminderFragment reminderFragment = new CreateReminderFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.main_layout, reminderFragment, "createReminderFragTag")
                .addToBackStack(null)
                .commit();
        Log.i("after createReminder, what is tag for reminderFragment? ", reminderFragment.getTag());
        //Log.i("after createReminder, what is tag for eventFragment? ", R.id.list_view_layout);
    }

}
