package com.example.java_1d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.fortuna.ical4j.model.property.Description;

public class ViewEntry extends AppCompatActivity {

    private static final int GET_VIEW_ENTRY = 400;
    private Button editBtn;
    private String spid;  // SharedPreferences ID
    private String toDisplay;

    //add in edit function  at the top right hand


    //take in intent when click on home fragment

    //shared pref
    public void displayInfo(){
        SharedPreferences mypref= getSharedPreferences(spid, 0);
        if (mypref!=null){
            String title = mypref.getString("title", "");
            String location = mypref.getString("location","");
//            String fromDate = mypref.getString("fromDate", "");
//            String toDate = mypref.getString("toDate", "");
            String fromTime = mypref.getString("fromTime", "");
            String toTime = mypref.getString("toTime", "");
            String desc=mypref.getString("description","");
            toDisplay+=title;
            toDisplay+="Location: "+location;
            toDisplay+="Time: "+fromTime+"-"+toTime;
            toDisplay+="Description: "+desc;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);
        displayInfo();
        TextView textView=findViewById(R.id.information);
        textView.setText(toDisplay);

        editBtn = findViewById(R.id.editButton);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: switch to Form page
                return;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_VIEW_ENTRY && resultCode == RESULT_OK){
            spid = data.getStringExtra("id");  // this is the SP id

        }
    }
}
