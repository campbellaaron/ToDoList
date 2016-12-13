package com.doomedforfailure.todolist;

import java.util.Date;

/**
 * Created by aaroncampbell on 12/13/16.
 */

public class Task implements Comparable<Task> {
    private String title;
    private String taskText;
    private Date dateModified;
    private String formattedDate;

    @Override
    public int compareTo(Task task) {
        return 0;
    }
}
