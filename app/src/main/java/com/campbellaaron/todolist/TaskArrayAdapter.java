package com.campbellaaron.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by aaroncampbell on 12/13/16.
 */

public class TaskArrayAdapter extends ArrayAdapter<Task> {
    private int resource;
    private ArrayList<Task> tasks;
    private LayoutInflater inflater;
    private SimpleDateFormat formatter;

    public TaskArrayAdapter(Context context, int resource, ArrayList<Task> objects) {

        super(context, resource, objects);
        this.resource = resource;
        this.tasks = objects;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        formatter = new SimpleDateFormat(context.getString(R.string.modified_date));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);

        TextView noteTitle = (TextView)view.findViewById(R.id.task_title);
        TextView noteText = (TextView)view.findViewById(R.id.note_text);
        TextView noteTime = (TextView)view.findViewById(R.id.task_time);
        TextView dueDate = (TextView)view.findViewById(R.id.view_date);
        TextView category = (TextView)view.findViewById(R.id.category_text);
        TextView modifiedDate = (TextView) view.findViewById(R.id.modifiedDate);
        ImageView imageView = (ImageView) view.findViewById(R.id.task_image);
        CheckBox isComplete = (CheckBox) view.findViewById(R.id.taskComplete);

        final Task task = tasks.get(position);

        noteTitle.setText(task.getTitle());
        noteText.setText(task.getTaskText());
        dueDate.setText(task.getFormattedDate());
        noteTime.setText(task.getDueTime());
        category.setText(task.getCategory());
        if (task.getImage() != null) {
            imageView.setImageBitmap(task.getImage());
        }
        modifiedDate.setText("Modified: " + formatter.format(task.getDateModified()));

        isComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    task.setComplete(true);
                } else {
                    task.setComplete(false);
                }
                Log.d(task.getTitle().toString(), task.getComplete().toString());

            }

        });
        return view;
    }

    public void updateAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
        super.notifyDataSetChanged();
    }
}
