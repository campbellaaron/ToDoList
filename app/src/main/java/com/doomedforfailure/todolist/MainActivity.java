package com.doomedforfailure.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Menu menu;
    private TaskArrayAdapter toDoArrayAdapter;
    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private SharedPreferences notesPrefs;
    private ListView taskListView;
    private Gson gson = new Gson();
    private static int RESULT_LOAD_IMG = 1;
    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.todolist);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);

        notesPrefs = getPreferences(Context.MODE_PRIVATE);

        setupNotes();

        Collections.sort(taskArrayList);

        taskListView = (ListView) findViewById(R.id.listView);
        toDoArrayAdapter = new TaskArrayAdapter(this, R.layout.task_todo, taskArrayList);
        taskListView.setAdapter(toDoArrayAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = taskArrayList.get(position);

                Intent intent = new Intent(MainActivity.this, AddEditTask.class);
                intent.putExtra("Title", task.getTitle());
                intent.putExtra("Time", task.getDueTime());
                intent.putExtra("Text", task.getTaskText());
                intent.putExtra("DueDate", task.getFormattedDate());
                intent.putExtra("Category", task.getCategory());
                intent.putExtra("Index", position);

                startActivityForResult(intent, 1);

            }
        });
        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle("Delete");
                alertBuilder.setMessage("You sure?");
                alertBuilder.setNegativeButton("Cancel", null);
                alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Task task = taskArrayList.get(position);
                        deleteFile("##" + task.getTitle());
                        taskArrayList.remove(position);
                        toDoArrayAdapter.updateAdapter(taskArrayList);
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = openFileOutput(task.getKey(), Context.MODE_PRIVATE);
                            outputStream.write(gson.toJson(task).getBytes());
                            outputStream.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                outputStream.close();
                            } catch (Exception ignored) {}
                        }
                    }

                });
                alertBuilder.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int index = data.getIntExtra("Index", -1);
            Task task = new Task(data.getStringExtra("Title"),
                    data.getStringExtra("Text"), DatePickerFragment.formattedDate, data.getStringExtra("Category"), data.getStringExtra("Time"));

            if (index == -1) {
                taskArrayList.add(task);
            }
            else {
                taskArrayList.set(index, task);
            }

            FileOutputStream outputStream = null;
            try {
                outputStream = openFileOutput(task.getTitle(), Context.MODE_PRIVATE);
                outputStream.write(gson.toJson(task).getBytes());
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                } catch (Exception ignored) {}
            }

            Collections.sort(taskArrayList);
            toDoArrayAdapter.updateAdapter(taskArrayList);

        }
    }

    private void setupNotes() {
        Log.d(TAG, "Made it to setup");
        if (notesPrefs.getBoolean("firstRun", true)) {
            SharedPreferences.Editor editor = notesPrefs.edit();
            editor.apply();

            Task task1 = new Task("Note 1", "This is a note", "Due Date", "Category", "Time");
            taskArrayList.add(task1);
            Log.d(TAG, "Just posted first task");

            for (Task task : taskArrayList) {
                FileOutputStream outputStream = null;
                try {
                    outputStream = openFileOutput(task.getTitle(), Context.MODE_PRIVATE);
                    outputStream.write(gson.toJson(task).getBytes());
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.close();
                    } catch (Exception ignored) {}
                }
            }
        } else {
            File[] filesDir = this.getFilesDir().listFiles();
            for (File file : filesDir) {
                FileInputStream inputStream = null;
                String title = file.getName();
                String text = "";
                try {
                    inputStream = openFileInput(title);
                    byte[] input = new byte[inputStream.available()];
                    while (inputStream.read(input) != -1) {}
                    text += new String(input);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Type collectionType = new TypeToken<List<Task>>(){}.getType();
                    List<Task> catList = gson.fromJson(text, collectionType);
                    taskArrayList = new ArrayList<>(catList);
                    try {
                        inputStream.close();
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Log.d(TAG, "Add a new Task");
                Intent intent = new Intent(MainActivity.this, AddEditTask.class);
                intent.putExtra("Title", "");
                intent.putExtra("Text", "");
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
