package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView taskrecyclerview;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton stopwatch;
    private taskdataAdapter taskdataAdapter;
    private List<taskData_Class> list;
    private database db;
    private TextView welcome;
    private FloatingActionButton mode;
    private MenuItem light_mode,dark_mode;
//added
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.DarkTheme);
        }
        else
        {
            setTheme(R.style.Apptheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        light_mode=findViewById(R.id.light_mode);
        dark_mode=findViewById(R.id.dark_mode);
//        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
//        {
//            dark_mode.setChecked(true);
//        }
        //WELCOMING
        welcome=findViewById(R.id.welcome);
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("a");
        String am_pm=simpleDateFormat.format(calendar.getTime());
        if(am_pm.equals("pm"))
            welcome.setText("GOOD EVENING");
        else if(am_pm.equals("am"))
            welcome.setText("GOOD MORNING");
        else
            welcome.setText("GOOD AFTERNOON");



        db=new database(this);
        db.openDatabase();
        list=new ArrayList<>();
        taskrecyclerview=findViewById(R.id.taskrecyclerview);
        taskrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        taskdataAdapter=new taskdataAdapter(db,this);
        taskrecyclerview.setAdapter(taskdataAdapter);

        floatingActionButton=findViewById(R.id.floater);
        stopwatch=findViewById(R.id.stopwatch);
        mode=findViewById(R.id.mode);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerItemTouchHelper(taskdataAdapter));
        itemTouchHelper.attachToRecyclerView(taskrecyclerview);

        list=db.getAllTasks();
        Collections.reverse(list);
        taskdataAdapter.setTasks(list);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);

            }
        });
        stopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddStopWatch.class);
                startActivity(intent);

            }
        });

        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(MainActivity.this,mode);
                popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.light_mode:
                            {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                Toast.makeText(getApplicationContext(), "Switched to light mode", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            case R.id.dark_mode:
                            {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                Toast.makeText(getApplicationContext(), "Switched to dark mode", Toast.LENGTH_SHORT).show();
                                reset();
                                return  true;
                            }
                            default:
                            {
//                                Toast.makeText(getApplicationContext(), "Nothing worked", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        }
                    }
                });
                popupMenu.show();
            }

            private void reset() {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //checking
//        taskData_Class taskData_class=new taskData_Class();
//        taskData_class.setTask("This is the sample task");
//        taskData_class.setStatus(0);
//        taskData_class.setId(1);
//        taskData_class.setDate("no date fixed");
//        taskData_class.setTime("No time fixed");
//        taskData_class.setNotify("Notify time not set");
//        taskData_class.setWork("No work declared");
//        list.add(taskData_class);
//        taskdataAdapter.setTasks(list);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        list=db.getAllTasks();
        Collections.reverse(list);
        taskdataAdapter.setTasks(list);
        taskdataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Confirm exit?")
                .setMessage("Are you sure?")
                .setIcon(R.drawable.ic_baseline_exit_to_app_24)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Cancel",null);
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }


}