package com.example.reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String REMINDER_TABLE = "REMINDER_TABLE";
    public static final String colId = "_id";
    public static final String colMessage = "message";
    public static final String colYear = "year";
    public static final String colMonth = "month";
    public static final String colDay = "day";
    public static final String colHour = "hour";
    public static final String colMinute = "minute";
    public static final String colTimeOfDay = "timeOfDay";

    //Constructor
    public DataBaseHelper(@Nullable Context context) {
        super(context, "reminder.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creates the column names for the SQLite table
        String createTableStatement = "CREATE TABLE " + REMINDER_TABLE + " (" + colId +" INTEGER PRIMARY KEY AUTOINCREMENT, " + colMessage +" TEXT, " + colYear +" INTEGER, " + colMonth + " INTEGER, " + colDay + " INTEGER, " + colHour +" INTEGER, " + colMinute + " INTEGER, " + colTimeOfDay + " INTEGER)";
        //Actually runs the SQLite statement
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    //This method adds the values from a Reminder object into the SQLite table
    public boolean addOne(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(colMessage, reminder.getMessage());
        cv.put(colYear, reminder.getYear());
        cv.put(colMonth, reminder.getMonth());
        cv.put(colDay, reminder.getDay());
        cv.put(colHour, reminder.getHour());
        cv.put(colMinute, reminder.getMinute());
        if(reminder.isAm()) {
            cv.put(colTimeOfDay, 0);
        }
        else {
            cv.put(colTimeOfDay, 1);
        }

        long insert = db.insert(REMINDER_TABLE, null, cv);

        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    //This method removes a value from the SQLite table
    public boolean deleteOne(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        //SQLite statement to delete the item from the table
        String queryString = "DELETE FROM " + REMINDER_TABLE + " WHERE " + colId + " = " + reminder.get_id();

        //Executes the SQLite statement
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    //Returns an ArrayList with all the values from the SQLite table
    public ArrayList<Reminder> getAll() {
        ArrayList<Reminder> allReminder = new ArrayList<>();
        String queryString = "SELECT * FROM " + REMINDER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            //Loops while there is still a row in the table that has not had its values added to the ArrayList yet
            do {
                int reminderId = cursor.getInt(0);
                String reminderMessage = cursor.getString(1);
                int reminderYear = cursor.getInt(2);
                int reminderMonth = cursor.getInt(3);
                int reminderDay = cursor.getInt(4);
                int reminderHour = cursor.getInt(5);
                int reminderMinute = cursor.getInt(6);
                boolean reminderTimeOfDayAM;
                boolean reminderTimeOfDayPM;
                if(cursor.getInt(7) == 0) {
                    reminderTimeOfDayAM = true;
                    reminderTimeOfDayPM = false;
                }
                else {
                    reminderTimeOfDayAM = false;
                    reminderTimeOfDayPM = true;
                }

                Reminder addReminder = new Reminder(reminderId, reminderMessage, reminderYear, reminderMonth, reminderDay, reminderHour, reminderMinute, reminderTimeOfDayAM, reminderTimeOfDayPM);
                allReminder.add(addReminder);
            } while (cursor.moveToNext());
        }

        else {
            //failure
        }
        cursor.close();
        db.close();
        return allReminder;
    }

}
