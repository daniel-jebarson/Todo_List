package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class AddStopWatch extends AppCompatActivity {
    private Chronometer chronometer;
    private Button startbutton;
    private Button resetbutton;
    private Button stopbutton;
    private long pauseoffset;
    private Button backtomain;
    private Boolean running=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.DarkTheme);
        }
        else
        {
            setTheme(R.style.Apptheme);
        }
        setContentView(R.layout.activity_add_stop_watch);
        chronometer=findViewById(R.id.chronometer);
        chronometer.setFormat("Stopwatch:  %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        startbutton=findViewById(R.id.starttimer);
        resetbutton=findViewById(R.id.resettimer);
        stopbutton=findViewById(R.id.stoptimer);
        backtomain=findViewById(R.id.backtomain);

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!running)
                {
                    chronometer.setBase(SystemClock.elapsedRealtime()-pauseoffset);
                    chronometer.start();
                    running=true;
                }
            }
        });
        stopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(running)
                {
                    chronometer.stop();
                    pauseoffset=SystemClock.elapsedRealtime()-chronometer.getBase();
                    running=false;
                }
            }
        });
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseoffset=0;
            }
        });
        backtomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseoffset=0;
                Intent intent=new Intent(AddStopWatch.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}