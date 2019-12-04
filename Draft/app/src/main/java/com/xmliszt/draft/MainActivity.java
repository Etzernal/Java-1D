package com.xmliszt.draft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LinearLayout drafts_space;
    private ImageButton toSPBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drafts_space = findViewById(R.id.draft_space);

        // get the master SP and loop through its contents to generate entry
        Map<String, ?> draftsMap = getSharedPreferences("master", MODE_PRIVATE).getAll();
        int index = 0;
        for (Map.Entry<String, ?> entry: draftsMap.entrySet()){
            String spid = entry.getKey();
            Log.d("myLog", "SPID: "+spid);
            ConstraintLayout draftEntry = generateDraftEntry(spid);
            if (draftEntry != null){
                drafts_space.addView(draftEntry, index);
                index ++;
            }
        }

        toSPBtn = findViewById(R.id.toSPCreator);
        toSPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SPCreator.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * programmatically create a ConstraintLayout that contains all contents of one draft entry,
     * using the SharedPreferences ID (spid) that is passed in to locate the specific SP contents
     * @param spid SharedPreferences ID
     * @return A ConstraintLayout that can be added to the LinearLayout to display the draft entry
     */
    private ConstraintLayout generateDraftEntry(final String spid){

        // getting all contents
        SharedPreferences sp = getSharedPreferences(spid, MODE_PRIVATE);
        final String title = sp.getString("title", "");
        String location = sp.getString("location","");
        String fromDate = sp.getString("fromDate", "");
        String toDate = sp.getString("toDate", "");
        String fromTime = sp.getString("fromTime", "");
        String toTime = sp.getString("toTime", "");
        String imgPath = sp.getString("imgPath", "");
        Boolean isDraft = sp.getBoolean("draft", true);

        if(!isDraft){
            return null;
        } else {
            ConstraintLayout constraintLayout = new ConstraintLayout(this);
            constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));

            TextView infoDisplay = new TextView(this);
            ImageView thumbnail = new ImageView(this);

            // configure constraints
            ConstraintLayout.LayoutParams infoLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
            infoLayout.setMargins(16, 16, 16, 16);
            infoDisplay.setLayoutParams(infoLayout);

            ConstraintLayout.LayoutParams thumbnailLayout = new ConstraintLayout.LayoutParams(300,300);
            thumbnailLayout.setMargins(16,16,16,16);
            thumbnail.setLayoutParams(thumbnailLayout);

            // set IDs
            constraintLayout.setId(View.generateViewId());
            infoDisplay.setId(View.generateViewId());
            thumbnail.setId(View.generateViewId());

            constraintLayout.addView(thumbnail, 0);
            constraintLayout.addView(infoDisplay, 1);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(thumbnail.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
            constraintSet.connect(thumbnail.getId(),ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(thumbnail.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
            constraintSet.connect(thumbnail.getId(), ConstraintSet.RIGHT, infoDisplay.getId(), ConstraintSet.LEFT);
            constraintSet.connect(infoDisplay.getId(), ConstraintSet.TOP, thumbnail.getId(), ConstraintSet.TOP);
            constraintSet.connect(infoDisplay.getId(), ConstraintSet.BOTTOM, thumbnail.getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(infoDisplay.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
            constraintSet.connect(infoDisplay.getId(), ConstraintSet.LEFT, thumbnail.getId(), ConstraintSet.RIGHT);
            constraintSet.applyTo(constraintLayout);

            // set contents
            String info = buildInfoDisplay(title, location, fromDate, toDate, fromTime, toTime);
            infoDisplay.setText(info);
            Log.i("myLog", info);

            if (imgPath == ""){
                thumbnail.setImageResource(R.drawable.placeholder);
            } else {
                File imgFile = new File(imgPath);
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                thumbnail.setImageBitmap(bitmap);
            }

            // set clickable event
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("myLog", String.format("Direct to entry editing page with ID <%s> and Title <%s>", spid, title));
                    return;
                }
            });


            return constraintLayout;
        }
    }

    /**
     * Generate the info string to be displayed in the draft entry list
     * @param title title of entry
     * @param location location of entry
     * @param fromDate fromDate string
     * @param toDate toDate string
     * @param fromTime fromTime string
     * @param toTime toTime string
     * @return info string to be displayed
     */
    private String buildInfoDisplay(String title, String location, String fromDate, String toDate, String fromTime, String toTime){
        return String.format("%s\n%s\n%s - %s\n%s ~ %s", title, location, fromDate, toDate, fromTime, toTime);
    }
}
