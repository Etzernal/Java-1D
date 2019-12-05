package com.example.java_1d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.ImageDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;


public class EditOCR extends AppCompatActivity {
    private static final String TAG = "EditOCR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ocr);

        final Button doneBtn = findViewById(R.id.btnDone);
        final Button backBtn = findViewById(R.id.btnBack);
        final Button venueBtn = findViewById(R.id.btnVenue);
        final Button descBtn = findViewById(R.id.btnDesc);
        final ImageView eventImg = findViewById(R.id.imgEvent);
        final TextView testOCR = findViewById(R.id.testOCR);

        String image_path = getIntent().getStringExtra("Image");
        Log.i("myDebug", String.format("EditOCR: image get: %s", image_path));

        //set selected image in imageView
        if (image_path != null) {
            Uri fileUri = Uri.parse(image_path);
            eventImg.setImageURI(fileUri);
            eventImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE); //scale and fit image into imageview
        } else {
            eventImg.setImageResource(R.drawable.placeholder);
        }

        // calling OCR
        Uri fileUri = Uri.parse(image_path);
        FirebaseVisionImage image;
        try {
            image = FirebaseVisionImage.fromFilePath(this, fileUri);
            FirebaseVisionTextRecognizer detector =
                    FirebaseVision.getInstance().getOnDeviceTextRecognizer();

            detector.processImage(image)
                    .addOnSuccessListener(texts -> {
                        testOCR.setText(processTextRecognitionResult(texts));

                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // function to go to form page
        doneBtn.setOnClickListener((View v) -> {
            //Parse for Date and Time
            String finalText = testOCR.getText().toString();
            String startDate = null;
            String endDate = null ;
            String startTime = null;
            String endTime = null;

            startDate = getDateTime(finalText).get(0);
            startTime = getDateTime(finalText).get(1);
            endDate = getDateTime(finalText).get(2);
            endTime = getDateTime(finalText).get(3);

            //Store into sharedPreference
            File photofile = new File(fileUri.toString());
            String prefName = photofile.getName();
            SharedPreferences sharedPref = getSharedPreferences(prefName, 0); // 0 for private mode
            SharedPreferences.Editor editor = sharedPref.edit();
            if (startDate != null){
                editor.putString("fromDate", startDate);
            }
            if (startTime != null){
                editor.putString("fromTime", startTime);
            }
            if (endDate != null){
                editor.putString("toDate", endDate);
            }
            if (endTime != null){
                editor.putString("toTime", endTime);
            }
            editor.putString("imgPath", image_path);
            editor.apply();
            Intent doneintent = new Intent(EditOCR.this, Form.class);
            doneintent.putExtra("prefName", prefName);
            startActivity(doneintent);


        });
    }

    // method for getting text from OCR call
    private String processTextRecognitionResult(FirebaseVisionText texts) {
        String result = "";
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            return result;
        }

        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    result = result.concat(elements.get(k).getText()).concat(" ");
                }
            }
        }

        return result;
    }

    // method for parsing text
    public static ArrayList<String> getDateTime(String text){
        ArrayList<String> finalList = new ArrayList<>();
        List<DateGroup> dateGroups =new Parser().parse(text);
        String startDate = null;
        String endDate = null ;
        String startTime = null;
        String endTime = null;
        String patternDate = "dd-MMM-yyyy EE";
        String patternTime = "HH:mm";
        DateFormat df1 = new SimpleDateFormat(patternDate);
        DateFormat df2 = new SimpleDateFormat(patternTime);
        Date eventStartDate;
        Date eventEndDate;

        if (dateGroups.size()==1){
            List<Date> dates = dateGroups.get(0).getDates();
            if (dates.size()==1){
                eventStartDate = dates.get(0);
                startTime = df2.format(eventStartDate);
                startDate = df1.format(eventStartDate);
            } else {
                eventStartDate = dates.get(0);
                startTime = df2.format(eventStartDate);
                startDate = df1.format(eventStartDate);

                eventEndDate = dates.get(1);
                endTime = df2.format(eventEndDate);
                endDate = df1.format(eventEndDate);
            }

        } else if (dateGroups.size()>=2) {
            eventStartDate = dateGroups.get(0).getDates().get(0);
            startTime = df2.format(eventStartDate);
            startDate = df1.format(eventStartDate);

            eventEndDate = dateGroups.get(1).getDates().get(0);
            endTime = df2.format(eventEndDate);
            endDate = df1.format(eventEndDate);
        }
        finalList.add(startDate);
        finalList.add(startTime);
        finalList.add(endDate);
        finalList.add(endTime);
        return finalList;
    }
}



