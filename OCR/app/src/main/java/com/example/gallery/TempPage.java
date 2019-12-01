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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;




public class TempPage extends AppCompatActivity {

    private static final int REQUEST_PICTURE_CAPTURE = 1;
    CameraUtils cameraUtils = new CameraUtils(this);
    private static int RESULT_LOAD_IMG = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_page);
        final Button cameraButton = (Button) findViewById(R.id.btnCamera);
        Log.i("myMessage", "CameraButton: "+cameraButton.toString());
        final Button galleryButton = (Button) findViewById(R.id.btnGallery);


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    take_photo();
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

    }

    protected void take_photo() {
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
            Uri photoURI = Uri.fromFile(imgFile);
            Toast.makeText(this, "Photo saved to " + cameraUtils.imgFilePath, Toast.LENGTH_LONG).show();
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                cameraUtils.galleryAddPic();

                try {
                    Intent changePage = new Intent(TempPage.this, editOCR.class);
                    Bitmap newImage = cameraUtils.processThumbnail(myBitmap);

                    changePage.putExtra("Image",photoURI.toString());
                    startActivity(changePage);
                    finish();



                } catch (IOException ioex) {
                    Toast.makeText(this, ioex.getMessage(), Toast.LENGTH_SHORT).show();
                    thumbnailImg.setImageBitmap(myBitmap);


                }
            }
        }

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap newImage = BitmapFactory.decodeStream(imageStream);

                Intent changePage = new Intent(TempPage.this, editOCR.class);

                changePage.putExtra("Image",imageUri.toString());
                startActivity(changePage);
                finish();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
