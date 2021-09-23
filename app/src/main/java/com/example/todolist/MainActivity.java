package com.example.todolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private taskdataAdapter taskdataAdapter;
    private List<taskData_Class> list;
    private database db;
    private TextView welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
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

        //checking
//        taskData_Class taskData_class=new taskData_Class();
//        taskData_class.setTask("This is the sample task");
//        taskData_class.setStatus(0);
//        taskData_class.setId(1);
//        taskData_class.setDate("no date fixed");
//        taskData_class.setTime("No time fixed");
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
}