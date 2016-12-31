package com.campbellaaron.todolist;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by aaroncampbell on 12/13/16.
 */

public class Task implements Comparable<Task> {
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String taskText;
    @SerializedName("date")
    private Date dateModified;
    @SerializedName("dueDate")
    private String formattedDate;
    @SerializedName("category")
    private String category;
    @SerializedName("dueTime")
    private String dueTime;
    @SerializedName("Complete")
    private Boolean complete;
    @SerializedName("imgSrc")
    private String imgSrc;
    @SerializedName("key")
    private String key;

    public Task(String title, String taskText, String formattedDate, String category, String dueTime, Date dateModified) {
        this.title = title;
        this.taskText = taskText;
        this.dateModified = dateModified;
        this.formattedDate = formattedDate;
        this.category = category;
        this.dueTime = dueTime;
        this.imgSrc = imgSrc;
        this.key = key;
    }

    public Task(String category) {
        this.category = category;
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
        return getDateModified().compareTo(another.getDateModified());
    }
}
