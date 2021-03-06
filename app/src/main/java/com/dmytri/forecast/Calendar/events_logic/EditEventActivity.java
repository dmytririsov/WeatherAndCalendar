package com.dmytri.forecast.Calendar.events_logic;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dmytri.forecast.Calendar.CalendarFragment;
import com.dmytri.forecast.Calendar.alarm_logic.AlarmActivity;
import com.dmytri.forecast.Calendar.data.EventsModel;
import com.dmytri.forecast.Preference;
import com.dmytri.weather.R;

public class EditEventActivity extends AppCompatActivity {


    private Button mEventButtonAlarm;
    private Button mEventButtonSubmit;
    private TextView mEventDate;
    private EditText mEventEditText;
    private String event_description;
    private String event_spinner;
    private String event_date;
    private final String[] mEventType = {"Meeting", "Birthday", "Reminder"};
    private final static String TAG = EditEventActivity.class.getSimpleName();
    private String month;
    private int position;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent.getStringExtra(CalendarFragment.MONTH_INTENT) != null
                && !intent.getStringExtra(CalendarFragment.MONTH_INTENT).isEmpty()
                && intent.getIntExtra(CalendarFragment.POSITION_INTENT, 1) != 1 ) {
            month = intent.getStringExtra(CalendarFragment.MONTH_INTENT);
            position = intent.getIntExtra(CalendarFragment.POSITION_INTENT, 1);
            Preference.getInstance(this).setCurrentMonth(month);
            Preference.getInstance(this).setCurrentPosition(position);
        }
        else {
            month = Preference.getInstance(this).getCurrentMonth();
            position = Preference.getInstance(this).getCurrentPosition();
        }

        mEventButtonAlarm = (Button) findViewById(R.id.button_set_alarm);
        mEventButtonSubmit = (Button) findViewById(R.id.event_button_submit);
        mEventDate = (TextView) findViewById(R.id.event_details_date);
        mEventDate.setText(month + ", " + position);
        mEventEditText = (EditText) findViewById(R.id.event_details_edit_text);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, mEventType);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        final Spinner spinner = (Spinner) findViewById(R.id.event_details_spinner);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Event details");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mEventButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick mEventButtonSubmit");
                event_description = mEventEditText.getText().toString().isEmpty() ? "Description empty" : mEventEditText.getText().toString();
                event_spinner = spinner.getSelectedItem().toString();
                event_date = month + ", " + Integer.toString(position);
                EventsModel eventsModel = new EventsModel(event_description, event_spinner, event_date);
                eventsModel.save();
                Toast.makeText(getBaseContext().getApplicationContext(), "Event was added", Toast.LENGTH_SHORT).show();
                Intent intentSubmit = new Intent(v.getContext(), EventsListActivity.class);
                intentSubmit.putExtra(CalendarFragment.POSITION_INTENT, position);
                intentSubmit.putExtra(CalendarFragment.MONTH_INTENT, month);
                v.getContext().startActivity(intentSubmit);
            }
        });
        mEventButtonAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick mEventButtonAlarm");
                Intent intentAlarm = new Intent(v.getContext(), AlarmActivity.class);
                intentAlarm.putExtra(CalendarFragment.POSITION_INTENT, position);
                intentAlarm.putExtra(CalendarFragment.MONTH_INTENT, month);
                v.getContext().startActivity(intentAlarm);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

