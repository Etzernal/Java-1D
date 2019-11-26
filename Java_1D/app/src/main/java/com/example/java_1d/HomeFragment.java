package com.example.java_1d;

import android.app.ActionBar;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.SharedPreferences;
import android.drm.DrmStore;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment {
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

<<<<<<< Updated upstream
=======
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

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    TextView date_tv;
    TextView event_view;
    SimpleDateFormat dateFormatMonth = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView date_tv = (TextView) view.findViewById(R.id.date_text);
        final CalendarView calendarView = (CalendarView) view.findViewById (R.id.calendar_view);
        final TextView event_view = (TextView) view.findViewById(R.id.event_text);



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            String textDisplayed;

            public String[] storeInfo(SharedPreferences pref){
                if (pref!=null){
                    String[] info=new String[6];
                    String fromDate = pref.getString("fromDate", "");
                    String toDate = pref.getString("toDate", "");
                    info[1] = pref.getString("fromTime", "");
                    info[2] = pref.getString("toTime", "");
                    info[0] = pref.getString("title", "");
                    String fromDateDigits= stripNonDigits(fromDate);
                    info[5]=fromDateDigits.substring(4);
                    info[4]=fromDateDigits.substring(2,4);
                    info[3]=fromDateDigits.substring(0,2);
                    return info;
                }
                return null;
            }




            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String[] info=eventInfo(Event);
                if (info!=null){
                    if(String.valueOf(year).equals(info[5]) && String.valueOf(month + 1).equals(info[4])&& String.valueOf(dayOfMonth).equals(info[3])){
                        textDisplayed= info[0]+":"+info[1]+"-"+info[2];
                    }
                else{textDisplayed="";}}

                date_tv.setSingleLine(false);
                date_tv.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth)
                        + " \n" );
//                date_tv.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth)
//                        + " \n" );


                    event_view.setText(textDisplayed);
//                Toast.makeText(getContext(), dayOfMonth + "-" + month + "-" + year, Toast.LENGTH_SHORT).show();

                calendarView.setVisibility(View.GONE); }
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


}

