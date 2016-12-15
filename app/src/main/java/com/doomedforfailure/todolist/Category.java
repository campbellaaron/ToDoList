package com.doomedforfailure.todolist;

/**
 * Created by aaroncampbell on 12/14/16.
 */

public class Category implements Comparable<Category> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Category category) {
        return category.getName().compareTo(getName());
    }
}


