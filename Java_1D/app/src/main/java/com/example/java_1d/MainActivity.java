package com.example.java_1d;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int PERMIT_STORAGE = 400;
    private static final int PERMIT_CAMERA = 401;
    private photoCaptured pc = photoCaptured.getInstance();

    private static final int REQUEST_PICTURE_CAPTURE = 1;
    private static int RESULT_LOAD_IMG = 2;
    private static final int PICK_IMAGE = 300;

    private Text user_email;
    private String password = "password";
    private String username = "User";

    private Boolean grantedCam;
    private Boolean grantedStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
=======
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
>>>>>>> 72aa16caf562693ea94c1e15d45aaf88231d353f
>>>>>>> Stashed changes

        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener((navListener));

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNav.setSelectedItemId(R.id.action_home);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMIT_CAMERA);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMIT_STORAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grandResults){
        switch (requestCode){
            case PERMIT_CAMERA: {
                if(grandResults.length>0 && grandResults[0] == PackageManager.PERMISSION_GRANTED){
                    grantedCam = true;
                }
            }

            case PERMIT_STORAGE: {
                if(grandResults.length>0 && grandResults[0] == PackageManager.PERMISSION_GRANTED){
                    grantedStore = true;
                }
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.action_drafts:
                            selectedFragment = new DraftsFragment();
                            break;
                        case R.id.action_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.action_settings:
                            selectedFragment = new SettingsFragment();
//                            final Button update = findViewById(R.id.settings_update_button);
//                            update.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    //no idea what to put in here
//                                }
//                            });

                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            Log.i("myDebug", "Passing Camera taken photo to OCR...");
            File imgFile = new File(pc.getImgPath());
            Uri photoURI = Uri.fromFile(imgFile);
            Toast.makeText(this, "Photo saved to " + pc.getImgPath(), Toast.LENGTH_SHORT).show();
            if (imgFile.exists()) {
                // pass the image file as String to EditOCR
                Intent changePage = new Intent(MainActivity.this, EditOCR.class);
                changePage.putExtra("Image",photoURI.toString());
                startActivity(changePage);
                finish();
            }
        }

        else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            Log.i("myDebug", "Passing Gallery selected photo to OCR...");
            Uri imageChosen = data.getData();
            if (imageChosen != null){
                Intent changePage = new Intent(MainActivity.this, EditOCR.class);
                changePage.putExtra("Image",imageChosen.toString());
                startActivity(changePage);
                finish();
            }
        }

        else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            Log.i("myDebug", "Switching intent to OCR...");
            final Uri imageUri = data.getData();
            Intent changePage = new Intent(MainActivity.this, EditOCR.class);
            changePage.putExtra("Image",imageUri.toString());
            startActivity(changePage);
            finish();

        }
    }
}
