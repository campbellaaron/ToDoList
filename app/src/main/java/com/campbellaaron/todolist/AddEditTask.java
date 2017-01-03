package com.campbellaaron.todolist;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
    private static final String TAG = "~ToDoList~";
    private EditText editTitle;
    private EditText editTask;
    private ImageView taskImage;
    private CheckBox isComplete;
    private Button imgBtn;
    private TextView dueTime;
    private TextView dueDate;
    protected Spinner spinner;
    private int index;
    private String item;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    public Bundle savedInstanceState;
    private EditText addCatText;
    private Button addCatBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_layout);
        this.savedInstanceState = savedInstanceState;
        final Intent intent = getIntent();

        imgBtn = (Button) findViewById(R.id.image_btn);
        editTitle = (EditText) findViewById(R.id.task_title);
        editTask = (EditText) findViewById(R.id.task_text);
        spinner = (Spinner) findViewById(R.id.select_category);
        dueDate = (TextView) findViewById(R.id.pick_date);
        dueTime = (TextView) findViewById(R.id.post_time);
        addCatText = (EditText) findViewById(R.id.add_category);
        addCatBtn = (Button) findViewById(R.id.catBtn);
        isComplete = (CheckBox) findViewById(R.id.checkBox);

        final List<String> categories = new ArrayList<>();
        categories.add("Personal");
        categories.add("Work");
        categories.add("Shopping");
        categories.add("Add New...");

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
                if (spinner.getSelectedItem().toString().equals("Add New...")) {
                    addCatText.setVisibility(View.VISIBLE);
                    addCatBtn.setVisibility(View.VISIBLE);
                    addCatBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (addCatText.getText().toString() == null) {
                                    Toast.makeText(AddEditTask.this, "You must cannot leave this blank!", Toast.LENGTH_SHORT).show();
                                } else {
                                    categories.set(0, addCatText.getText().toString());
                                    addCatText.setText("");
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(addCatText.getWindowToken(), 0);
                                    addCatBtn.setVisibility(View.INVISIBLE);
                                    addCatText.setVisibility(View.INVISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    item = spinner.getItemAtPosition(i).toString();
                    addCatText.setVisibility(View.INVISIBLE);
                    addCatBtn.setVisibility(View.INVISIBLE);

                    // Showing selected spinner item
                    Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        index = intent.getIntExtra("Index", -1);

        editTitle.setText(intent.getStringExtra("Title"));
        editTask.setText(intent.getStringExtra("Text"));
        dueDate.setText(intent.getStringExtra("Date"));
        dueTime.setText(intent.getStringExtra("Time"));
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

        Task task = new Task();
        String title = intent.getStringExtra("Title");
        String text = intent.getStringExtra("Text");
        String time = intent.getStringExtra("Time");
        String date = intent.getStringExtra("Date");
        String category = intent.getStringExtra("Category");
        if (category != null) {
            int spinnerPosition = catAdapter.getPosition(category);
            spinner.setSelection(spinnerPosition);
        }

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
        intent.putExtra("Complete", isComplete.isChecked());
        intent.putExtra("Category", item.toString());
        intent.putExtra("Date", dueDate.getText().toString());
        intent.putExtra("Time", dueTime.getText().toString());

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
            Task task = new Task();

            taskImage = (ImageView) findViewById(imageView);
            Bitmap bitImage = BitmapFactory.decodeFile(picturePath);
            taskImage.setImageBitmap(bitImage);
            task.setImage(bitImage);
            Log.d(TAG, task.getImage().toString());
            
        }
    }
}
