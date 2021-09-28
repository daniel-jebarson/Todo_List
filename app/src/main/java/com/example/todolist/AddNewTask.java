package com.example.todolist;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private static int n=0;
//    ArrayList<String> id_derived=new ArrayList<String>();
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private TextView date_picker;
    private TextView time_picker;
    private TextView notify_date_picker;
    private TextView notify_picker;
    private TextView task_description;
    private static ArrayList<PendingIntent> intentArray = new ArrayList<>();
    TimePickerDialog timePickerDialog;
    DatePickerDialog.OnDateSetListener setListener;
    private database db;

    public static AddNewTask newInstance() {
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
        task_description = requireView().findViewById(R.id.task_description);
        date_picker = requireView().findViewById(R.id.date_picker);
        time_picker = requireView().findViewById(R.id.time_picker);
        notify_date_picker = requireView().findViewById(R.id.notify_date_picker);
        notify_picker = requireView().findViewById(R.id.notify_picker);
        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            //doubt
            String date = bundle.getString("date");
            date_picker.setText(date);
            String time = bundle.getString("time");
            time_picker.setText(time);
            String notify_date = bundle.getString("notify_date");
            notify_date_picker.setText(notify_date);
            String notify = bundle.getString("notify");
            notify_picker.setText(notify);
            String work = bundle.getString("work");
            task_description.setText(work);
            assert task != null;
            assert date != null;
            assert work != null;
            assert time != null;
            assert notify != null;
            assert notify_date != null;
            if (task.length() > 0 && date.length() > 0 && work.length() > 0 && time.length() > 0 && notify.length() > 0 && notify_date.length() > 0)
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
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_primary_dark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        date_picker.setText(date);
                    }
                }, year, month, day
                );
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String am_pm;
                        if (hour > 12) {
                            am_pm = "pm";
                            hour = hour - 12;
                        } else if (hour == 0) {
                            am_pm = "am";
                            hour = hour + 12;
                        } else if (hour == 12) {
                            am_pm = "pm";
                        } else {
                            am_pm = "am";
                        }

                        String time = hour + ":" + minute + " " + am_pm;
                        time_picker.setText(time);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        notify_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        notify_date_picker.setText(date);
                    }
                }, year, month, day
                );
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        notify_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String am_pm;
                        if (hour > 12) {
                            am_pm = "pm";
                            hour = hour - 12;
                        } else if (hour == 0) {
                            am_pm = "am";
                            hour = hour + 12;
                        } else if (hour == 12) {
                            am_pm = "pm";
                        } else {
                            am_pm = "am";
                        }
                        String time = hour + ":" + minute + " " + am_pm;
                        notify_picker.setText(time);
                    }
                }, 0, 0, false);
                timePickerDialog.show();


            }
        });


        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                
                String text = newTaskText.getText().toString();
                String date = date_picker.getText().toString();
                String time = time_picker.getText().toString();
                String notify_date = notify_date_picker.getText().toString();
                String notify = notify_picker.getText().toString();
                String work = task_description.getText().toString();
                int date_len = date.length();
                int time_len = time.length();
                int notify_date_len = notify_date.length();
                int notify_len = notify.length();
                int work_len=work.length();
                if (text.length() == 0)
                    Toast.makeText(getContext(), "You didn't entered the title", Toast.LENGTH_SHORT).show();
                else if((date_len==0 && time_len==0 && notify_date_len==0 &&notify_len==0)||(date_len>0 && time_len>0 && notify_date_len==0 && notify_len==0) || (date_len==0 && time_len==0 && notify_len>0 && notify_date_len>0) || (date_len>0 && time_len>0 && notify_date_len>0 && notify_len>0)) {
                    if ((date_len > 0 && time_len > 0 && notify_date_len == 0 && notify_len == 0) && return_millies_current(date + " " + time) < 0)
                        Toast.makeText(getContext(), "Enter proper due date and time", Toast.LENGTH_SHORT).show();
                    else if ((date_len == 0 && time_len == 0 && notify_len > 0 && notify_date_len > 0) && return_millies_current(notify_date + " " + notify) < 0)
                        Toast.makeText(getContext(), "Enter proper notification date and time", Toast.LENGTH_SHORT).show();
                    else if ((date_len > 0 && time_len > 0 && notify_date_len > 0 && notify_len > 0) && (return_millies((notify_date + " " + notify), (date + " " + time)) < 0))
                        Toast.makeText(getContext(), "Check your due date and notify date properly", Toast.LENGTH_SHORT).show();
                    else {

                        //working
                        if(notify_len>0 && notify_date_len>0) {
                            createNotificationChannel();
                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                            long timeButtonClick = System.currentTimeMillis();
//                            ArrayList<PendingIntent> intentArray = new ArrayList<>();
                            String works;
                            if(work_len==0)
                                works="No work specified";
                            else
                                works=work;
                            Intent intent = new Intent(getContext(), RemainderBroadcast.class);
                            intent.putExtra("title",text);
                            intent.putExtra("work",works);
                            // Loop counter `i` is used as a `requestCode`
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), n, intent, 0);
                            // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
//                            alarmManager.set(AlarmManager.RTC_WAKEUP,
//                                    System.currentTimeMillis()+10000 * i,
//                                    pendingIntent);
                            long tenSecondsInMillis = return_millies_current(notify_date + " " + notify);
                            // tenSecondsInMillis=This is the time of the notification to be fired
                            alarmManager.set(AlarmManager.RTC_WAKEUP, timeButtonClick + tenSecondsInMillis, pendingIntent);
                            intentArray.add(pendingIntent);
                            n = n + 1;
//                        System.out.println(n);
                            Toast.makeText(getContext(), "Remainder set successfully!", Toast.LENGTH_SHORT).show();
                        }




                        if (finalIsUpdate) {

                            db.updateTask(bundle.getInt("id"), text);
//                            Log.d("sam doubtr", String.valueOf(bundle.getInt("id")));

                            db.updateDate(bundle.getInt("id"), date);
                            db.updateTime(bundle.getInt("id"), time);
                            db.updateNotify_date(bundle.getInt("id"), notify_date);
                            db.updateNotify(bundle.getInt("id"), notify);
                            db.updateWork(bundle.getInt("id"), work);

                        } else {
                            taskData_Class task = new taskData_Class();


                            task.setTask(text);
                           // Log.d("sample programming", String.valueOf((task.getId())));
                            task.setDate(date);
                            task.setTime(time);
                            task.setNotify_date(notify_date);
                            task.setNotify(notify);
                            task.setWork(work);
                            task.setStatus(0);
                            db.insertTask(task);
                           // error
                            // Log.d("sam skills", String.valueOf(db.LASTTASK.getId()));
                            //Log.d("cheking idea", String.valueOf(db.id_taken));
                            System.out.println(db.LASTTASK);
                        }
                        dismiss();
                    }
                }
                else
                {
                    if(date_len>0 && time_len>0)
                        Toast.makeText(getContext(), "Check your notify date and time properly", Toast.LENGTH_SHORT).show();
                    else if(notify_len > 0 && notify_date_len > 0)
                        Toast.makeText(getContext(), "Check your due date and time properly", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Check whether you entered correct value or not", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public long return_millies(String startDateString, String endDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy h:m a");
        ZoneId zone = ZoneId.systemDefault();
//        DateFormat dateFormat=new SimpleDateFormat("d/M/yyyy h:m a");
//        String startDateString=dateFormat.format(new Date()).toString();
//        String endDateString = "24/9/2021 5:44 pm";
        ZonedDateTime start = LocalDateTime.parse(startDateString, formatter).atZone(zone);
        ZonedDateTime end = LocalDateTime.parse(endDateString, formatter).atZone(zone);
        return (ChronoUnit.SECONDS.between(start, end) * 1000);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long return_millies_current(String endDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy h:m a");
        ZoneId zone = ZoneId.systemDefault();
        DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy h:m a");
        String startDateString = dateFormat.format(new Date()).toString();
//        String endDateString = "24/9/2021 5:44 pm";
        ZonedDateTime start = LocalDateTime.parse(startDateString, formatter).atZone(zone);
        ZonedDateTime end = LocalDateTime.parse(endDateString, formatter).atZone(zone);
        long diffSeconds = ChronoUnit.SECONDS.between(start, end);
        System.out.println(diffSeconds);
        return (diffSeconds * 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            CharSequence name = "This is Name";
            String description = "This is description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notify", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener)
            ((DialogCloseListener) activity).handleDialogClose(dialog);
    }
}
