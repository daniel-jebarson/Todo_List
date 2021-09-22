package com.example.todolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView taskrecyclerview;
    private FloatingActionButton floatingActionButton;
    private taskdataAdapter taskdataAdapter;
    private List<taskData_Class> list;
    private database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        db=new database(this);
        db.openDatabase();
        list=new ArrayList<>();
        taskrecyclerview=findViewById(R.id.taskrecyclerview);
        taskrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        taskdataAdapter=new taskdataAdapter(db,this);
        taskrecyclerview.setAdapter(taskdataAdapter);

        floatingActionButton=findViewById(R.id.floater);
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
        taskData_Class taskData_class=new taskData_Class();
        taskData_class.setTask("This is the sample task");
        taskData_class.setStatus(0);
        taskData_class.setId(1);
        taskData_class.setDate("no date fixed");
        taskData_class.setWork("No work declared");
        list.add(taskData_class);
        taskdataAdapter.setTasks(list);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        list=db.getAllTasks();
        Collections.reverse(list);
        taskdataAdapter.setTasks(list);
        taskdataAdapter.notifyDataSetChanged();
    }
}