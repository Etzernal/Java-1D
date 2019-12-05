package com.example.java_1d;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import android.util.Log;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

    private static final int PICK_IMAGE = 300;

    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private photoCaptured pc = photoCaptured.getInstance();
    private LinearLayout entrySpace;
    private CalendarView calendar;

    // Read from master SP at onCreate and generate HashMap of the subSP content
    // "key": ID
    // "value": [displayedText, year, month, day]
    private Map<String, Map> entryContentMap = new HashMap<>();

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Activity thisActivity;
        if (context instanceof MainActivity){
            thisActivity = (Activity) context;
        }
    }

    private static final int REQUEST_PICTURE_CAPTURE = 1;

    private String getImagePathFromSP(String spid){
        SharedPreferences sp = this.getActivity().getSharedPreferences(spid, MODE_PRIVATE);
        return sp.getString("imgPath", "");
    }

    private String getFromDateFromSP(String spid){
        SharedPreferences sp = this.getActivity().getSharedPreferences(spid, MODE_PRIVATE);
        return sp.getString("fromDate", "");
    }

    private Boolean isDraft(String spid){
        SharedPreferences sp = this.getActivity().getSharedPreferences(spid, MODE_PRIVATE);
        return sp.getBoolean("draft", true);
    }

    private String generateEventInfo(String spid) {
        SharedPreferences sp = getActivity().getSharedPreferences(spid, MODE_PRIVATE);
        final String title = sp.getString("title", "");
        String location = sp.getString("location","");
        String fromDate = sp.getString("fromDate", "");
        String toDate = sp.getString("toDate", "");
        String fromTime = sp.getString("fromTime", "");
        String toTime = sp.getString("toTime", "");
        return buildInfoDisplay(title, location, fromDate, toDate, fromTime, toTime);
    }

    private String buildInfoDisplay(String title, String location, String fromDate, String toDate, String fromTime, String toTime){
        return String.format("Title: %s\nLocation: %s\n%s - %s\n%s ~ %s", title, location, fromDate, toDate, fromTime, toTime);
    }

    private int getYearFromDate(String fromDate){
        String[] s = fromDate.split("-");
        return Integer.parseInt(s[2]);
    }

    private int getMonthFromDate(String fromDate){
        String[] s = fromDate.split("-");
        String monthInText = s[1];
        return getMonthNumFromText(monthInText.toLowerCase());

    }
    private int getDayFromDate(String fromDate){
        String[] s = fromDate.split("-");
        return Integer.parseInt(s[0]);
    }

    private int getMonthNumFromText(String month){
        switch (month){
            case("jan"): return 1;
            case("feb"): return 2;
            case("mar"): return 3;
            case("apr"): return 4;
            case("may"): return 5;
            case("jun"): return 6;
            case("jul"): return 7;
            case("aug"): return 8;
            case("sep"): return 9;
            case("oct"): return 10;
            case("nov"): return 11;
            case("dec"): return 12;
            default: return 0;
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        entrySpace = view.findViewById(R.id.entrySpace);
        calendar = view.findViewById(R.id.calendar_view);
        final TextView date_select = view.findViewById(R.id.selected_date);
        final CalendarView calendarView = view.findViewById(R.id.calendar_view);
        final FloatingActionButton fabcam = view.findViewById(R.id.floatingActionButtoncam);
        final FloatingActionButton fabgal = view.findViewById(R.id.floatingActionButtongal);
        final FloatingActionButton fabadd = view.findViewById(R.id.floatingActionButtonAdd);

        // initialize ContentMap
        SharedPreferences master = getActivity().getSharedPreferences("master", MODE_PRIVATE);
        Map<String, ?> sps = master.getAll();
        for(Map.Entry<String, ?> entry: sps.entrySet()){
            String spid = entry.getKey();
            if(!isDraft(spid)){
                String eventInfoEntry = generateEventInfo(spid);
                String fromDate = getFromDateFromSP(spid);
                Map content = new HashMap();
                content.put("info", eventInfoEntry);
                content.put("year", getYearFromDate(fromDate));
                content.put("month", getMonthFromDate(fromDate));
                content.put("day", getDayFromDate(fromDate));
                Log.i("myDebug", content.toString());
                entryContentMap.put(spid, content);
            }
        }

        // initialize entry display
        entrySpace.removeAllViews();
        long dateInLong = calendar.getDate();
        Date crrtDate = new Date(dateInLong);
        Calendar cld = Calendar.getInstance();
        cld.setTime(crrtDate);
        int year = cld.get(Calendar.YEAR);
        int month = cld.get(Calendar.MONTH);
        int day = cld.get(Calendar.DAY_OF_MONTH);

        for(String k: entryContentMap.keySet()){
            Map entry = entryContentMap.get(k);
            int y = (int) entry.get("year");
            int m = (int) entry.get("month");
            int d = (int) entry.get("day");
            Log.i("myDebug", String.format("What I get: %d-%d-%d", y, m, d));
            Log.i("myDebug", String.format("Selected date: %d-%d-%d", year, month, day));
            if (year==y && month+1==m && day==d){
                //display it!
                ConstraintLayout cl = generateConstraintLayout(entry, k);
                if (cl != null){
                    entrySpace.addView(cl);
                }
            }
        }
        date_select.setSingleLine(false);
        date_select.setText(day + "-" + (month + 1) + "-" + year + " \n" );

        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_clkwise);
        fab_anticlock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_anticlkwise);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date_select.setText(sdf.format(calendarView.getDate()));

        fabadd.setOnClickListener(new View.OnClickListener() {
            boolean isOpen = false;

            @Override
            public void onClick(View v) {
                if (isOpen) {
                    fabgal.startAnimation(fab_close);
                    fabcam.startAnimation(fab_close);
                    fabadd.startAnimation(fab_anticlock);
                    fabgal.setClickable(false);
                    fabcam.setClickable(false);
                    isOpen = false;
                } else {
                    fabgal.startAnimation(fab_open);
                    fabcam.startAnimation(fab_open);
                    fabadd.startAnimation(fab_clock);
                    fabgal.setClickable(true);
                    fabcam.setClickable(true);
                    isOpen = true;
                }
            }
        });
        fabgal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        fabcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                take_photo();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                entrySpace.removeAllViews();
                // loop through the contentMap and find that matched entry to display
                for(String k: entryContentMap.keySet()){
                    Map entry = entryContentMap.get(k);
                    int y = (int) entry.get("year");
                    int m = (int) entry.get("month");
                    int d = (int) entry.get("day");
                    Log.i("myDebug", String.format("What I get: %d-%d-%d", y, m, d));
                    Log.i("myDebug", String.format("Selected date: %d-%d-%d", year, month, dayOfMonth));
                    if (year==y && month+1==m && dayOfMonth==d){
                        //display it!
                        ConstraintLayout cl = generateConstraintLayout(entry, k);
                        if (cl != null){
                            entrySpace.addView(cl);
                        }
                    }
                }
                date_select.setSingleLine(false);
                date_select.setText(dayOfMonth + "-" + (month + 1) + "-" + year + " \n" );
            }
        });
        return view;
    }

    public void take_photo() {
        pc.setContext(getContext());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            Log.d("myMessage","trying to capture photo");
            File pictureFile;
            try {
                pictureFile = pc.createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(),
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            } catch (NullPointerException exn){Toast.makeText(getContext(), "Cannot get external files directory", Toast.LENGTH_SHORT).show(); return;}
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.java_1d.fileprovider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }

    /**
     *
     * @param entry EntryMap for one entry
     * @param k Entry SharedPreferences ID
     * @return ConstraintLayout of the entry content
     */
    private ConstraintLayout generateConstraintLayout(Map entry, String k){
        ConstraintLayout constraintLayout = new ConstraintLayout(getActivity());
        constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));

        TextView infoDisplay = new TextView(getActivity());
        ImageView thumbnail = new ImageView(getActivity());

        // configure constraints
        ConstraintLayout.LayoutParams infoLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        infoLayout.setMargins(32, 16, 16, 16);
        infoDisplay.setLayoutParams(infoLayout);

        ConstraintLayout.LayoutParams thumbnailLayout = new ConstraintLayout.LayoutParams(300,300);
        thumbnailLayout.setMargins(16,16,16,16);
        thumbnail.setLayoutParams(thumbnailLayout);

        // set IDs
        constraintLayout.setId(View.generateViewId());
        infoDisplay.setId(View.generateViewId());
        thumbnail.setId(View.generateViewId());

        // set contents
        String info = (String) entry.get("info");
        infoDisplay.setText(info);
        Log.i("myLog", info);

        String imgPath = getImagePathFromSP(k);
        Log.i("myDebug", "Entry Image: "+imgPath);
        if (imgPath == ""){
            thumbnail.setImageResource(R.drawable.placeholder);
        } else {
            File imgFile = new File(imgPath);
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            thumbnail.setImageBitmap(bitmap);
        }

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

        // TODO: on-click bring up a list of
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("myLog", String.format("Direct to entry editing page with ID <%s> ", k));
            }
        });

        return constraintLayout;
    }


}

