# Minimal Calendar App – Project Three

## Overview

The **Minimal Calendar App** is a lightweight Android application designed for users who want to keep track of daily events while maintaining control over permissions and security. This app focuses on a clean interface and basic CRUD (Create, Read, Update, Delete) functionality for events, login persistence using local SQLite storage, and optional SMS notification permissions.


## Features

- **Login & Account Creation**
  - Secure account creation and login via SQLite database.
  - Login state is stored using `SharedPreferences`.

- **Main Navigation Hub**
  - Dynamic buttons shown only when logged in.
  - Options to view calendar, request SMS permissions, or browse the database.

- **Calendar View**
  - Allows users to view and add events.
  - Basic time and location metadata for each entry.
  - Deletion functionality included.

- **Database Viewer**
  - Visual representation of event records.
  - Used for testing and confirmation of data persistence.

- **SMS Permission Handling**
  - Asks for runtime permission to send SMS alerts.
  - Handles both accepted and denied states gracefully.


## Screens

- **LoginActivity**
- **MainActivity (Navigation Hub)**
- **CalendarActivity**
- **DatabaseActivity**
- **SMSPermissionActivity**


## Tech Stack

- **Java (Android)**
- **SQLite**
- **Android Studio XML layouts**
- **SharedPreferences**
- **Runtime Permission API**


## Setup

1. Clone the repository or import the ZIP into Android Studio.
2. Build and run the app on an Android emulator or physical device (API level 21+ recommended).
3. Test login, calendar, and SMS functionality from the main interface.


## Permissions

The app requests the following permission:
- `SEND_SMS` – Only requested when user explicitly navigates to the SMS permission screen.


## Reflections

### App Goals and User Needs

The goal of this app was to create a simple, accessible calendar system that supports basic user login, event tracking, and optional SMS permissions. It targets users who want privacy (via local storage) and an uncluttered UI, while still managing appointments or reminders.

### UI Design Strategy

The interface includes:
- A login screen for access control.
- A minimal main menu that hides or shows options based on login state.
- Event views with clean layouts and intuitive buttons.
- Buttons are disabled when fields are empty to guide proper usage.

All designs kept user flow in mind: from login to scheduling, every screen aims to reduce friction and offer clarity. These choices were successful because they minimized user errors and emphasized essential tasks without distractions.

### Coding Approach

Development followed a modular approach:
- Separation of activities and database logic.
- SharedPreferences handled login persistence.
- Reusable utility methods like `validateInputs()` reduced redundancy.

These techniques helped maintain readability and scalability. I’d reuse these strategies in future mobile apps for clear organization and reusability.

### Testing and Validation

I tested the app on Android Studio’s emulator and ensured each function (login, calendar, SMS permission) worked in isolation and integration. Testing is crucial, it revealed issues like visibility toggles, disabled buttons, and runtime permission handling. Manual UI walkthroughs and database dumps were also useful.

### Innovation and Challenges

The main challenge was managing UI state changes based on login status and ensuring clean transitions between activities. I overcame this using `SharedPreferences` and conditional visibility logic. Also, properly managing runtime permissions without crashing was another key innovation.

### Demonstrated Strengths

My strongest area was building the local database and implementing user authentication and event CRUD logic. These components showcased my understanding of Android’s lifecycle, local storage mechanisms, and user experience principles.

## Author

Angel M. Rivera Moreira  
Project Three

CS360 – Mobile Architect & Programming 