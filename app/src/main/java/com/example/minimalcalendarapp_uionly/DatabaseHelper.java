package com.example.minimalcalendarapp_uionly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite database helper for CS360 app.
 * Milestone 4 updates: schema constraints, indices, and non-destructive migration (v1 -> v2).
 */
public class DatabaseHelperEnhanced extends SQLiteOpenHelper {
    //Updating to use the injected versions
    public DatabaseHelperEnhanced(Context context) {
        super(context, DbConfig.getDatabaseName(), null, DbConfig.getDatabaseVersion());
        // Improves concurrent reads during writes
        try { setWriteAheadLoggingEnabled(true); } catch (Throwable ignored) {}
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table with constraints
        String createUsers = "CREATE TABLE " + DbConfig.TABLE_USERS + " (" +
                DbConfig.COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbConfig.COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                DbConfig.COLUMN_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(createUsers);
        db.execSQL("CREATE INDEX IF NOT EXISTS " + DbConfig.IDX_USERS_USERNAME +
                " ON " + DbConfig.TABLE_USERS + " (" + DbConfig.COLUMN_USERNAME + ")");

        // Events table with constraints
        String createEvents = "CREATE TABLE " + DbConfig.TABLE_EVENTS + " (" +
                DbConfig.COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbConfig.COLUMN_EVENT_TITLE + " TEXT NOT NULL, " +
                DbConfig.COLUMN_EVENT_DATE + " TEXT NOT NULL)";
        db.execSQL(createEvents);
        db.execSQL("CREATE INDEX IF NOT EXISTS " + DbConfig.IDX_EVENTS_DATE +
                " ON " + DbConfig.TABLE_EVENTS + " (" + DbConfig.COLUMN_EVENT_DATE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Migrate users
            db.execSQL("ALTER TABLE " + DbConfig.TABLE_USERS + " RENAME TO users_old");
            // Recreate tables with new constraints and indices
            onCreate(db);
            // Migrate user data; skip rows with null username/password
            db.execSQL("INSERT OR IGNORE INTO " + DbConfig.TABLE_USERS + " (" +
                    DbConfig.COLUMN_USERNAME + ", " + DbConfig.COLUMN_PASSWORD + ") " +
                    "SELECT " + DbConfig.COLUMN_USERNAME + ", " + DbConfig.COLUMN_PASSWORD + " FROM users_old WHERE " +
                    DbConfig.COLUMN_USERNAME + " IS NOT NULL AND " + DbConfig.COLUMN_PASSWORD + " IS NOT NULL");
            db.execSQL("DROP TABLE IF EXISTS users_old");

            // Note: events table in v1 was already simple; if it existed, ensure data remains.
            // If an old events table existed without rename (fresh install), nothing to migrate.
        }
    }

    // Updated function to use the new injected values and to better handle errors and exceptions
    // Adds a new user with hashed password
    public boolean addUser(String username, String plainPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbConfig.COLUMN_USERNAME, username);
        values.put(DbConfig.COLUMN_PASSWORD, SecurityUtils.hashPassword(plainPassword));
        long result = db.insertWithOnConflict(
                DbConfig.TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return result != -1;
    }

    // Validates login credentials
    public boolean checkUserCredentials(String username, String plainPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashed = SecurityUtils.hashPassword(plainPassword);
        String q = "SELECT 1 FROM " + DbConfig.TABLE_USERS + " WHERE " +
                DbConfig.COLUMN_USERNAME + "=? AND " + DbConfig.COLUMN_PASSWORD + "=? LIMIT 1";
        Cursor c = db.rawQuery(q, new String[]{username, hashed});
        boolean ok = c != null && c.moveToFirst();
        if (c != null) c.close();
        db.close();
        return ok;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor c = db.rawQuery(q, new String[]{username})) {
            return c.moveToFirst();
        }
    }

    // Event CRUD - updated for injection data
    public boolean addEvent(String title, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbConfig.COLUMN_EVENT_TITLE, title);
        values.put(DbConfig.COLUMN_EVENT_DATE, date);
        long result = db.insertWithOnConflict(
                DbConfig.TABLE_EVENTS, null, values, SQLiteDatabase.CONFLICT_ABORT);
        return result != -1;
    }


    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT * FROM " + DbConfig.TABLE_EVENTS + " ORDER BY " + DbConfig.COLUMN_EVENT_DATE + " ASC";
        return db.rawQuery(q, null);
    }

    public boolean updateEvent(int id, String newTitle, String newDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbConfig.COLUMN_EVENT_TITLE, newTitle);
        values.put(DbConfig.COLUMN_EVENT_DATE, newDate);
        int rows = db.update(DbConfig.TABLE_EVENTS, values, DbConfig.COLUMN_EVENT_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public boolean deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(DbConfig.TABLE_EVENTS, DbConfig.COLUMN_EVENT_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }
    //Adding Foreign keys enabled
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
