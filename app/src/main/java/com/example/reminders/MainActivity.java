package com.example.reminders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    //Declares the variables for all the XML elements that will be modified
    TextView yearText;
    Button yearMinus;
    Button yearPlus;
    CheckBox am;
    CheckBox pm;
    EditText reminderMessage;
    Button createReminderButton;
    ListView reminderList;
    LinearLayout reminderCreator;
    Button hideReminderCreator;
    Slider monthSlider;
    Slider daySlider;
    Slider hourSlider;
    Slider minuteSlider;
    TextView dateTimePreview;
    RelativeLayout creatorShower;
    private static final String CHANNEL_ID = "Notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initial code that allows for certain app functions
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        //Allows the necessary SQLite table to be created with each of the necessary headers
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

        //Instantiates all of the previously declared variables with the corresponding XML View
        yearText = findViewById(R.id.YearText);
        yearMinus = findViewById(R.id.YearMinus);
        yearPlus = findViewById(R.id.YearPlus);
        am = findViewById(R.id.AmBox);
        pm = findViewById(R.id.PmBox);
        reminderMessage = findViewById(R.id.ReminderMessage);
        createReminderButton = findViewById(R.id.CreateReminderButton);
        reminderList = findViewById(R.id.ReminderList);
        reminderCreator = findViewById(R.id.ReminderCreator);
        hideReminderCreator = findViewById(R.id.HideCreator);
        monthSlider = findViewById(R.id.MonthSlider);
        daySlider = findViewById(R.id.DaySlider);
        hourSlider = findViewById(R.id.HourSlider);
        minuteSlider = findViewById(R.id.MinuteSlider);
        dateTimePreview = findViewById(R.id.DateTimePreview);
        creatorShower = findViewById(R.id.CreatorShower);

        //Instantiates the ReminderAdapter that will then add items to the Listview. These items are obtained from the SQLite table
        ReminderAdapter adapter = new ReminderAdapter(this, dataBaseHelper.getAll());
        reminderList.setAdapter(adapter);

        //Allows the minus button to decrease the value of the year in the year's TextView
        yearMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.parseInt(yearText.getText().toString());
                year--;
                yearText.setText("" + year);
                //Makes it so the day slider is changed when the selected month is February and it is a leap year
                if(year % 4 == 0 && monthSlider.getValue() == 2) {
                    if((int) daySlider.getValue() > 29) {
                        daySlider.setValue(29);
                    }
                    daySlider.setValueTo(29);
                }
                //Makes it so the day slider is changed when the selected month is February and it is not a leap year
                if(year % 4 != 0 && monthSlider.getValue() == 2) {
                    if((int) daySlider.getValue() > 28) {
                        daySlider.setValue(28);
                    }
                    daySlider.setValueTo(28);
                }
                updatePreview();
            }
        });

        //Allows the plus button to increase the value of the year in the year's TextView
        yearPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.parseInt(yearText.getText().toString());
                year++;
                yearText.setText("" + year);
                //Makes it so the day slider is changed when the selected month is February and it is a leap year
                if(year % 4 == 0 && monthSlider.getValue() == 2) {
                    if((int) daySlider.getValue() > 29) {
                        daySlider.setValue(29);
                    }
                    daySlider.setValueTo(29);
                }
                //Makes it so the day slider is changed when the selected month is February and it is not a leap year
                if(year % 4 != 0 && monthSlider.getValue() == 2) {
                    if((int) daySlider.getValue() > 28) {
                        daySlider.setValue(28);
                    }
                    daySlider.setValueTo(28);
                }
                //Updates the preview TextView
                updatePreview();
            }
        });

        //This code segment allows the month slider to function as intended by updating the values the day slider can select based on what month it is.
        monthSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                //Allows only up to 31 days to be selected on months that contain 31 days
                if((int) monthSlider.getValue() == 1 || (int) monthSlider.getValue() == 3 || (int) monthSlider.getValue() == 5 || (int) monthSlider.getValue() == 7 || (int) monthSlider.getValue() == 8 || (int) monthSlider.getValue() == 10 || (int) monthSlider.getValue() == 12) {
                    daySlider.setValueTo(31);
                }
                //Allows only 29 days to be selected when the currently selected month is February on a leap year.
                else if((int) monthSlider.getValue() == 2 && Integer.parseInt(yearText.getText().toString()) % 4 == 0) {
                    if((int) daySlider.getValue() > 29) {
                        daySlider.setValue(29);
                    }
                    daySlider.setValueTo(29);
                }
                //Allows only 28 days to be selected when the current month is February and it is not a leap year
                else if ((int) monthSlider.getValue() == 2) {
                    if((int) daySlider.getValue() > 28) {
                        daySlider.setValue(28);
                    }
                    daySlider.setValueTo(28);
                }
                //Allows only 30 days to be selected on every other month, which are the months that only have 30 days in them
                else {
                    if((int) daySlider.getValue() > 30) {
                        daySlider.setValue(30);
                    }
                    daySlider.setValueTo(30);
                }
                //Updates the preview TextView
                updatePreview();
            }
        });

        //Updates the preview TextView when the day slider is modified
        daySlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                updatePreview();
            }
        });

        //Updates the preview TextView when the day slider is modified
        hourSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                updatePreview();
            }
        });

        //Updates the preview TextView when the minute slider is modified
        minuteSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                updatePreview();
            }
        });

        //Unchecks the PM checkbox if the user presses the AM checkbox. Then, updates the preview TextView
        am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pm.isChecked()) {
                    pm.setChecked(false);
                }
                updatePreview();
            }
        });

        //Unchecks the AM checkbox if the user presses the PM checkbox. Then, updates the preview TextView
        pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(am.isChecked()) {
                    am.setChecked(false);
                }
                updatePreview();
            }
        });

        //Creates a Reminder object given the current values entered into the app, adds a View to the ListView, and creates a notification with those values
        createReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checks if the user has entered a message into the Message section of the Reminder Creator
                if(reminderMessage.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "You must write a message", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //Checks if the user has selected AM or PM
                else if((!am.isChecked()) && (!(pm.isChecked()))) {
                    Toast toast = Toast.makeText(getApplicationContext(), "You must select either AM or PM", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //Runs this code if both of the previous conditions are met
                else {
                    //Creates a Reminder object given the inputted values
                    Reminder reminder = new Reminder(0, reminderMessage.getText().toString(), Integer.parseInt(yearText.getText().toString()), (int) monthSlider.getValue(), (int) daySlider.getValue(), (int) hourSlider.getValue(), (int) minuteSlider.getValue(), am.isChecked(), pm.isChecked());

                    //Adds the data in the Reminder object to the SQLite table
                    dataBaseHelper.addOne(reminder);

                    //Sets the updated SQLite table data as the ArrayList in the ArrayAdapter, allowing the data from the Reminder object to be displayed in the Current Reminders section
                    ReminderAdapter adapter = new ReminderAdapter(MainActivity.this, dataBaseHelper.getAll());
                    reminderList.setAdapter(adapter);

                    //Sets the EditText and the Checkboxes to their default state
                    reminderMessage.setText("");
                    am.setChecked(false);
                    pm.setChecked(false);

                    //Lets the user know a reminder was created
                    Toast toast = Toast.makeText(getApplicationContext(), "Reminder Created", Toast.LENGTH_SHORT);
                    toast.show();

                    //These 2 sections of if/else statements create variables with updated versions of values that will be used when creating the notification
                    String minute;
                    if(reminder.getMinute() < 10) {
                        minute = "0" + reminder.getMinute();
                    }
                    else {
                        minute = "" + reminder.getMinute();
                    }
                    String timeOfDay;
                    if(reminder.isAm()) {
                        timeOfDay = "AM";
                    }
                    else {
                        timeOfDay = "PM";
                    }

                    //Gets the newly created Reminder object from the SQLite database.
                    //This section is important, because having the id of the row in the SQLite database be equal to the id of the notification allows the correct notification to be closed when the user deletes a reminder
                    ArrayList<Reminder> currentReminders = dataBaseHelper.getAll();
                    int id = currentReminders.get(currentReminders.size()-1).get_id();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

                    //Creates the notification that contains data from the reminder
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.clock)
                            .setContentTitle(reminder.getMessage())
                            .setContentText("Date: " + reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear() + " Time: " + reminder.getHour() + ":" + minute + " " + timeOfDay)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setOngoing(true);

                    //Shows the notification
                    notificationManager.notify(id, builder.build());

                }
            }
        });


        //This coding segment makes it so the Reminder Creator can be shown or become hidden so that more of the screen can be dedicated to showing current reminders
        //The button's function changes depending on whether or not the Reminder Creator is currently hidden
        hideReminderCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hideReminderCreator.getText().toString().equals("Hide Creator")) {
                    reminderCreator.setVisibility(View.GONE);
                    createReminderButton.setVisibility(View.GONE);
                    hideReminderCreator.setText("Show Creator");
                }
                else {
                    reminderCreator.setVisibility(View.VISIBLE);
                    createReminderButton.setVisibility(View.VISIBLE);
                    hideReminderCreator.setText("Hide Creator");
                }
            }
        });

        //Allows reminders to be deleted when the user taps on an item in the ListView
        reminderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Reminder clickedReminder = (Reminder) parent.getItemAtPosition(i);
                dataBaseHelper.deleteOne(clickedReminder);
                ReminderAdapter adapter = new ReminderAdapter(MainActivity.this, dataBaseHelper.getAll());
                reminderList.setAdapter(adapter);
                Toast toast = Toast.makeText(getApplicationContext(), "Reminder deleted", Toast.LENGTH_SHORT);
                toast.show();
                notificationManager.cancel(clickedReminder.get_id());
            }
        });

    }

    //Formats the String that will be displayed as the preview
    public String timeString(int year, int month, int day, int hour, int minute, boolean isAM, boolean isPM) {
        String timeOfDay;
        if(isAM) {
            timeOfDay = "AM";
        }
        else if(isPM){
            timeOfDay = "PM";
        }
        else {
            timeOfDay = "";
        }
        String minuteString;
        if(minute < 10) {
            minuteString = "0" + minute;
        }
        else {
            minuteString = "" + minute;
        }
        return (month + "/" + day + "/" + year + " at " + hour + ":" + minuteString + " " + timeOfDay);
    }

    //Updates what is displayed in the preview
    public void updatePreview() {
        dateTimePreview.setText(timeString(Integer.parseInt(yearText.getText().toString()), (int) monthSlider.getValue(), (int) daySlider.getValue(), (int) hourSlider.getValue(), (int) minuteSlider.getValue(), am.isChecked(), pm.isChecked()));
    }

    //Creates the notification channel, which is necessary to display notifications in some versions of Android
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notif Channel";
            String description = "This is the channel for notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //UNUSED
    //Creates a Calendar object with the date and time from a Reminder
    public Calendar calendarCreator(Reminder a) {
        Calendar c  = Calendar.getInstance();
        int hour = a.getHour();
        if(hour == 12 && a.isAm() == true) {
            hour = 0;
        }
        else if(a.isPm() && (!(hour == 12))) {
            hour = hour + 12;
        }
        c.set(a.getYear(), a.getMonth(), a.getDay(), hour, a.getMinute());
        return c;
    }

    //UNUSED
    //Creates an alarm that activates at the time that corresponds to a Reminder object. The time has to be converted into a long before it is input in this method
    public void setAlarm(long timeInMili) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcast.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, timeInMili, pendingIntent);
        }
    }
}