package com.example.minimalcalendarapp_uionly;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * This class handles the SQLite database interactions for our app.
 * We're managing users here and will later expand to calendar events.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Basic DB config
    private static final String DATABASE_NAME = "calendarApp.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names for users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Table and column names for events
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_EVENT_ID = "id";
    public static final String COLUMN_EVENT_TITLE = "title";
    public static final String COLUMN_EVENT_DATE = "date";

    // Constructor – gets called with activity context
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the DB is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Set up the user table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Set up the events table
        String createEventsTable = "CREATE TABLE " + TABLE_EVENTS + " ("
                + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EVENT_TITLE + " TEXT, "
                + COLUMN_EVENT_DATE + " TEXT)";
        db.execSQL(createEventsTable);
    }

    // Called when we increment DATABASE_VERSION
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and rebuild both tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    // Adds a new user to the DB – returns true if successful
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1; // -1 means failure
    }

    // Validates a username/password combo
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Inserts a new calendar event into the DB – returns true if successful
    public boolean addEvent(String title, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, title);
        values.put(COLUMN_EVENT_DATE, date);

        long result = db.insert(TABLE_EVENTS, null, values);
        db.close();

        return result != -1;
    }

    // Returns all events from the DB as a Cursor – for reading/display
    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_EVENTS + " ORDER BY " + COLUMN_EVENT_DATE + " ASC";
        return db.rawQuery(query, null);
    }

    // Updates a calendar event by ID – returns true if a row was updated
    public boolean updateEvent(int id, String newTitle, String newDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, newTitle);
        values.put(COLUMN_EVENT_DATE, newDate);

        int rows = db.update(TABLE_EVENTS, values, COLUMN_EVENT_ID + "=?", new String[]{String.valueOf(id)});
        db.close();

        return rows > 0;
    }

    // Deletes a calendar event by ID – returns true if a row was deleted
    public boolean deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rows = db.delete(TABLE_EVENTS, COLUMN_EVENT_ID + "=?", new String[]{String.valueOf(id)});
        db.close();

        return rows > 0;
    }


    // Checks if a user already exists – used for sign-up
    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }
}