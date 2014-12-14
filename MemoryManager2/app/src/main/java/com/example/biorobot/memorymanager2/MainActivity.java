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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
//import com.amazon.insights.*;


public class MainActivity extends Activity implements Communicator, AddReminderButtonCommunicator {
    ListViewFragment fragmentListView;
    CreateReminderFragment fragmentCreateReminder;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();

        //fragment variables defined before transaction this time
        fragmentListView = (ListViewFragment) fragmentManager.findFragmentById(R.id.list_view_layout);

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
        getFragmentManager().popBackStack();

       // getFragmentManager().beginTransaction()
         //       .add(R.id.item_view_layout,this.fragmentListView,"listFragTag")
         //       .commit();

    }

    //AddReminderButtonCommunicator
    public void onCreateReminderClicked() {
        //blah
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
