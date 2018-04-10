package com.example.lowryj.myplanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView cView;
    private Toolbar tb;
    static final int ADD_ENTRY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cView = findViewById(R.id.pView);
        Toolbar tb = findViewById(R.id.my_toolbar);
        tb.setTitle("My Planner");
        setSupportActionBar(tb);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add_new_entry:
                addNewEntry(item.getActionView());
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addNewEntry(View v) {
        Intent intent = new Intent(this, AddEvent.class);
        long date = cView.getDate();
        intent.putExtra("MYPLANNER_DATE", date);
        startActivityForResult(intent, ADD_ENTRY_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_ENTRY_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Assignment newAssignment = data.getParcelableExtra("ASSIGNMENT");

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }


    public void saveAssignment(Assignment assignment) {
        String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        CSVWriter writer = new CSVWriter(new FileWriter(csv));

        List<String[]> data = new ArrayList<String[]>();
        data.add(new String[] {"India", "New Delhi"});
        data.add(new String[] {"United States", "Washington D.C"});
        data.add(new String[] {"Germany", "Berlin"});

        writer.writeAll(data);

        writer.close();
    }
}
