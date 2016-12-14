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
    private String category;
    private String dueTime;
    private String imgSrc;
    private String key;

    public Task(String title, String taskText, Date dateModified, String formattedDate, String category, String dueTime, String imgSrc, String key) {
        this.title = title;
        this.taskText = taskText;
        this.dateModified = dateModified;
        this.formattedDate = formattedDate;
        this.category = category;
        this.dueTime = dueTime;
        this.imgSrc = imgSrc;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(Task another) {
        return another.getFormattedDate().compareTo(getFormattedDate());
    }
}
