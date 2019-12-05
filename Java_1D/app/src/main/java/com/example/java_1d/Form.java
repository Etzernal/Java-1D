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

import com.joestelmach.natty.Parser;

import java.text.SimpleDateFormat;
import org.apache.commons.lang.ObjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Form extends AppCompatActivity {
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
    private Button addCalendarBtn;

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
        String des = (String) extras.get("des");
        String loc = (String) extras.get("loc");
        String imgPath = (String) extras.get("imgPath");

        GalleryUtils galleryUtils = new GalleryUtils(Form.this);
        final Calendar cldr = Calendar.getInstance();
        final int year = cldr.get(Calendar.YEAR);
        final int month = cldr.get(Calendar.MONTH);
        final int dayNum = cldr.get(Calendar.DAY_OF_MONTH);
        final int day = cldr.get(Calendar.DAY_OF_WEEK);
        final int hour = cldr.get(Calendar.HOUR_OF_DAY);
        final int min = cldr.get(Calendar.MINUTE);

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
                Bitmap processedBitmap = pc.processThumbnail(bitmap);
                thumbnailImg.setImageBitmap(processedBitmap);
            } catch (IOException ioex){
                Toast.makeText(Form.this, ioex.getMessage(), Toast.LENGTH_SHORT).show();
                thumbnailImg.setImageResource(R.drawable.placeholder);
            }
        } else {
            thumbnailImg.setImageResource(R.drawable.placeholder);
        }
        // end of retrieving info

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
            galleryUtils.savePref(id, xtitle, xloc, xdes, xfromDate, xfromTime, xtoDate, xtoTime, true, ximgPath);
            Intent direct = new Intent(Form.this, MainActivity.class);
            startActivity(direct);
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
                galleryUtils.savePref(id, xtitle, xloc, xdes, xfromDate, xfromTime, xtoDate, xtoTime, false, ximgPath);
                Intent direct = new Intent(Form.this, MainActivity.class);
                startActivity(direct);
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

    }
}
