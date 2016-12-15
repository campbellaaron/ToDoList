package com.doomedforfailure.todolist.db;

import android.provider.BaseColumns;

/**
 * Created by aaroncampbell on 12/14/16.
 */

public class TaskContract {
    public static final String DB_NAME = "com.doomedforfailure.todolist.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "Tasks";

        public static final String COL_TASK_TITLE = "Title";

        public static final String COL_TASK_TEXT = "Text";

        public static final String COL_TASK_DATE = "Date";

        public static final String COL_TASK_CATEGORY = "Category";
    }
}
