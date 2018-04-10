package com.example.lowryj.myplanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /* {month, day, year, hour, minute} */
    private Calendar reminderDate;

    private int reminderPreset = 0;
    private Date dueDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_a_new_assignment);
        setSupportActionBar(toolbar);

        Spinner spinner = findViewById(R.id.reminder_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayList<String> spinnerOptions = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.assignment_reminder_spinner_entries)));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Intent intent = getIntent();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMain(view);
            }
        });

        reminderDate = Calendar.getInstance();

        EditText name = findViewById(R.id.assignment_name_entry);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void selectDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(),"dueDate");
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void goToMain(View v) {
        String assignmentName = ((EditText) findViewById(R.id.assignment_name_entry)).getText().toString();
        if(assignmentName.equals("") || reminderPreset == 0 || dueDate == null) {
            String error = "";
            if(assignmentName.equals("")) {
                error += AddEvent.this.getString(R.string.please_name_the_assignment) + "\n";
            }
            if(reminderPreset == 0) {
                error += AddEvent.this.getString(R.string.please_set_a_reminder) + "\n";
            }
            if(dueDate == null) {
                error += AddEvent.this.getString(R.string.please_select_a_due_date) + "\n";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEvent.this);
            builder.setMessage(error);

            builder.setNeutralButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return;
        }
        switch(reminderPreset) {
            case 1:
                reminderDate.setTime(dueDate);
                reminderDate.add(Calendar.DATE, -7);
                break;
            case 2:
                reminderDate.setTime(dueDate);
                reminderDate.add(Calendar.DATE, -1);
                break;
            case 3:
                reminderDate.setTime(dueDate);
                reminderDate.add(Calendar.DATE, -2);
                break;
            default:
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ASSIGNMENT", new Assignment(assignmentName, reminderDate, dueDate));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch((String) parent.getItemAtPosition(pos)) {
            case "A week before":
                reminderPreset = 1;
                break;
            case "The day before":
                reminderPreset = 2;
                break;
            case "Two days before":
                reminderPreset = 3;
                break;
            case "Custom…":
                reminderPreset = -1;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "customReminderDate");
                break;
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String tag = (String) view.getTag();
        if(tag.equals("customReminderDate")) {
            reminderDate.set(year, month, day);
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(), "customReminderTime");
        }
        if(tag.equals("dueDate")) {
            dueDate = new Date(year, month, day);
            EditText date = findViewById(R.id.date_picker_text);
            date.setText(String.format(Locale.ENGLISH,"%02d/%02d/%04d", month, day, year));
        }
    }

    @SuppressWarnings("unchecked")
    public void onTimeSet(TimePicker view, int hour, int minute) {
        reminderDate.set(reminderDate.get(Calendar.YEAR), reminderDate.get(Calendar.MONTH), reminderDate.get(Calendar.DAY_OF_MONTH), hour, minute);
        Spinner spinner = findViewById(R.id.reminder_spinner);
        ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinner.getAdapter();
        String info = String.format(Locale.ENGLISH, "Remind me at %d:%d on %02d/%02d/%04d",
                reminderDate.get(Calendar.HOUR_OF_DAY),
                reminderDate.get(Calendar.MINUTE),
                reminderDate.get(Calendar.MONTH),
                reminderDate.get(Calendar.DAY_OF_MONTH),
                reminderDate.get(Calendar.YEAR));
        spinnerAdapter.add(info);
        spinnerAdapter.notifyDataSetChanged();
        spinner.setSelection(spinnerAdapter.getPosition(info));
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }
}
