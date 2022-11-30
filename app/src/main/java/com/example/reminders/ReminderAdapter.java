package com.example.reminders;

import android.app.Activity;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    private ArrayList<Reminder> reminderList;

    //Constructor for a ReminderAdapter
    public ReminderAdapter(Activity context, ArrayList<Reminder> mReminderList) {
        super(context, 0, mReminderList);
        reminderList = mReminderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        //Sets the XML layout that will be updated and then added to the ListView
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_item, parent, false);
        }

        //The code segment below sets the text for the listItemView to contain data from a Reminder object in the mReminderList ArrayList, and then returns that view
        Reminder currentReminder = getItem(position);
        String timeOfDay;
        if(currentReminder.isAm()) {
            timeOfDay = "AM";
        }
        else {
            timeOfDay = "PM";
        }
        TextView reminderMessage = (TextView) listItemView.findViewById(R.id.ReminderText);
        reminderMessage.setText("" + currentReminder.getMessage() + "  |  Date: " + currentReminder.getMonth() + "/" + currentReminder.getDay() + "/" + currentReminder.getYear() + "  Time: " + currentReminder.getHour() + ":" + minuteFormatter(currentReminder.getMinute()) + " " + timeOfDay);

        return listItemView;
    }

    //This method allows minute values less than 10 to be two digits long, the same way a digital clock would display them
    public String minuteFormatter(int minute) {
        if(minute < 10) {
            return ("0" + minute);
        }
        else {
            return ("" + minute);
        }
    }

}
