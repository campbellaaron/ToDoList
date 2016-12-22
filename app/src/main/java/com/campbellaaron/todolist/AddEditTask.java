package com.campbellaaron.todolist;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.campbellaaron.todolist.R.id.imageView;

/**
 * Created by aaroncampbell on 12/13/16.
 */

public class AddEditTask extends AppCompatActivity {
    private static final int SELECTED_PICTURE = 1;
    private static final String TAG = "ToDoList";
    private EditText editTitle;
    private EditText editTask;
    private ImageView taskImage;
    private Button imgBtn;
    private TextView dueTime;
    private TextView dueDate;
    private TextView categoryText;
    protected Spinner spinner;
    TaskArrayAdapter adapter;
    private int index;
    private String item;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    public Bundle savedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_layout);
        this.savedInstanceState = savedInstanceState;

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

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        if (Build.VERSION.SDK_INT >= 23){
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }

        dueDate.setText(dateFormatter.format(calendar.getTime()));

        final Intent intent = getIntent();

        index = intent.getIntExtra("Index", -1);


        String title = intent.getStringExtra("Title");
        String text = intent.getStringExtra("Text");
        String time = intent.getStringExtra("Time");
        String date = intent.getStringExtra("Date");
        editTitle.setText(title);
        editTask.setText(text);
        dueTime.setText(time);
        dueDate.setText(date);

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



    private void addImage() {
        Intent image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(image, SELECTED_PICTURE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECTED_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri chosenImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(chosenImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            taskImage = (ImageView) findViewById(imageView);
            Bitmap bitImage = BitmapFactory.decodeFile(picturePath);
            taskImage.setImageBitmap(bitImage);
        }
    }
}
