package com.example.jack.pubkid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Jack on 1/5/2017.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    String[] tasks;
    Context context;
    View view;
    ViewHolder viewHolder;
    static MainActivity mainActivity;

    RecyclerViewAdapter(Context context, String[] tasks, MainActivity mainActivity){
        this.tasks = tasks;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        CheckBox checkBox;

        ViewHolder(View v){
            super(v);
            textView = (TextView) v.findViewById(R.id.task_view);
            checkBox = (CheckBox) v.findViewById(R.id.taskCheckBox);

            this.checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mainActivity.publishCheck(this.getAdapterPosition());
            this.checkBox.setClickable(false);
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        this.view = LayoutInflater.from(context).inflate(R.layout.task_item_view,parent,false);
        this.viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.textView.setText(tasks[position]);
    }

    @Override
    public int getItemCount(){
        return tasks.length;
    }
}
