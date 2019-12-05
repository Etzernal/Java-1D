package com.example.java_1d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
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
import java.text.SimpleDateFormat;
import org.apache.commons.lang.ObjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Form extends AppCompatActivity {
    private photoCaptured pc = photoCaptured.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        final Button saveButton = (Button) findViewById(R.id.saveBtn);
        final Button importButton = (Button) findViewById(R.id.importBtn);
        final EditText entryTitle = (EditText) findViewById(R.id.entryTitle);
        final EditText locationBox = (EditText) findViewById(R.id.locationBox);
        final EditText descriptionBox = (EditText) findViewById(R.id.descriptionBox);
        final EditText sharedPrefName = (EditText) findViewById(R.id.sharedPrefName);
        final Switch reminderSwitch = (Switch) findViewById(R.id.reminder);
        final ImageButton cameraButton = (ImageButton) findViewById(R.id.camerabtn);
        final ImageView thumbnailImg = (ImageView) findViewById(R.id.thumbnail);
        cameraButton.setImageResource(R.drawable.camera);
        final Button fromDateBtn = (Button) findViewById(R.id.fromDateBtn);
        final Button toDateBtn = (Button) findViewById(R.id.toDateBtn);
        final Button fromTimeBtn = (Button) findViewById(R.id.fromTimeBtn);
        final Button toTimeBtn = (Button) findViewById(R.id.toTimeBtn);
        final Button addCalendarBtn = (Button) findViewById(R.id.addCalendar);
        GalleryUtils galleryUtils = new GalleryUtils(this);

        // autofill contents
        Intent intent1 = getIntent();
        String prefName = intent1.getExtras().getString("prefName");
        SharedPreferences myPref = getSharedPreferences(prefName, Context.MODE_PRIVATE);
        if (myPref.contains("title")) {
            entryTitle.setText(myPref.getString("title", ""));
        } if (myPref.contains("location")) {
            locationBox.setText(myPref.getString("location", ""));
        } if (myPref.contains("description")) {
            descriptionBox.setText(myPref.getString("description", ""));
        } if (myPref.contains("fromDate")) {
            fromDateBtn.setText(myPref.getString("fromDate", ""));
        } if (myPref.contains("fromTime")) {
            fromTimeBtn.setText(myPref.getString("fromTime", ""));
        } if (myPref.contains("toDate")) {
            toDateBtn.setText(myPref.getString("toDate", ""));
        } if (myPref.contains("toTime")) {
            toTimeBtn.setText(myPref.getString("toTime", ""));
        } if (myPref.contains("reminder")) {
            reminderSwitch.setChecked(myPref.getBoolean("reminder", true));
        } if (myPref.contains("imgPath")) {
            String imgPath = myPref.getString("imgPath", "");
            Uri fileUri = Uri.parse(imgPath);
            thumbnailImg.setImageURI(fileUri);
            thumbnailImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            thumbnailImg.setImageResource(R.drawable.placeholder);
        }
        sharedPrefName.setText(prefName);


        //save info
        saveButton.setOnClickListener((View v) ->{
            final String title = entryTitle.getText().toString();
            final String location = locationBox.getText().toString();
            final String description = descriptionBox.getText().toString();
            final String sharedPref = sharedPrefName.getText().toString();
            final String fromDate = fromDateBtn.getText().toString();
            final String toDate = toDateBtn.getText().toString();
            final String fromTime = fromTimeBtn.getText().toString();
            final String toTime = toTimeBtn.getText().toString();
            final boolean reminder = reminderSwitch.isChecked();
            final String imgPath = pc.getImgPath();

            galleryUtils.savePref(sharedPref, title, location, description, fromDate, fromTime, toDate, toTime, reminder, imgPath);
            Log.i("myMessage", "title: " + title);
            Log.i("myMessage", "loc: " + location);
            Log.i("myMessage", "des: " + description);
            Log.i("myMessage", "from: " + fromDate + fromTime);
            Log.i("myMessage", "to: " + toDate + toTime);
            Log.i("myMessage", "Reminder: " + reminder);
            Log.i("myMessage", "Image Path: " + imgPath);

        });

        //retrieve info
        importButton.setOnClickListener((View v) ->{
            final Calendar cldr = Calendar.getInstance();
            final int year = cldr.get(Calendar.YEAR);
            final int month = cldr.get(Calendar.MONTH);
            final int dayNum = cldr.get(Calendar.DAY_OF_MONTH);
            final int day = cldr.get(Calendar.DAY_OF_WEEK);
            final int hour = cldr.get(Calendar.HOUR_OF_DAY);
            final int min = cldr.get(Calendar.MINUTE);

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
            final File image = new File(imgPath);
            if (image.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                try {
                    Bitmap processedBitmap = pc.processThumbnail(bitmap);
                    thumbnailImg.setImageBitmap(processedBitmap);
                } catch (IOException ioex){
                    Toast.makeText(Form.this, ioex.getMessage(), Toast.LENGTH_SHORT).show();
                    thumbnailImg.setImageResource(R.drawable.placeholder);
                }
            } else {
                thumbnailImg.setImageResource(R.drawable.placeholder);
            }
        });


        //set from date
        fromDateBtn.setOnClickListener((View v) -> {
            galleryUtils.saveDate(fromDateBtn);

        });

        //set from time
        fromTimeBtn.setOnClickListener((View v) -> {
            galleryUtils.saveTime(fromTimeBtn);

        });

        //set to date
        toDateBtn.setOnClickListener((View v) -> {
            galleryUtils.saveDate(toDateBtn);

        });

        //set to time
        toTimeBtn.setOnClickListener((View v) -> {
            galleryUtils.saveTime(toTimeBtn);

        });

        //add to calendar
        addCalendarBtn.setOnClickListener((View v) -> {
            final String title = entryTitle.getText().toString();
            final String location = locationBox.getText().toString();
            final String fromDate = fromDateBtn.getText().toString();
            final String toDate = toDateBtn.getText().toString();
            final String fromTime = fromTimeBtn.getText().toString();
            final String toTime = toTimeBtn.getText().toString();

            final String fromDateTime = fromDate + " " + fromTime;
            final String toDateTime = toDate + " " + toTime;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy EE hh:mm", Locale.ENGLISH);
            try{
                Date from = formatter.parse(fromDateTime);
                long fromTimeMilli = from.getTime();
                Date to = formatter.parse(toDateTime);
                long toTimeMilli = to.getTime();
                galleryUtils.addEvent(title, location, fromTimeMilli, toTimeMilli);
            } catch (ParseException e){
                e.printStackTrace();
            }

        });

        cameraButton.setOnClickListener((View v) -> {
            if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                take_photo();
            }
        });
    }

    private static final int REQUEST_PICTURE_CAPTURE = 1;

    protected void take_photo(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File pictureFile;
            try {
                pictureFile = pc.createImageFile();
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
        super.onActivityResult(requestCode, resultCode, data);
        final ImageView thumbnailImg = (ImageView) findViewById(R.id.thumbnail);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pc.getImgPath());
            Toast.makeText(this, "Photo saved to "+ pc.getImgPath(), Toast.LENGTH_LONG).show();
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                // rotate the Bitmap in the correct orientation
                try{
                    Bitmap processedBitmap = pc.processThumbnail(myBitmap);
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
