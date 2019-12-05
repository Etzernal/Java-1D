package com.example.java_1d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * This Activity is created for testing purposes only!
 * Please do not include it in the final application!
 */
public class SPCreator extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 200;
    private EditText title;
    private EditText spid;
    private EditText fromDate;
    private EditText fromTime;
    private EditText toDate;
    private EditText toTime;
    private EditText des;
    private EditText loc;
    private Button saveBtn;
    private Button confirmBtn;
    private ImageButton cameraBtn;
    private ImageButton upBtn;
    private ImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spcreator);

        saveBtn = findViewById(R.id.saveDraftBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        cameraBtn = findViewById(R.id.cameraBtn);
        upBtn = findViewById(R.id.upBtn);

        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SPCreator.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoCaptured.getInstance().setImgPath("");

                title = findViewById(R.id.title_entry);
                fromDate = findViewById(R.id.fromDate);
                toDate = findViewById(R.id.toDate);
                fromTime = findViewById(R.id.fromTime);
                toTime = findViewById(R.id.toTime);
                des = findViewById(R.id.des);
                loc = findViewById(R.id.loc);

                String id = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                SharedPreferences master = getSharedPreferences("master", MODE_PRIVATE);
                SharedPreferences.Editor masterEditor = master.edit();
                masterEditor.putString(id, id);
                masterEditor.apply();

                SharedPreferences entrySP = getSharedPreferences(id, MODE_PRIVATE);
                SharedPreferences.Editor editor = entrySP.edit();
                editor.putString("title", title.getText().toString());
                editor.putString("fromDate", fromDate.getText().toString());
                editor.putString("toDate", toDate.getText().toString());
                editor.putString("fromTime", fromTime.getText().toString());
                editor.putString("toTime", toTime.getText().toString());
                editor.putString("description", des.getText().toString());
                editor.putString("location", loc.getText().toString());
                editor.putBoolean("draft", true);
                if (photoCaptured.getInstance().getImgPath() == null){
                    editor.putString("imgPath", "");
                } else {
                    editor.putString("imgPath", photoCaptured.getInstance().getImgPath());
                }
                editor.apply();

                Toast.makeText(SPCreator.this, "Draft saved with id: "+id, Toast.LENGTH_LONG).show();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoCaptured.getInstance().setImgPath("");

                title = findViewById(R.id.title_entry);
                fromDate = findViewById(R.id.fromDate);
                toDate = findViewById(R.id.toDate);
                fromTime = findViewById(R.id.fromTime);
                toTime = findViewById(R.id.toTime);
                des = findViewById(R.id.des);
                loc = findViewById(R.id.loc);

                String id = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                SharedPreferences master = getSharedPreferences("master", MODE_PRIVATE);
                SharedPreferences.Editor masterEditor = master.edit();
                masterEditor.putString(id, id);
                masterEditor.apply();

                SharedPreferences entrySP = getSharedPreferences(id, MODE_PRIVATE);
                SharedPreferences.Editor editor = entrySP.edit();
                editor.putString("title", title.getText().toString());
                editor.putString("fromDate", fromDate.getText().toString());
                editor.putString("toDate", toDate.getText().toString());
                editor.putString("fromTime", fromTime.getText().toString());
                editor.putString("toTime", toTime.getText().toString());
                editor.putString("description", des.getText().toString());
                editor.putString("location", loc.getText().toString());
                editor.putBoolean("draft", false);  // For confirmed entry, set this to false so that it is not recognised as draft
                if (photoCaptured.getInstance().getImgPath() == null){
                    editor.putString("imgPath", "");
                } else {
                    editor.putString("imgPath", photoCaptured.getInstance().getImgPath());
                }
                editor.apply();

                Toast.makeText(SPCreator.this, "Entry confirmed with id: "+id, Toast.LENGTH_LONG).show();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String imgPath = photoCaptured.getInstance().getImgPath();
            Bitmap imageBitmap = BitmapFactory.decodeFile(imgPath);
            thumbnail = findViewById(R.id.tn);
            thumbnail.setImageBitmap(imageBitmap);
        }
    }

    private void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("myLog", ex.getMessage());
                photoFile = null;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.java_1d.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "KRONOS_" + timeStamp;
        File rootPath = getExternalFilesDir("Pictures");
        File image = new File(rootPath.getAbsolutePath()+"/"+imageFileName+".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        photoCaptured.getInstance().setImgPath(image.getAbsolutePath());
        return image;
    }
}
