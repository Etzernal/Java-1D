package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CameraUtils cameraUtils = new CameraUtils(this);

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
        final ImageButton cameraButton = (ImageButton) findViewById(R.id.camerabtn);
        final ImageView thumbnailImg = (ImageView) findViewById(R.id.thumbnail);
        thumbnailImg.setImageResource(R.drawable.placeholder);
        cameraButton.setImageResource(R.drawable.camera);

        final Button fromDateBtn = (Button) findViewById(R.id.fromDateBtn);
        final Button toDateBtn = (Button) findViewById(R.id.toDateBtn);
        final Button fromTimeBtn = (Button) findViewById(R.id.fromTimeBtn);
        final Button toTimeBtn = (Button) findViewById(R.id.toTimeBtn);
        final Button addCalendarBtn = (Button) findViewById(R.id.addCalendar);

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
                final String imgPath = cameraUtils.imgFilePath;

                galleryUtils.savePref(sharedPref, title, location, description, fromDate, fromTime, toDate, toTime, reminder, imgPath);
                Log.i("myMessage", "title: " + title);
                Log.i("myMessage", "loc: " + location);
                Log.i("myMessage", "des: " + description);
                Log.i("myMessage", "from: " + fromDate + fromTime);
                Log.i("myMessage", "to: " + toDate + toTime);
                Log.i("myMessage", "Reminder: " + reminder);
                Log.i("myMessage", "Image Path: " + imgPath);
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
                String imgPath = pref.getString("imgPath", "");

                entryTitle.setText(title);
                locationBox.setText(location);
                descriptionBox.setText(description);
                fromDateBtn.setText(fromDate);
                fromTimeBtn.setText(fromTime);
                toDateBtn.setText(toDate);
                toTimeBtn.setText(toTime);
                reminderSwitch.setChecked(reminder);

                // get the image from imgPath and convert to Bitmap
                final ImageView thumbnailImg = (ImageView) findViewById(R.id.thumbnail);
                final File image = new File(imgPath);
                if (image.exists()){
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                    try {
                        Bitmap processedBitmap = cameraUtils.processThumbnail(bitmap);
                        thumbnailImg.setImageBitmap(processedBitmap);
                    } catch (IOException ioex){
                        Toast.makeText(MainActivity.this, ioex.getMessage(), Toast.LENGTH_SHORT).show();
                        thumbnailImg.setImageResource(R.drawable.placeholder);
                    }
                } else {
                    thumbnailImg.setImageResource(R.drawable.placeholder);
                }
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

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                    take_photo();
                }
            }
        });
    }

    private static final int REQUEST_PICTURE_CAPTURE = 1;

    protected void take_photo(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File pictureFile;
            try {
                pictureFile = cameraUtils.createUniqueImageFilename();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final ImageView thumbnailImg = (ImageView) findViewById(R.id.thumbnail);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(cameraUtils.imgFilePath);
            Toast.makeText(this, "Photo saved to "+cameraUtils.imgFilePath, Toast.LENGTH_LONG).show();
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                cameraUtils.galleryAddPic();
                // rotate the Bitmap in the correct orientation
                try{
                    Bitmap processedBitmap = cameraUtils.processThumbnail(myBitmap);
                    thumbnailImg.setImageBitmap(processedBitmap);
                } catch (IOException ioex){
                    Toast.makeText(this, ioex.getMessage(), Toast.LENGTH_SHORT).show();
                    thumbnailImg.setImageBitmap(myBitmap);
                }
            } else {
                thumbnailImg.setImageResource(R.drawable.placeholder);
            }
        }
    }

}