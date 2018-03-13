package com.example.lowryj.myplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private CalendarView cView;
    private Toolbar tb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cView = findViewById(R.id.pView);
        Toolbar tb = findViewById(R.id.my_toolbar);
        setSupportActionBar(tb);
    }

    public void onClick(View v) {

    }
}
