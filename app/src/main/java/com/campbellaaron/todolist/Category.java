package com.campbellaaron.todolist;

import java.util.Date;

/**
 * Created by aaroncampbell on 12/14/16.
 */

public class Category implements Comparable<Category> {
    private String name;
    private Date modifiedDate;

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Category category) {
        return category.getModifiedDate().compareTo(getModifiedDate());
    }
}


