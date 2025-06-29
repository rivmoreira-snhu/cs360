package com.example.minimalcalendarapp_uionly;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    private LinearLayout eventList;
    private Button addEventButton;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        eventList = findViewById(R.id.eventList);
        addEventButton = findViewById(R.id.addEventButton);
        TextView currentDateView = findViewById(R.id.currentDate);
        dbHelper = new DatabaseHelper(this);

        // Format the date to something friendly
        String currentDate = java.text.DateFormat.getDateInstance().format(new java.util.Date());
        currentDateView.setText(currentDate);

        // Load events from database
        loadEventsFromDatabase();

        // Add new dummy event to DB and refresh
        addEventButton.setOnClickListener(view -> {
            boolean success = dbHelper.addEvent("New Event", currentDate);
            if (success) {
                loadEventsFromDatabase(); // refresh the UI list
                Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error adding event", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Pulls all events from the DB and renders them
    private void loadEventsFromDatabase() {
        eventList.removeAllViews(); // clear list before refreshing
        Cursor cursor = dbHelper.getAllEvents();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TITLE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DATE));
                String location = "TBD"; // location can be static for now

                inflateEventItem(title, date, location);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    // Builds a single event UI row and adds to eventList
    private void inflateEventItem(String title, String time, String location) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View eventItem = inflater.inflate(R.layout.event_item, eventList, false);

        TextView titleView = eventItem.findViewById(R.id.eventTitle);
        TextView timeView = eventItem.findViewById(R.id.eventTime);
        TextView locationView = eventItem.findViewById(R.id.eventLocation);
        Button deleteButton = eventItem.findViewById(R.id.deleteButton);

        titleView.setText(title);
        timeView.setText(time);
        locationView.setText(location);

        // Delete button just removes the view (not from DB...yet)
        deleteButton.setOnClickListener(v -> eventList.removeView(eventItem));

        eventList.addView(eventItem);
    }

}
