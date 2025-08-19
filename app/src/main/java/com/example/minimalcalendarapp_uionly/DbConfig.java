package com.example.minimalcalendarapp_uionly;

public class DbConfig {
    // Schema identifiers are not secrets; keep them here for clarity.
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password"; // stores hash

    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_EVENT_ID = "id";
    public static final String COLUMN_EVENT_TITLE = "title";
    public static final String COLUMN_EVENT_DATE = "date";

    public static final String IDX_USERS_USERNAME = "idx_users_username";
    public static final String IDX_EVENTS_DATE = "idx_events_date";

    // Pull non-sensitive config values from BuildConfig (not hardcoded).
    // NOTE: BuildConfig values compile into the APK. Use placeholders for anything secret in production.
    public static String getDatabaseName() {
        return BuildConfig.DB_NAME;       // e.g., "calendarApp.db" injected via Gradle
    }

    public static int getDatabaseVersion() {
        return BuildConfig.DB_VERSION;    // 2 (version 2 for the injected valueys)
    }

    // Potentially sensitive or environment-specific values will be injected, not hardcoded anymore.
    public static String getApiBaseUrl() {
        return clean(BuildConfig.API_BASE_URL);
    }

    public static String getMongoUri() {
        return clean(BuildConfig.MONGO_URI);
    }

    public static String getSentryDsn() {
        return clean(BuildConfig.SENTRY_DSN);
    }

    private static String clean(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }
}
