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
import java.util.ArrayList;
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

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Activity thisActivity;
        if (context instanceof MainActivity){
            thisActivity = (Activity) context;
        }
    }

    private static final int REQUEST_PICTURE_CAPTURE = 1;
    CameraUtils cameraUtils = new CameraUtils(getActivity());
    private static int RESULT_LOAD_IMG = 2;


    File file;// = this.getActivity().getExternalFilesDir("");
    File[] directoryListing ;//= file.listFiles();
//    Log.i("fileDebug", file.toString());
//    String myDirectoryPath =file.toString();
//
//    File dir = new File(myDirectoryPath); //place where sharedprefs are stored



//    public ArrayList<String> loop_through_file(File[] directoryListing) {
//        if (directoryListing != null) {
//            for (File child : directoryListing) {
//                //read pref name
//                //add child to output list
//                //return list of all shared prefs
//            }
//        } else {
//            //return null
//        }
//        return null;
//    }

    //    ArrayList<String> list_SP= loop_through_file(directoryListing);
    ArrayList<SharedPreferences> Events=new ArrayList<>(); //list of shared prefs


    public void createEvents() {
        if (directoryListing != null) {
            Log.d("fileDebug","first");
            for (File i : directoryListing) {
                try {
                    SharedPreferences Event = this.getActivity().getSharedPreferences(i.getName(), 0);
                    if(Event!=null){
                        Events.add(Event);}
                } catch (Exception exception) {
                    Log.d("fileDebug","Exception");
                }
            }
        }
    }
    ArrayList<String[]> allEventInfo=new ArrayList<>();

    public void allEventInfo(){
        for(SharedPreferences i: Events){
            allEventInfo.add(eventInfo(i));
            Log.d("fileDebug","i m here");
        }
    }



    public static String[] eventInfo(SharedPreferences pref) {
//        if(pref!=null) {
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
//        }
//        return null;

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
        final TextView date_tv = view.findViewById(R.id.date_text);
        final CalendarView calendarView = view.findViewById (R.id.calendar_view);
        final TextView event_view = view.findViewById(R.id.event_text);
        FloatingActionButton fabcam = view.findViewById(R.id.floatingActionButtoncam);
        FloatingActionButton fabgal = view.findViewById(R.id.floatingActionButtongal);


        fabcam.setOnClickListener((View v) -> {
            take_photo();


        });

        fabgal.setOnClickListener((View v) -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            getActivity().startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            String textDisplayed="";

//            public String[] storeInfo(SharedPreferences pref){
//                if (pref!=null){
//                    String[] info=new String[6];
//                    String fromDate = pref.getString("fromDate", "");
//                    String toDate = pref.getString("toDate", "");
//                    info[1] = pref.getString("fromTime", "");
//                    info[2] = pref.getString("toTime", "");
//                    info[0] = pref.getString("title", "");
//                    String fromDateDigits= stripNonDigits(fromDate);
//                    info[5]=fromDateDigits.substring(4);
//                    info[4]=fromDateDigits.substring(2,4);
//                    info[3]=fromDateDigits.substring(0,2);
//                    return info;
//                }
//                return null;
//            }




            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                createEvents();
                allEventInfo();


                for(String[] info:allEventInfo){
                    if(String.valueOf(year).equals(info[5]) && String.valueOf(month + 1).equals(info[4])&& String.valueOf(dayOfMonth).equals(info[3])){
                        textDisplayed+= info[0]+":"+info[1]+"-"+info[2]+"\n";
                    }
                }


                date_tv.setSingleLine(false);
                date_tv.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year)
                        + " \n" );
//                date_tv.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth)
//                        + " \n" );


                event_view.setText(textDisplayed);
//                Toast.makeText(getContext(), dayOfMonth + "-" + month + "-" + year, Toast.LENGTH_SHORT).show();

                calendarView.setVisibility(View.GONE);

            }
        });


        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setVisibility(calendarView.isShown()
                        ? View.GONE
                        : View.VISIBLE);
            }
        });
        event_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setVisibility(calendarView.isShown()
                        ? View.GONE
                        : View.VISIBLE);
            }
        });
        return view;
    }

    public void take_photo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            Log.d("myMessage","trying to capture photo");
            File pictureFile;
            try {
                File f = getActivity().getExternalFilesDir("");
                pictureFile = cameraUtils.createUniqueImageFilename(f);
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


}

