package com.example.java_1d;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PICTURE_CAPTURE = 1;
    CameraUtils cameraUtils = new CameraUtils(this);
    private static int RESULT_LOAD_IMG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener((navListener));

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.action_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.action_capture:
                            selectedFragment = new CaptureFragment();
                            break;
                        case R.id.action_gallery:
                            selectedFragment = new GalleryFragment();
                            break;
                        case R.id.action_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("myMessage", String.format("Request code %d, Result code %d, Data: %s", requestCode, resultCode, data.toString()));
        super.onActivityResult(requestCode, resultCode, data);
        final ImageView thumbnailImg = (ImageView) findViewById(R.id.thumbnail);

        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("myMessage", "Im in onActivityResult take photo");
            File imgFile = new File(cameraUtils.imgFilePath);
            Uri photoURI = Uri.fromFile(imgFile);
            Toast.makeText(this, "Photo saved to " + cameraUtils.imgFilePath, Toast.LENGTH_LONG).show();
            if (imgFile.exists()) {
                cameraUtils.galleryAddPic();
                Intent changePage = new Intent(MainActivity.this, EditOCR.class);
                changePage.putExtra("Image",photoURI.toString());
                startActivity(changePage);
                finish();
            }
        }

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            Log.d("myMessage", "Switching intent to OCR...");
            final Uri imageUri = data.getData();
            Intent changePage = new Intent(MainActivity.this, EditOCR.class);
            changePage.putExtra("Image",imageUri.toString());
            startActivity(changePage);
            finish();

        }
    }
}
