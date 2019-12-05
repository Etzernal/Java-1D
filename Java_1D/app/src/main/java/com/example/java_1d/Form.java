package com.example.java_1d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.joestelmach.natty.Parser;

import java.text.SimpleDateFormat;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Form extends AppCompatActivity {
    private static final int REQUEST_PICTURE_CAPTURE = 101;
    private photoCaptured pc = photoCaptured.getInstance();
    private Button saveButton;
    private Button confirmButton;
    private EditText entryTitle;
    private EditText locationBox;
    private EditText descriptionBox;
    private ImageView thumbnailImg;
    private Button fromDateBtn;
    private Button fromTimeBtn;
    private Button toDateBtn;
    private Button toTimeBtn;
<<<<<<< Updated upstream
    private ImageButton shareBtn;

    private Boolean hasPhoto;
=======
<<<<<<< HEAD
    private ImageButton shareBtn;

    private Boolean hasPhoto;
=======
    private Button addCalendarBtn;
>>>>>>> 72aa16caf562693ea94c1e15d45aaf88231d353f
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // get this bundle from either ViewEntry or EditOCR
        Bundle extras = getIntent().getExtras();
        String id = (String) extras.get("id");
        String title = (String) extras.get("title");
        String fromDate = (String) extras.get("fromDate");
        String fromTime = (String) extras.get("fromTime");
        String toDate = (String) extras.get("toDate");
        String toTime = (String) extras.get("toTime");
<<<<<<< Updated upstream
        String des = (String) extras.get("description");
        String loc = (String) extras.get("location");
=======
<<<<<<< HEAD
        String des = (String) extras.get("description");
        String loc = (String) extras.get("location");
=======
        String des = (String) extras.get("des");
        String loc = (String) extras.get("loc");
>>>>>>> 72aa16caf562693ea94c1e15d45aaf88231d353f
>>>>>>> Stashed changes
        String imgPath = (String) extras.get("imgPath");

        GalleryUtils galleryUtils = new GalleryUtils(Form.this);
        final Calendar cldr = Calendar.getInstance();
        final int year = cldr.get(Calendar.YEAR);
        final int month = cldr.get(Calendar.MONTH);
        final int dayNum = cldr.get(Calendar.DAY_OF_MONTH);
        final int day = cldr.get(Calendar.DAY_OF_WEEK);
        final int hour = cldr.get(Calendar.HOUR_OF_DAY);
        final int min = cldr.get(Calendar.MINUTE);

<<<<<<< Updated upstream
=======
<<<<<<< HEAD
>>>>>>> Stashed changes
        saveButton = (Button) findViewById(R.id.saveBtn);
        confirmButton = (Button) findViewById(R.id.addCalendar);
        entryTitle = (EditText) findViewById(R.id.entryTitle);
        locationBox = (EditText) findViewById(R.id.locationBox);
        descriptionBox = (EditText) findViewById(R.id.descriptionBox);
        thumbnailImg = (ImageView) findViewById(R.id.thumbnail);
        fromDateBtn = (Button) findViewById(R.id.fromDateBtn);
        toDateBtn = (Button) findViewById(R.id.toDateBtn);
        fromTimeBtn = (Button) findViewById(R.id.fromTimeBtn);
        toTimeBtn = (Button) findViewById(R.id.toTimeBtn);
        shareBtn = (ImageButton) findViewById(R.id.shareButton);

        entryTitle.setText(title);

        Log.d("myDebug", String.format("fromDate: %s", fromDate));

        // if date time not existing, set current date and time dd:MMM:yyyy EE
        if (fromDate.length() == 0) {
            fromDateBtn.setText(dateBuilder(year, month, dayNum, day));
        } else { fromDateBtn.setText(fromDate); }

        if (toDate.length() == 0) {
            toDateBtn.setText(dateBuilder(year, month, day, dayNum));
        } else { toDateBtn.setText(toDate); }

        if (fromTime.length() == 0) {
            fromTimeBtn.setText(timeBuilder(hour, min));
        } else { fromTimeBtn.setText(fromTime);}

        if (toTime.length() == 0) {
            toTimeBtn.setText(timeBuilder(hour, min));
        } else { toTimeBtn.setText(toTime);}

        if (!des.equals("")) {descriptionBox.setSingleLine(false); descriptionBox.setText(des);}
        if (!loc.equals("")) {locationBox.setSingleLine(false); locationBox.setText(loc);}

        Log.d("myDebug", "Image Path: "+imgPath+"Length of imgPath"+imgPath.length());
        // get the image from imgPath and convert to Bitmap
        if (imgPath.length() == 0){
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            try {
                hasPhoto = true;
<<<<<<< Updated upstream
=======
=======
        String dayStr = galleryUtils.convertDay(day);

        entryTitle.setText(title);
        locationBox.setText(loc);
        descriptionBox.setText(des);

        // if date time not existing, set current date and time
        if (fromDate == "") {
            String date = fromDateBtn.getText().toString();
            List<Date> parseDate = new Parser().parse(date).get(0).getDates();
            Date parsedDate = parseDate.get(0);
            cldr.setTime(parsedDate);
        }
        fromDateBtn.setText(fromDate);
        fromTimeBtn.setText(fromTime);
        toDateBtn.setText(toDate);
        toTimeBtn.setText(toTime);

        // get the image from imgPath and convert to Bitmap
        final File image = new File(imgPath);
        if (image.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            try {
>>>>>>> 72aa16caf562693ea94c1e15d45aaf88231d353f
>>>>>>> Stashed changes
                Bitmap processedBitmap = pc.processThumbnail(bitmap);
                thumbnailImg.setImageBitmap(processedBitmap);
            } catch (IOException ioex){
                Toast.makeText(Form.this, ioex.getMessage(), Toast.LENGTH_SHORT).show();
<<<<<<< Updated upstream
                hasPhoto = false;
=======
<<<<<<< HEAD
                hasPhoto = false;
=======
>>>>>>> 72aa16caf562693ea94c1e15d45aaf88231d353f
>>>>>>> Stashed changes
                thumbnailImg.setImageResource(R.drawable.placeholder);
            }
        } else {
            hasPhoto = false;
            thumbnailImg.setImageResource(R.drawable.placeholder);
        }
        // end of retrieving info

<<<<<<< Updated upstream
=======
<<<<<<< HEAD
=======
        saveButton = (Button) findViewById(R.id.saveBtn);
        confirmButton = (Button) findViewById(R.id.confirmBtn);
        entryTitle = (EditText) findViewById(R.id.entryTitle);
        locationBox = (EditText) findViewById(R.id.locationBox);
        descriptionBox = (EditText) findViewById(R.id.descriptionBox);
        thumbnailImg = (ImageView) findViewById(R.id.thumbnail);
        fromDateBtn = (Button) findViewById(R.id.fromDateBtn);
        toDateBtn = (Button) findViewById(R.id.toDateBtn);
        fromTimeBtn = (Button) findViewById(R.id.fromTimeBtn);
        toTimeBtn = (Button) findViewById(R.id.toTimeBtn);
        addCalendarBtn = (Button) findViewById(R.id.addCalendar);
>>>>>>> 72aa16caf562693ea94c1e15d45aaf88231d353f
>>>>>>> Stashed changes

        //save info -> direct to HOME
        saveButton.setOnClickListener((View v) ->{
            final String xtitle = entryTitle.getText().toString();
            final String xloc = locationBox.getText().toString();
            final String xdes = descriptionBox.getText().toString();
            final String xfromDate = fromDateBtn.getText().toString();
            final String xtoDate = toDateBtn.getText().toString();
            final String xfromTime = fromTimeBtn.getText().toString();
            final String xtoTime = toTimeBtn.getText().toString();
            final String ximgPath = pc.getImgPath();
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
>>>>>>> Stashed changes
            if (xtitle.length() == 0){
                Toast.makeText(Form.this, "Event title cannot be blanked!", Toast.LENGTH_SHORT).show();
            } else {
                galleryUtils.savePref(id, xtitle, xloc, xdes, xfromDate, xfromTime, xtoDate, xtoTime, true, ximgPath);
                Intent direct = new Intent(Form.this, MainActivity.class);
                startActivity(direct);
            }
<<<<<<< Updated upstream
=======
=======
            galleryUtils.savePref(id, xtitle, xloc, xdes, xfromDate, xfromTime, xtoDate, xtoTime, true, ximgPath);
            Intent direct = new Intent(Form.this, MainActivity.class);
            startActivity(direct);
>>>>>>> 72aa16caf562693ea94c1e15d45aaf88231d353f
>>>>>>> Stashed changes
        });

        //confirm info -> direct to HOME
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String xtitle = entryTitle.getText().toString();
                final String xloc = locationBox.getText().toString();
                final String xdes = descriptionBox.getText().toString();
                final String xfromDate = fromDateBtn.getText().toString();
                final String xtoDate = toDateBtn.getText().toString();
                final String xfromTime = fromTimeBtn.getText().toString();
                final String xtoTime = toTimeBtn.getText().toString();
                final String ximgPath = pc.getImgPath();
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
>>>>>>> Stashed changes
                if (xtitle.length() == 0){
                    Toast.makeText(Form.this, "Event title cannot be blanked!", Toast.LENGTH_SHORT).show();
                } else {
                    galleryUtils.savePref(id, xtitle, xloc, xdes, xfromDate, xfromTime, xtoDate, xtoTime, false, ximgPath);
                    Intent direct = new Intent(Form.this, MainActivity.class);
                    startActivity(direct);
                }
<<<<<<< Updated upstream
=======
=======
                galleryUtils.savePref(id, xtitle, xloc, xdes, xfromDate, xfromTime, xtoDate, xtoTime, false, ximgPath);
                Intent direct = new Intent(Form.this, MainActivity.class);
                startActivity(direct);
>>>>>>> 72aa16caf562693ea94c1e15d45aaf88231d353f
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
=======
<<<<<<< HEAD
>>>>>>> Stashed changes
        shareBtn.setOnClickListener((View v) -> {
            final String xtitle = entryTitle.getText().toString();
            final String xlocation = locationBox.getText().toString();
            final String xdes = descriptionBox.getText().toString();
            final String xfromDate = fromDateBtn.getText().toString();
            final String xtoDate = toDateBtn.getText().toString();
            final String xfromTime = fromTimeBtn.getText().toString();
            final String xtoTime = toTimeBtn.getText().toString();


            final String fromDateTime = xfromDate + " " + xfromTime;
            final String toDateTime = xtoDate + " " + xtoTime;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy EE hh:mm", Locale.ENGLISH);
            try{
                Date from = formatter.parse(fromDateTime);
                Date to = formatter.parse(toDateTime);
                long fromTimeMilli = from.getTime();
                long toTimeMilli = to.getTime();
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, xtitle)
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, xlocation)
                        .putExtra(CalendarContract.Events.DESCRIPTION, xdes)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, fromTimeMilli)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, toTimeMilli);

                if (intent.resolveActivity(this.getPackageManager()) != null) {
                    this.startActivity(intent);
                }
            } catch (ParseException e){
                Log.e("myDebug", e.getMessage());
                e.printStackTrace();
            }
        });

        thumbnailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("myDebug", hasPhoto.toString());
                if (!hasPhoto){
                    Log.i("myDebug", "Don't have photo, take a photo!");
                    take_photo(id);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK){
            Log.i("myDebug", "New Image: "+photoCaptured.getInstance().getImgPath());
            Form.this.recreate();
        }
    }
<<<<<<< Updated upstream

    private String dateBuilder(int year, int month, int dayOfWeek, int dayOfMonth){
        GalleryUtils gu = new GalleryUtils(Form.this);
        return String.format("%02d-%s-%d %s", dayOfMonth, gu.convertMonth(month), year, gu.convertDayOfWeek(dayOfWeek));
    }

    private String timeBuilder(int hour, int minute){
        return String.format("%02d:%02d", hour, minute);
    }

=======

    private String dateBuilder(int year, int month, int dayOfWeek, int dayOfMonth){
        GalleryUtils gu = new GalleryUtils(Form.this);
        return String.format("%02d-%s-%d %s", dayOfMonth, gu.convertMonth(month), year, gu.convertDayOfWeek(dayOfWeek));
    }

    private String timeBuilder(int hour, int minute){
        return String.format("%02d:%02d", hour, minute);
    }

>>>>>>> Stashed changes
    private void take_photo(String spid) {
        pc.setContext(Form.this);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
            Log.d("myMessage","trying to capture photo");
            File pictureFile;
            try {
                pictureFile = pc.createImageFile();
                SharedPreferences entrySP = getSharedPreferences(spid, MODE_PRIVATE);
                SharedPreferences.Editor ed = entrySP.edit();
                ed.putString("imgPath", pc.getImgPath());
                ed.apply();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            } catch (NullPointerException exn){Toast.makeText(this, "Cannot get external files directory", Toast.LENGTH_SHORT).show(); return;}
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.java_1d.fileprovider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                this.startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }
<<<<<<< Updated upstream
=======
=======
    }
>>>>>>> 72aa16caf562693ea94c1e15d45aaf88231d353f
>>>>>>> Stashed changes
}
