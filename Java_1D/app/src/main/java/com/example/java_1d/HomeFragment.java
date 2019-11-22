package com.example.java_1d;

import android.app.ActionBar;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.drm.DrmStore;
import android.graphics.Color;
import android.os.Bundle;
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

    TextView date_tv;
    TextView event_view;
    SimpleDateFormat dateFormatMonth = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView date_tv = (TextView) view.findViewById(R.id.date_text);
        final CalendarView calendarView = (CalendarView) view.findViewById (R.id.calendar_view);
        final TextView event_view = (TextView) view.findViewById(R.id.event_text);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date_tv.setSingleLine(false);
                date_tv.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth)
                        + " \n" );
//                date_tv.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth)
//                        + " \n" );
                event_view.setText("7.30pm - " + "How to be a Carebear");
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


}

