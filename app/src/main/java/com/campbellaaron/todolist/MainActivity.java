package com.campbellaaron.todolist;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Menu menu;
    private TaskArrayAdapter toDoArrayAdapter;
    public static ArrayList<Task> taskArrayList = new ArrayList<Task>();
    private ArrayList<Task> results = new ArrayList<>();
    List <Task> tasks = new ArrayList<>();
    private SharedPreferences notesPrefs;
    private Boolean searchOn;
    private SearchManager searchManager;
    public ListView taskListView;
    private Gson gson = new Gson();
    public static boolean tasksCompleted = true;
    String filename = "TaskList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.todolist);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);
        searchOn = false;
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);


        notesPrefs = getPreferences(Context.MODE_PRIVATE);

        setupNotes();

        Collections.sort(taskArrayList);

        taskListView = (ListView) findViewById(R.id.listView);
        taskListView.setTextFilterEnabled(true);
        toDoArrayAdapter = new TaskArrayAdapter(this, R.layout.task_todo, taskArrayList);
        taskListView.setAdapter(toDoArrayAdapter);
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
                        taskArrayList.remove(task);
                        toDoArrayAdapter.notifyDataSetChanged();
                        writeNotes();
                    }

                });
                alertBuilder.create().show();
                return true;
            }
        });

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = taskArrayList.get(position);
                if (task.getKey() == null) {
                    Intent intent = new Intent(MainActivity.this, AddEditTask.class);
                    intent.putExtra("Title", task.getTitle());
                    intent.putExtra("Time", task.getDueTime());
                    intent.putExtra("Text", task.getTaskText());
                    intent.putExtra("Date", task.getFormattedDate());
                    intent.putExtra("Category", task.getCategory());
                    intent.putExtra("Image", task.getImage());
                    intent.putExtra("Index", position);
                    toDoArrayAdapter.remove(taskArrayList.get(position));
                    toDoArrayAdapter.notifyDataSetChanged();
                    startActivityForResult(intent, 1);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int index = data.getIntExtra("Index", -1);
            Task task = new Task(data.getStringExtra("Title"),
                    data.getStringExtra("Text"), DatePickerFragment.formattedDate, data.getStringExtra("Category"), data.getStringExtra("Time"), new Date());

            if (index == -1) {
                taskArrayList.add(taskArrayList.size(), task);
                writeNotes();
            }
            else {
                taskArrayList.set(index, task);
            }
            Collections.sort(taskArrayList);
            toDoArrayAdapter.updateAdapter(taskArrayList);

        }
    }

    private void setupNotes() {
        Log.d(TAG, "Made it to setup");
        taskArrayList = new ArrayList<>();

        File filesDir = this.getFilesDir();
        File taskFile = new File(filesDir + File.separator + filename);
        if (taskFile.exists()) {
            readNotes(taskFile);

            for (Task task : tasks) {
                taskArrayList.add(task);
            }
        } else {
            writeNotes();
        }
    }

    private void readNotes(File taskFile) {
        FileInputStream inputStream = null;
        String todosText = "";
        try {
            inputStream = openFileInput(taskFile.getName());
            byte[] input = new byte[inputStream.available()];
            while (inputStream.read(input) != -1) {
            }
            todosText = new String(input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Task[] todoList = gson.fromJson(todosText, Task[].class);
            tasks = Arrays.asList(todoList);
        }
    }

    private void writeNotes() {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);

            String json = gson.toJson(taskArrayList);
            byte[] bytes = json.getBytes();
            outputStream.write(bytes);

            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception ignored) {
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
                startActivityForResult(intent, 1);
                return true;
            case R.id.menuSearch:
                final SearchView searchView = (SearchView)item.getActionView();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (TextUtils.isEmpty(searchView.getQuery())) {
                            taskListView.clearTextFilter();

                            toDoArrayAdapter = new TaskArrayAdapter(MainActivity.this, R.layout.task_todo, taskArrayList);
                            taskListView.setAdapter(toDoArrayAdapter);
                            toDoArrayAdapter.updateAdapter(taskArrayList);
                            toDoArrayAdapter.notifyDataSetChanged();
                            results = new ArrayList<>();
                            searchOn = false;
                        } else {
                            results = new ArrayList<>();
                            taskListView = (ListView) findViewById(R.id.listView);
                            searchOn = true;

                            for (Task task : taskArrayList){
                                if (task.getTitle().toLowerCase().contains(searchView.getQuery().toString().toLowerCase())) {
                                    results.add(results.size(), task);
                                    toDoArrayAdapter = new TaskArrayAdapter(MainActivity.this, R.layout.task_todo, results);
                                    taskListView.setAdapter(toDoArrayAdapter);
                                    toDoArrayAdapter.updateAdapter(results);
                                    toDoArrayAdapter.notifyDataSetChanged();
                                }
                            }

                            if (results.size() == 0) {
                                results.add(0, new Task("No Results", null, null, "No Results", null, null));
                                toDoArrayAdapter = new TaskArrayAdapter(MainActivity.this, R.layout.task_todo, results);
                                taskListView.setAdapter(toDoArrayAdapter);
                                toDoArrayAdapter.updateAdapter(results);
                                toDoArrayAdapter.notifyDataSetChanged();
                            }
                        }

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (TextUtils.isEmpty(searchView.getQuery())) {
                            taskListView.clearTextFilter();

                            toDoArrayAdapter = new TaskArrayAdapter(MainActivity.this, R.layout.task_todo, taskArrayList);
                            taskListView.setAdapter(toDoArrayAdapter);
                            toDoArrayAdapter.updateAdapter(taskArrayList);
                            toDoArrayAdapter.notifyDataSetChanged();
                            results = new ArrayList<>();
                            searchOn = false;
                        } else {
                            taskListView.setFilterText(newText.toString());
                            results = new ArrayList<>();
                            taskListView = (ListView) findViewById(R.id.listView);
                            searchOn = true;

                            for (Task task : taskArrayList){
                                if (task.getTitle().toLowerCase().contains(searchView.getQuery().toString().toLowerCase())) {
                                    results.add(results.size(), task);
                                    toDoArrayAdapter = new TaskArrayAdapter(MainActivity.this, R.layout.task_todo, results);
                                    taskListView.setAdapter(toDoArrayAdapter);
                                    toDoArrayAdapter.updateAdapter(results);
                                    toDoArrayAdapter.notifyDataSetChanged();
                                }
                            }

                            if (results.size() == 0) {
                                results.add(0, new Task("No Results", null, null, "No Results", null, null));
                                toDoArrayAdapter = new TaskArrayAdapter(MainActivity.this, R.layout.task_todo, results);
                                taskListView.setAdapter(toDoArrayAdapter);
                                toDoArrayAdapter.updateAdapter(results);
                                toDoArrayAdapter.notifyDataSetChanged();
                            }
                        }

                        return true;
                    }
                });
                ComponentName componentName = new ComponentName(getApplicationContext(), MainActivity.class);
                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
                break;
            case R.id.toggleIt:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
