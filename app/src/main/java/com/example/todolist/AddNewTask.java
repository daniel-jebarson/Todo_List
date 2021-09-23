package com.example.todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todolist.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import java.util.Calendar;
import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private TextView date_picker;
    private TextView time_picker;
    private TextView task_description;
    TimePickerDialog timePickerDialog;
    DatePickerDialog.OnDateSetListener setListener;
    private database db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = requireView().findViewById(R.id.Task_title);
        newTaskSaveButton = getView().findViewById(R.id.save);
        task_description=requireView().findViewById(R.id.task_description);
        date_picker=requireView().findViewById(R.id.date_picker);
        time_picker=requireView().findViewById(R.id.time_picker);
        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            //doubt
            String date = bundle.getString("date");
            date_picker.setText(date);
            String time = bundle.getString("time");
            time_picker.setText(time);
            String work = bundle.getString("work");
            task_description.setText(work);
            assert task != null;
            assert  date!=null;
            assert work!=null;
            assert  time!=null;
            if(task.length()>0 && date.length()>0 && work.length()>0 && time.length()>0 )
                newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_primary_dark));
        }

        db = new database(getActivity());
        db.openDatabase();

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_primary_dark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        ///code here



        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
       // int time=calendar.get(Calendar.HOUR);

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date=day+"/"+month+"/"+year;
                date_picker.setText(date);
            }
        };
        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String am_pm;
                        if(hour>12)
                        {
                            am_pm="PM";
                            hour=hour-12;
                        }
                        else if (hour==0)
                        {
                            am_pm="AM";
                            hour=hour+12;
                        }
                        else if(hour==12)
                        {
                            am_pm="PM";
                        }
                        else
                        {
                            am_pm="AM";
                        }

                        String time=hour + " : " +minute + " "+ am_pm;
                        time_picker.setText(time);
                    }
                },0,0,false);
                timePickerDialog.show();
            }
        });



        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                String date=date_picker.getText().toString();
                String time=time_picker.getText().toString();
                String work=task_description.getText().toString();

                if(finalIsUpdate){
                         db.updateTask(bundle.getInt("id"), text);
                        db.updateDate(bundle.getInt("id"), date);
                        db.updateTime(bundle.getInt("id"),time);
                        db.updateWork(bundle.getInt("id"), work);

                }
                else {
                   taskData_Class task = new taskData_Class();
                    task.setTask(text);
                    task.setDate(date);
                    task.setTime(time);
                    task.setWork(work);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}