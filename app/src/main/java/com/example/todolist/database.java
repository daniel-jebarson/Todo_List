package com.example.todolist;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class database extends SQLiteOpenHelper {
    ArrayList<String> id = new ArrayList<String>();
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    public int LASTTASK;
    public int id_taken;
    public int need_id=1;
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String NOTIFY_DATE = "notify_date";
    private static final String NOTIFY = "notify";
    private static final String WORK = "work";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER, " + DATE + " TEXT, " + TIME + " TEXT, " + NOTIFY_DATE + " TEXT, " + NOTIFY + " TEXT, " + WORK + " TEXT)";

    private SQLiteDatabase db;

    public database(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(taskData_Class task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(DATE, task.getDate());
        cv.put(TIME, task.getTime());
        cv.put(NOTIFY_DATE, task.getNotify_date());
        cv.put(NOTIFY, task.getNotify());
        cv.put(WORK, task.getWork());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<taskData_Class> getAllTasks() {
        List<taskData_Class> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        //trying
                        taskData_Class task = new taskData_Class();
                        // Log.d("checking id_number", String.valueOf(cur.getInt(cur.getColumnIndex(ID))));
                        id.add(cur.getString(cur.getColumnIndex(ID)));
                       //works Log.d("checking id_number", String.valueOf(id));



                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        id_taken=cur.getInt(cur.getColumnIndex(ID));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setTime(cur.getString(cur.getColumnIndex(TIME)));
                        task.setNotify_date(cur.getString(cur.getColumnIndex(NOTIFY_DATE)));
                        task.setNotify(cur.getString(cur.getColumnIndex(NOTIFY)));
                        task.setWork(cur.getString(cur.getColumnIndex(WORK)));
                        taskList.add(task);
                        LASTTASK=task.getId();
                        //System.out.println(LASTTASK);


                    }
                    while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateTask(int id, String task) {

        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateDate(int id, String date) {
        ContentValues cv = new ContentValues();
        cv.put(DATE, date);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateTime(int id, String time) {
        ContentValues cv = new ContentValues();
        cv.put(TIME, time);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateNotify_date(int id, String notify_date) {
        ContentValues cv = new ContentValues();
        cv.put(NOTIFY_DATE, notify_date);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateNotify(int id, String notify) {
        ContentValues cv = new ContentValues();
        cv.put(NOTIFY, notify);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateWork(int id, String work) {
        ContentValues cv = new ContentValues();
        cv.put(WORK, work);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "= ?", new String[]{String.valueOf(id)});
    }

}