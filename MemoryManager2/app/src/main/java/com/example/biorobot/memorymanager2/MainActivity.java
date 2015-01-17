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
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.amazon.insights.*;
import com.amazon.insights.error.InsightsError;


public class MainActivity extends Activity implements Communicator,
        AddReminderButtonCommunicator, EventCommunicator,
        OnTimeSetCommunicator, OnDateSetCommunicator {
    ListViewFragment fragmentListView;
    CreateReminderFragment currentCreateReminderFragment;
    Event fragmentEvent;

    FragmentManager fragmentManager;

    // see https://developer.amazon.com/public/apis/manage/ab-testing/doc/a-b-testing-for-android-fire-os
    // for more information about amazon ab testing

    public static final String AMAZON_PUBLIC_KEY = "43987f10f024480c8f6447607cf58b26";
    public static final String AMAZON_PRIVATE_KEY = "/TzvGgh+o0n69l93goJWmgorbEAzzQnaaYUI40AxxJs=";

    private ABTestClient abClient;
    private EventClient eventClient;


    private int ABTestLayoutMode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();

        //fragment variables defined before transaction
        fragmentListView = (ListViewFragment) fragmentManager.findFragmentById(R.id.list_view_layout);

        fragmentEvent = (Event) fragmentManager.findFragmentById(R.id.event_layout);


        //A/B Testing

        InsightsCredentials credentials = AmazonInsights.newCredentials(AMAZON_PUBLIC_KEY, AMAZON_PRIVATE_KEY);
        InsightsOptions options = AmazonInsights.newOptions(true, true);
        AmazonInsights insightsInstance = AmazonInsights.newInstance(credentials, getApplicationContext(),options);
        abClient = insightsInstance.getABTestClient();
        eventClient = insightsInstance.getEventClient();

        ListViewFragment listFragment = new ListViewFragment();
        fragmentListView = listFragment;
        getFragmentManager().beginTransaction().replace(R.id.main_layout, listFragment, "listFragTag")
                //.addToBackStack("mainFragTag")
                .commit();


        /**
         * here is more amazon AB test framework code
         */
        abClient.getVariations("MemoryManager")
                .setCallback(new InsightsCallback<VariationSet>() {
                    @Override
                    public void onComplete(VariationSet variations) {
                        try {

                            final Variation createReminderVariation = variations.getVariation("MemoryManager");
                            if(createReminderVariation.containsVariable("ReminderGroup")) {
                               String reminderGroup = createReminderVariation.getVariableAsString("ReminderGroup", "1.0");
                                ABTestLayoutMode = (int)Double.parseDouble(reminderGroup);
                            }

                            Log.i("Got Layout From Amazon: ", Integer.toString(ABTestLayoutMode));
                        } catch(Exception ex) {
                            Log.e("Amazon.Insight.Callback", ex.getMessage());
                        }

                    }

                    @Override
                    public void onError(InsightsError error) {
                        super.onError(error);
                        Log.e("InsightsCallback.OnError", "Error from amazon\n" + error.getMessage());
                    }
                });

        // Visit event starts the AB Test
        com.amazon.insights.Event abTestStart = eventClient.createEvent("buttonTextStart");

        eventClient.recordEvent(abTestStart);

    }


    @Override
    protected void onPause() {
        super.onPause();

        eventClient.submitEvents();
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
        this.fragmentListView.getReminder(pushThisReminder);
        com.amazon.insights.Event reminderCreatedEvent = eventClient.createEvent("reminderCreated");
        eventClient.recordEvent(reminderCreatedEvent);

        fragmentManager.popBackStack();

    }
    //eventCommunicator
    public void readReminder(Reminder getReminder) {
        fragmentEvent = new Event();

        fragmentManager.beginTransaction()
                .replace(R.id.main_layout, fragmentEvent, "eventFragTag")
                .addToBackStack(null)
                .commit();

        fragmentEvent.pullReminder(getReminder);
    }

    public void returnReminder(Reminder pushReminder) {

        this.fragmentListView.replaceReminder(pushReminder);

        fragmentManager.beginTransaction()
                .replace(R.id.main_layout, fragmentListView)
                .addToBackStack(null)
                .commit();
    }
    public void deleteReminder() {
        /**
         * need to search in list for item to delete? doesn't it remember the list position?
         * it's possible to change so should be highly possible to delete, just do .delete(pos)
         * instead of .change(pos);
         */
        Log.i("inside ", "deleteReminder");
        this.fragmentListView.removeReminder();
        fragmentManager.beginTransaction()
                .replace(R.id.main_layout, fragmentListView)
                .addToBackStack(null)
                .commit();

    }

    //AddReminderButtonCommunicator
    public void onCreateReminderClicked() {
        //changes fragment from listview to the reminder creating fragment,
        //this is not for looking at individual Reminders, that is for readReminder and
        //the Event fragment class.
        CreateReminderFragment reminderFragment = new CreateReminderFragment();
        currentCreateReminderFragment = reminderFragment;
        reminderFragment.ABLayoutMode = ABTestLayoutMode;
        fragmentManager.beginTransaction()
                .replace(R.id.main_layout, reminderFragment, "createReminderFragTag")
                .addToBackStack(null)
                .commit();
        //Log.i("after createReminder, what is tag for eventFragment? ", R.id.list_view_layout);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(currentCreateReminderFragment != null) {
            currentCreateReminderFragment.updateTime(hourOfDay, minute);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if(currentCreateReminderFragment != null) {
            currentCreateReminderFragment.updateDate(year,month,day);
        }
    }
}
