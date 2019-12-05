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
import android.text.method.ScrollingMovementMethod;
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
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;


public class EditOCR extends AppCompatActivity {
    Button cropBtn ;
    Button backBtn ;
    Button doneBtn ;
    CropImageView eventImg ;
    TextView testView;
    String image_path ;
    Context context1 = EditOCR.this;
    String[] info = new String[2];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ocr);
        Log.d("myMessage", "trying to oncreate");

        cropBtn = findViewById(R.id.btnCrop);
        backBtn = findViewById(R.id.btnBack);
        doneBtn = findViewById(R.id.btnDone);
        eventImg = findViewById(R.id.imgEvent);
        image_path = getIntent().getStringExtra("Image");
        eventImg = findViewById(R.id.imgEvent);
        testView = findViewById(R.id.testView);




        //set selected image in imageView
        Uri fileUri = Uri.parse(image_path);
        eventImg.setImageUriAsync(fileUri);

        // does OCR for overall image and stores string in info[0]
        FirebaseVisionImage image;
        try {
            image = FirebaseVisionImage.fromFilePath(context1, fileUri);
            FirebaseVisionTextRecognizer detector =
                    FirebaseVision.getInstance().getOnDeviceTextRecognizer();

            detector.processImage(image)
                    .addOnSuccessListener(texts -> {
                        info[0] = processTextRecognitionResult(texts);
                        String dateTimetext = info[0];
                        String fromDate = getDateTime(dateTimetext).get(0);
                        testView.setText(fromDate);
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }


        cropBtn.setOnClickListener((View v) -> {
            Bitmap[] cropped = eventImg.getCroppedImage(500, 500);
            // do google vision OCR on either cropped[0] or cropped[1]
            // this is for title
            // store the string in info[1]
        });


        // function to go to form page
        // parse for date and time
        doneBtn.setOnClickListener((View v) -> {

            //Parse for Date and Time from the overall OCR
            String dateTimetext = info[0];
            String fromDate = "";
            String toDate = "" ;
            String fromTime = "";
            String toTime = "";
            String title = ""; // put title = info[1]

            fromDate = getDateTime(dateTimetext).get(0);
            fromTime = getDateTime(dateTimetext).get(1);
            toDate = getDateTime(dateTimetext).get(2);
            toTime = getDateTime(dateTimetext).get(3);

            // store into intent
            File photofile = new File(fileUri.toString());
            String idRaw = photofile.getName();
            String id = idRaw.substring(0, idRaw.lastIndexOf('.'));

            Intent doneIntent = new Intent(EditOCR.this, Form.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("title", "");
            bundle.putString("fromDate", fromDate);
            bundle.putString("fromTime", fromTime);
            bundle.putString("toDate", toDate);
            bundle.putString("toTime", toTime);
            bundle.putString("des", "");
            bundle.putString("loc", "");
            bundle.putString("imgPath", image_path);
            doneIntent.putExtras(bundle);
            startActivity(doneIntent);

        });

        backBtn.setOnClickListener((View v) ->{
            Intent goBack = new Intent(EditOCR.this, MainActivity.class);
            startActivity(goBack);
            finish();

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
        String startDate = "";
        String endDate = "";
        String startTime = "";
        String endTime = "";
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



