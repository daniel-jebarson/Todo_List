package com.example.todolist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class taskdataAdapter extends RecyclerView.Adapter<taskdataAdapter.ViewHolder> {
    private List<taskData_Class> list;
    private MainActivity mainActivity;
    private database db;
    private Context context;

    public taskdataAdapter(database db,MainActivity mainActivity) {
        this.db=db;
        this.mainActivity=mainActivity;
    }

//    public taskdataAdapter(MainActivity mainActivity) {
//
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item=LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);
        return new ViewHolder(item);
    }



//    public void setContext(Context context) {
//        this.context = context;
//    }
    public Context getContext()
    {
        return mainActivity;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checking_box;
        TextView date_display;
        TextView time_display;
        TextView my_work_description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checking_box=itemView.findViewById(R.id.checking_box);
            date_display=itemView.findViewById(R.id.date_display);
            time_display=itemView.findViewById(R.id.time_display);
            my_work_description=itemView.findViewById(R.id.my_work_description);

        }
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();
        taskData_Class item=list.get(position);
        holder.checking_box.setText(item.getTask());
        holder.checking_box.setChecked(toBoollean(item.getStatus()));
        holder.date_display.setText(item.getDate());
        holder.time_display.setText(item.getTime());
        holder.my_work_description.setText(item.getWork());
        holder.checking_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    db.updateStatus(item.getId(),1);
                }
                else
                {
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

    private boolean toBoollean(int status) {
        if(status==0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public void setTasks(List<taskData_Class> list){
        this.list=list;
        notifyDataSetChanged();
    }
    public void editItem(int position)
    {
       taskData_Class item=list.get(position);
        Bundle bundle=new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        bundle.putString("date",item.getDate());
        bundle.putString("time",item.getTime());
        bundle.putString("work",item.getWork());
        AddNewTask fragment =new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(),AddNewTask.TAG);
    }
    public void deleteItem(int position)
    {
        taskData_Class item=list.get(position);
        db.deleteTask(item.getId());
        list.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
