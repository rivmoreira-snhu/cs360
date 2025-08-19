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

/**
 * CalendarActivity shows and manages events.
 * Enhancements include proper DB deletion, UI feedback for empty state,
 * and preparation for RecyclerView refactor.
 */
public class CalendarActivityEnhanced extends AppCompatActivity {
    private LinearLayout eventList;
    private DatabaseHelper dbHelper;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        eventList = findViewById(R.id.eventList);
        Button addEventButton = findViewById(R.id.addEventButton);
        TextView currentDateView = findViewById(R.id.currentDate);
        dbHelper = new DatabaseHelper(this);

        currentDate = java.text.DateFormat.getDateInstance().format(new java.util.Date());
        currentDateView.setText(currentDate);

        loadEventsFromDatabase();

        addEventButton.setOnClickListener(view -> {
            boolean success = dbHelper.addEvent("New Event", currentDate);
            if (success) {
                loadEventsFromDatabase();
                Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error adding event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEventsFromDatabase() {
        eventList.removeAllViews();
        Cursor cursor = dbHelper.getAllEvents();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_EVENT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_EVENT_TITLE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_EVENT_DATE));
                inflateEventItem(id, title, date, "TBD");
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            TextView emptyState = new TextView(this);
            emptyState.setText("No events available. Tap '+' to add one.");
            eventList.addView(emptyState);
        }
    }

    private void inflateEventItem(int eventId, String title, String time, String location) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View eventItem = inflater.inflate(R.layout.event_item, eventList, false);

        TextView titleView = eventItem.findViewById(R.id.eventTitle);
        TextView timeView = eventItem.findViewById(R.id.eventTime);
        TextView locationView = eventItem.findViewById(R.id.eventLocation);
        Button deleteButton = eventItem.findViewById(R.id.deleteButton);

        titleView.setText(title);
        timeView.setText(time);
        locationView.setText(location);

        deleteButton.setOnClickListener(v -> {
            boolean deleted = dbHelper.deleteEvent(eventId);
            if (deleted) {
                Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
                loadEventsFromDatabase();
            } else {
                Toast.makeText(this, "Error deleting event", Toast.LENGTH_SHORT).show();
            }
        });

        eventList.addView(eventItem);
    }
}
