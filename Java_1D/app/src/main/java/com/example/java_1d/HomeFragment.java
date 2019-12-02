package com.example.java_1d;

import android.app.ActionBar;
import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.drm.DrmStore;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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



public class HomeFragment extends Fragment {
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Activity thisActivity;
        if (context instanceof MainActivity){
            thisActivity = (Activity) context;
        }
    }

    private static final int REQUEST_PICTURE_CAPTURE = 1;
    CameraUtils cameraUtils = new CameraUtils(super.getContext());
    private static int RESULT_LOAD_IMG = 2;

    SharedPreferences Event;
    public void createEvent(){
        try {
            Event = this.getActivity().getSharedPreferences("Info", 0);
        } catch (Exception exception) {
            Event = null;
        }
    }


    public static String[] eventInfo(SharedPreferences pref) {
        if(pref!=null) {
            String[] out=new String[6];
            String fromDate = pref.getString("fromDate", "");
            String toDate = pref.getString("toDate", "");
            out[5]= pref.getString("fromTime", "");
            out[4]= pref.getString("toTime", "");
            out[3]= pref.getString("title", "");
            String fromDateDigits = stripNonDigits(fromDate);
            out[2] = fromDateDigits.substring(4);
            out[1] = fromDateDigits.substring(2, 4);
            out[0]= fromDateDigits.substring(0, 2);
            return out;
        }
        return null;

    }



    public static String stripNonDigits(String input){
        String out="";
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);
            if(c > 47 && c < 58){
                out+=i;
            }
        }
        return out;
    }


    TextView date_tv;
    TextView event_view;
    SimpleDateFormat dateFormatMonth = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView date_select = view.findViewById(R.id.selected_date);
        final TextView date_events = view.findViewById(R.id.date_events);
        final CalendarView calendarView = view.findViewById(R.id.calendar_view);
        final TextView event_view = view.findViewById(R.id.event_text);
        final FloatingActionButton fabcam = view.findViewById(R.id.floatingActionButtoncam);
        final FloatingActionButton fabgal = view.findViewById(R.id.floatingActionButtongal);
        final FloatingActionButton fabadd = view.findViewById(R.id.floatingActionButtonAdd);

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
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("content://media/external/images/media/"));
                startActivity(intent);
            }
        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            String textDisplayed;

            public String[] storeInfo(SharedPreferences pref) {
                if (pref != null) {
                    String[] info = new String[6];
                    String fromDate = pref.getString("fromDate", "");
                    String toDate = pref.getString("toDate", "");
                    info[1] = pref.getString("fromTime", "");
                    info[2] = pref.getString("toTime", "");
                    info[0] = pref.getString("title", "");
                    String fromDateDigits = stripNonDigits(fromDate);
                    info[5] = fromDateDigits.substring(4);
                    info[4] = fromDateDigits.substring(2, 4);
                    info[3] = fromDateDigits.substring(0, 2);
                    return info;
                }
                return null;
            }


            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String[] info = eventInfo(Event);
                if (info != null) {
                    if (String.valueOf(year).equals(info[5]) && String.valueOf(month + 1).equals(info[4]) && String.valueOf(dayOfMonth).equals(info[3])) {
                        textDisplayed = info[0] + ":" + info[1] + "-" + info[2];
                    } else {
                        textDisplayed = "";
                    }
                }

                date_select.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year)
                        + " \n");
//                date_events.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth)
//                        + " \n" );

//
//                event_view.setText(textDisplayed);
////                Toast.makeText(getContext(), dayOfMonth + "-" + month + "-" + year, Toast.LENGTH_SHORT).show();
//
//                calendarView.setVisibility(View.GONE);

            }
        });


        date_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setVisibility(calendarView.isShown()
                        ? View.GONE
                        : View.VISIBLE);
            }
        });
//        event_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calendarView.setVisibility(calendarView.isShown()
//                        ? View.GONE
//                        : View.VISIBLE);
//            }
//        });


        fabcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                take_photo();
                return;
            }
        });
        return view;
    }

    public void take_photo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            Log.d("MainActivity","I am heree");
            File pictureFile;
            try {
                pictureFile = cameraUtils.createUniqueImageFilename();
            } catch (IOException ex) {
                Toast.makeText(getContext(),
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            } catch (NullPointerException exn){Toast.makeText(getContext(), "Cannot get external files directory", Toast.LENGTH_SHORT).show(); return;}
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }


}

