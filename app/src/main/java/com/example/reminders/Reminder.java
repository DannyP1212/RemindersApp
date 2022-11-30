package com.example.reminders;

public class Reminder {
    //Class-level variables for a Reminder object
    private int _id;
    private String message;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private boolean am;
    private boolean pm;

    //Constructor for a reminder object
    public Reminder(int mId, String mMessage, int mYear, int mMonth, int mDay, int mHour, int mMinute, boolean mAm, boolean mPm) {
        _id = mId;
        message = mMessage;
        year = mYear;
        month = mMonth;
        day = mDay;
        hour = mHour;
        minute = mMinute;
        am = mAm;
        pm = mPm;
    }

    //Default constructor
    public Reminder() {

    }

    //All the getter and setter methods
    public String getMessage() {
        return message;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isAm() {
        return am;
    }

    public boolean isPm() {
        return pm;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setAm(boolean am) {
        this.am = am;
    }

    public void setPm(boolean pm) {
        this.pm = pm;
    }
}
