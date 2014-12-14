package com.example.biorobot.memorymanager2;

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
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by biorobot on 2014-11-27.
 *
 * Total chaos inside here..
 */
public class CreateReminderFragment extends Fragment {

    View createReminderView;
    EditText makeType, makeDesc, makeTime;

    Button addReminderButton;

    public CreateReminderFragment() {
            super();
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("now inside creaRemidnerFragment", "LAUNCHED!");
        createReminderView = (View) inflater.inflate(R.layout.create_reminder_fragment, container, false);
        //linking the code with the xml for editing data values later..

        makeType = (EditText) createReminderView.findViewById(R.id.create_reminder_make_type);
        makeDesc = (EditText) createReminderView.findViewById(R.id.create_reminder_make_desc);
        makeTime = (EditText) createReminderView.findViewById(R.id.create_reminder_make_time);

        addReminderButton = (Button) createReminderView.findViewById(R.id.create_reminder_add_button);
        /**
         * Want to make it add stuff by listeners here...
         */
        makeType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //its a bool so we need .isEmpty() at the end. We reverse the test with !
                addReminderButton.setEnabled(!makeType.getText().toString().trim().isEmpty());
                //is it disabled before?
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //anything here??
            }

            @Override
            public void afterTextChanged(Editable s) {
                //anything here?
            }
        });

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("blah","blah");
                Toast.makeText(getActivity(), "Your Contact has been created!", Toast.LENGTH_LONG).show();
                /**
                 * TODO: Add checks here for data, if needed
                 */
                Reminder reminder = new Reminder(makeType.getText().toString().trim(), makeDesc.getText().toString().trim(), makeTime.getText().toString().trim());
                //is this actually pushing the reminder? Yes.
                Communicator comm = (Communicator) getActivity();
                comm.transferReminder(reminder);
                //why doesn't it push the reminder, its empty when check. Hey man check this:
                Log.i("hey is this reminder active, we are inside ONCLICKLISTENER: ", reminder.getType());
                Log.i("it works!!!", "YAY");
            }
        });
        /**
         * wants to return some kind of view..
         */
        return createReminderView;

    }

}