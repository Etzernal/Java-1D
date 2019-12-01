package com.example.gallery;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Utility class for Gallery functions
 */
public class GalleryUtils{

    /**
     * context is specified in construction of the class object to specify these set of functions apply in which context
     */
    private final Context context;

    /**
     * Constructor for GalleryUtils class
     * @param context: The context of activity this class is applied on
     */
    GalleryUtils(Context context){
        this.context = context;
    }

    /**
     * Function to call out third-party calendar app and create an event
     * @param title: title of the event
     * @param location: location of the event
     * @param begin: start date and time of the event
     * @param end: end date and time of the event
     */
    void addEvent(String title, String location, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(this.context.getPackageManager()) != null) {
            this.context.startActivity(intent);
        }
    }

    /**
     * call this function to display current date
     * @param dateBtn: the date button which you want to display the current date as the text
     */
    void displayCrrtDate(final Button dateBtn){
        final Calendar cldr = Calendar.getInstance();
        final int year = cldr.get(Calendar.YEAR);
        final int month = cldr.get(Calendar.MONTH);
        final int dayNum = cldr.get(Calendar.DAY_OF_MONTH);
        final int day = cldr.get(Calendar.DAY_OF_WEEK);

        String dayStr = convertDay(day);
        dateBtn.setText(String.format("%s %02d/%02d/%04d", dayStr, dayNum, month+1, year));
    }

    /**
     * call this function to display current time
     * @param timeBtn: the time button which you want to display the current time as the text
     */
    void displayCrrtTime(final Button timeBtn){
        final Calendar cldr = Calendar.getInstance();
        final int hour = cldr.get(Calendar.HOUR_OF_DAY);
        final int min = cldr.get(Calendar.MINUTE);
        timeBtn.setText(String.format("%02d:%02d", hour, min));
    }

    /**
     * bring out date picker component and let user choose a date to save for the date button
     * @param dateBtn: date button
     */
    void saveDate(final Button dateBtn){
        Calendar cldr = Calendar.getInstance();
        int year = cldr.get(Calendar.YEAR);
        int month = cldr.get(Calendar.MONTH);
        int dayNum = cldr.get(Calendar.DAY_OF_MONTH);
        if (dateBtn.getText() != null) {
            String date = dateBtn.getText().toString();
            Log.d("GalleryUtils",date);
            List<Date> parseDate = new Parser().parse(date).get(0).getDates();
            Date parsedDate = parseDate.get(0);
            Log.d("GalleryUtils",parsedDate.toString());
            cldr.setTime(parsedDate);
            year = cldr.get(Calendar.YEAR);
            month = cldr.get(Calendar.MONTH);
            dayNum = cldr.get(Calendar.DAY_OF_MONTH);
        }

        Log.d("GalleryUtils",dateBtn.getText().toString());
        Log.d("GalleryUtils",Integer.toString(dayNum));
        final DatePickerDialog picker = new DatePickerDialog(this.context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new Date(year, month, dayOfMonth);
                        int day = date.getDay();
                        String dayStr = convertDay(day);
                        Log.i("myMessage", "Day selected: " + dayStr);
                        dateBtn.setText(String.format("%s %02d-%02d-%04d", dayStr, dayOfMonth, month+1, year));
                    }
                }, year, month, dayNum);
        picker.show();
    }

    /**
     * bring out time picker that allows user to choose a time to be saved into the time button
     * @param timeBtn: time button
     */
    void saveTime(final Button timeBtn){
        final Calendar cldr = Calendar.getInstance();
        final int hour = cldr.get(Calendar.HOUR_OF_DAY);
        final int min = cldr.get(Calendar.MINUTE);

        final TimePickerDialog picker = new TimePickerDialog(
                this.context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeBtn.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                },
                hour, min, true);
        picker.show();
    }

    /**
     * save data into shared preferences as key-value pairs
     * @param prefName: the ID of the shared preference object
     * @param title: title of the event
     * @param location: location of the event
     * @param description: description of the event
     * @param fromDate: start date of the event
     * @param fromTime: start time of the event
     * @param toDate: end date of the event
     * @param toTime: end time of the event
     * @param reminder: set reminder or not. True: has reminder. False: has no reminder
     */
    void savePref(String prefName, String title, String location, String description,
                            String fromDate, String fromTime, String toDate, String toTime,
                            Boolean reminder, String imagePath){
        SharedPreferences sharedPref = this.context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("title", title);
        editor.putString("location", location);
        editor.putString("description", description);
        editor.putString("fromDate", fromDate);
        editor.putString("fromTime", fromTime);
        editor.putString("toDate", toDate);
        editor.putString("toTime", toTime);
        editor.putBoolean("reminder", reminder);
        editor.putString("imgPath", imagePath);
        editor.apply();
        Toast.makeText(this.context, "Entry saved with ID: " + prefName, Toast.LENGTH_SHORT).show();
    }

    /**
     * return a shared preference object
     * @param prefName: the ID of shared preference to locate the object
     * @return SharedPreferences object contains relevant data
     */
    SharedPreferences retrievePref(String prefName){
        return this.context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    /**
     * to convert day as integer number to its corresponding day as String
     * @param day: day of a week as Integer
     * @return day of a week as String
     */
    String convertDay(int day) {
        String dayStr;

        switch (day) {
            case Calendar.MONDAY:
                dayStr = "Monday";
                break;
            case Calendar.TUESDAY:
                dayStr = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayStr = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayStr = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayStr = "Friday";
                break;
            case Calendar.SATURDAY:
                dayStr = "Saturday";
                break;
            case Calendar.SUNDAY:
                dayStr = "Sunday";
                break;
            default:
                dayStr = "";
                break;
        }
        return dayStr;
    }
}
