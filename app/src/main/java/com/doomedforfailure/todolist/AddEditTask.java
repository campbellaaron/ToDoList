package com.doomedforfailure.todolist;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by aaroncampbell on 12/13/16.
 */

public class AddEditTask extends AppCompatActivity {
    private static final String TAG = "ToDoList";
    private EditText editTitle;
    private EditText editTask;
    private Button saveBtn;
    private Button imgBtn;
    private TextView dueTime;
    private TextView dueDate;
    private TextView categoryText;
    protected Spinner spinner;
    private int index;
    private String item;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_layout);

        saveBtn = (Button) findViewById(R.id.save_button);
        imgBtn = (Button) findViewById(R.id.image_btn);
        editTitle = (EditText) findViewById(R.id.task_title);
        editTask = (EditText) findViewById(R.id.task_text);
        spinner = (Spinner) findViewById(R.id.select_category);
        categoryText = (TextView) findViewById(R.id.category_text);
        dueDate = (TextView) findViewById(R.id.pick_date);
        dueTime = (TextView) findViewById(R.id.post_time);

        final List<String> categories = new ArrayList<>();
        categories.add("Personal");
        categories.add("Work");
        categories.add("Shopping");
        categories.add("Projects");
        categories.add("Travel");

        //Create adapter for Spinner object
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        //Drop down layout-style
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(catAdapter);

        //Spinner element
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // On selecting a spinner item
                item = spinner.getItemAtPosition(i).toString();

                // Showing selected spinner item
                Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        dueDate.setText(dateFormatter.format(calendar.getTime()));

        final Intent intent = getIntent();

        index = intent.getIntExtra("Index", -1);
    }

    public void showDatePicker(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void saveClicked(View v) {
        Intent intent = getIntent();
        intent.putExtra("Title", editTitle.getText().toString());
        intent.putExtra("Text", editTask.getText().toString());
        intent.putExtra("Time", dueTime.getText().toString());
        intent.putExtra("DueDate", dueDate.getText().toString());
        intent.putExtra("Category", item.toString());

        intent.putExtra("Index", index);
        setResult(RESULT_OK, intent);
        Log.d(TAG, "Added a Task");
        finish();
    }

}
