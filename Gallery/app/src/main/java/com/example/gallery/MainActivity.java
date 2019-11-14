package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button saveButton = (Button) findViewById(R.id.saveBtn);
        final Button importButton = (Button) findViewById(R.id.importBtn);
        final EditText entryTitle = (EditText) findViewById(R.id.entryTitle);
        final EditText locationBox = (EditText) findViewById(R.id.locationBox);
        final EditText descriptionBox = (EditText) findViewById(R.id.descriptionBox);
        final EditText sharedPrefName = (EditText) findViewById(R.id.sharedPrefName);
        final Switch reminderSwitch = (Switch) findViewById(R.id.reminder);
        final ImageView thumbnailImg = (ImageView) findViewById(R.id.thumbnail);

        final Button fromDateBtn = (Button) findViewById(R.id.fromDateBtn);
        final Button toDateBtn = (Button) findViewById(R.id.toDateBtn);
        final Button fromTimeBtn = (Button) findViewById(R.id.fromTimeBtn);
        final Button toTimeBtn = (Button) findViewById(R.id.toTimeBtn);
        final Button addCalendarBtn = (Button) findViewById(R.id.addCalendar);

        thumbnailImg.setImageResource(R.drawable.duck);

        GalleryUtils galleryUtils = new GalleryUtils(this);
        //initialize datetime display
        galleryUtils.displayCrrtDate(fromDateBtn);
        galleryUtils.displayCrrtDate(toDateBtn);
        galleryUtils.displayCrrtTime(fromTimeBtn);
        galleryUtils.displayCrrtTime(toTimeBtn);

        //save info
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String title = entryTitle.getText().toString();
                final String location = locationBox.getText().toString();
                final String description = descriptionBox.getText().toString();
                final String sharedPref = sharedPrefName.getText().toString();
                final String fromDate = fromDateBtn.getText().toString();
                final String toDate = toDateBtn.getText().toString();
                final String fromTime = fromTimeBtn.getText().toString();
                final String toTime = toTimeBtn.getText().toString();
                final boolean reminder = reminderSwitch.isChecked();
                final GalleryUtils galleryUtils = new GalleryUtils(MainActivity.this);

                galleryUtils.savePref(sharedPref, title, location, description, fromDate, fromTime, toDate, toTime, reminder);
                Log.i("myMessage", "title: " + title);
                Log.i("myMessage", "loc: " + location);
                Log.i("myMessage", "des: " + description);
                Log.i("myMessage", "from: " + fromDate + fromTime);
                Log.i("myMessage", "to: " + toDate + toTime);
                Log.i("myMessage", "Reminder" + reminder);
            }
        });

        //retrieve info
        importButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Calendar cldr = Calendar.getInstance();
                final int year = cldr.get(Calendar.YEAR);
                final int month = cldr.get(Calendar.MONTH);
                final int dayNum = cldr.get(Calendar.DAY_OF_MONTH);
                final int day = cldr.get(Calendar.DAY_OF_WEEK);
                final int hour = cldr.get(Calendar.HOUR_OF_DAY);
                final int min = cldr.get(Calendar.MINUTE);
                final GalleryUtils galleryUtils = new GalleryUtils(MainActivity.this);

                String dayStr = galleryUtils.convertDay(day);

                final String sharedPref = sharedPrefName.getText().toString();
                SharedPreferences pref = galleryUtils.retrievePref(sharedPref);
                String title = pref.getString("title", "");
                String location = pref.getString("location", "");
                String description = pref.getString("description", "");
                String fromDate = pref.getString("fromDate", String.format("%s %d/%d/%d", dayStr, dayNum, month+1, year));
                String fromTime = pref.getString("fromTime", String.format("%d:%d", hour, min));
                String toDate = pref.getString("toDate", String.format("%s %d/%d/%d", dayStr, dayNum, month+1, year));
                String toTime = pref.getString("toTime", String.format("%d:%d", hour, min));
                Boolean reminder = pref.getBoolean("reminder", false);

                entryTitle.setText(title);
                locationBox.setText(location);
                descriptionBox.setText(description);
                fromDateBtn.setText(fromDate);
                fromTimeBtn.setText(fromTime);
                toDateBtn.setText(toDate);
                toTimeBtn.setText(toTime);
                reminderSwitch.setChecked(reminder);
            }
        });


        //set from date
        fromDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GalleryUtils galleryUtils = new GalleryUtils(MainActivity.this);
                galleryUtils.saveDate(fromDateBtn);
            }
        });

        //set from time
        fromTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GalleryUtils galleryUtils = new GalleryUtils(MainActivity.this);
                galleryUtils.saveTime(fromTimeBtn);
            }
        });

        //set to date
        toDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GalleryUtils galleryUtils = new GalleryUtils(MainActivity.this);
                galleryUtils.saveDate(toDateBtn);
            }
        });

        //set to time
        toTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryUtils galleryUtils = new GalleryUtils(MainActivity.this);
                galleryUtils.saveTime(toTimeBtn);
            }
        });

        //add to calendar
        addCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = entryTitle.getText().toString();
                final String location = locationBox.getText().toString();
                final String fromDate = fromDateBtn.getText().toString();
                final String toDate = toDateBtn.getText().toString();
                final String fromTime = fromTimeBtn.getText().toString();
                final String toTime = toTimeBtn.getText().toString();
                final GalleryUtils galleryUtils = new GalleryUtils(MainActivity.this);

                final String fromDateTime = fromDate + " " + fromTime;
                final String toDateTime = toDate + " " + toTime;
                SimpleDateFormat formatter = new SimpleDateFormat("EEEEE dd/MM/yyyy hh:mm", Locale.ENGLISH);
                try{
                    Date from = formatter.parse(fromDateTime);
                    long fromTimeMilli = from.getTime();
                    Date to = formatter.parse(toDateTime);
                    long toTimeMilli = to.getTime();
                    galleryUtils.addEvent(title, location, fromTimeMilli, toTimeMilli);
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        });
    }


}