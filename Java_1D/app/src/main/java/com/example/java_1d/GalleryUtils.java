package com.example.java_1d;

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
import java.text.SimpleDateFormat;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            List<Date> parseDate = new Parser().parse(date).get(0).getDates();
            Date parsedDate = parseDate.get(0);
            cldr.setTime(parsedDate);
            year = cldr.get(Calendar.YEAR);
            month = cldr.get(Calendar.MONTH);
            dayNum = cldr.get(Calendar.DAY_OF_MONTH);
        }
        final DatePickerDialog picker = new DatePickerDialog(this.context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new Date(year-1900, month, dayOfMonth);
                        Log.d("GalleryUtils", date.toString());
                        String dayStr = new SimpleDateFormat("EE", Locale.ENGLISH).format(date);
                        String monthStr = new SimpleDateFormat("MMM", Locale.ENGLISH).format(date);
                        dateBtn.setText(String.format("%02d-%s-%04d %s", dayOfMonth, monthStr, year, dayStr));
                    }
                }, year, month, dayNum);
        picker.show();
    }

    /**
     * bring out time picker that allows user to choose a time to be saved into the time button
     * @param timeBtn: time button
     */
    void saveTime(final Button timeBtn){
        Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int min = cldr.get(Calendar.MINUTE);
        if (timeBtn.getText() != null){
            String time = timeBtn.getText().toString();
            List<Date> parseTime = new Parser().parse(time).get(0).getDates();
            Date parsedTime = parseTime.get(0);
            cldr.setTime(parsedTime);
            hour = cldr.get(Calendar.HOUR_OF_DAY);
            min = cldr.get(Calendar.MINUTE);
        }

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
     * @param draft: True means is draft. False means is to be displayed in calendar
     */
    void savePref(String prefName, String title, String location, String description,
                  String fromDate, String fromTime, String toDate, String toTime,
                  Boolean draft, String imagePath){
        SharedPreferences sharedPref = this.context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("title", title);
        editor.putString("location", location);
        editor.putString("description", description);
        editor.putString("fromDate", fromDate);
        editor.putString("fromTime", fromTime);
        editor.putString("toDate", toDate);
        editor.putString("toTime", toTime);
        editor.putBoolean("draft", draft);
        editor.putString("imgPath", imagePath);
        editor.apply();
        Toast.makeText(this.context, "Entry saved with ID: " + prefName, Toast.LENGTH_SHORT).show();
    }

    /**
     * to convert day as integer number to its corresponding day as String
     * @param day: day of a week as Integer
     * @return day of a week as String
     */
    String convertDayOfWeek(int day) {
        String dayStr;

        switch (day) {
            case Calendar.MONDAY:
                dayStr = "Mon";
                break;
            case Calendar.TUESDAY:
                dayStr = "Tue";
                break;
            case Calendar.WEDNESDAY:
                dayStr = "Wed";
                break;
            case Calendar.THURSDAY:
                dayStr = "Thu";
                break;
            case Calendar.FRIDAY:
                dayStr = "Fri";
                break;
            case Calendar.SATURDAY:
                dayStr = "Sat";
                break;
            case Calendar.SUNDAY:
                dayStr = "Sun";
                break;
            default:
                dayStr = "";
                break;
        }
        return dayStr;
    }

    String convertMonth(int month){
        switch (month){
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "Mar";
            case 3: return "Apr";
            case 4: return "May";
            case 5: return "Jun";
            case 6: return "Jul";
            case 7: return "Aug";
            case 8: return "Sep";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default: return "";
        }
    }

    int getMonthNumFromText(String month){
        switch (month){
            case("jan"): return 1;
            case("feb"): return 2;
            case("mar"): return 3;
            case("apr"): return 4;
            case("may"): return 5;
            case("jun"): return 6;
            case("jul"): return 7;
            case("aug"): return 8;
            case("sep"): return 9;
            case("oct"): return 10;
            case("nov"): return 11;
            case("dec"): return 12;
            default: return 0;
        }
    }
}

