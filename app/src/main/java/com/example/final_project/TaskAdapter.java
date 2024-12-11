package com.example.final_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;
    private final DatabaseReference dbRef;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
        this.dbRef = FirebaseDatabase.getInstance("https://final-project-1d01f-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("tasks");
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTitle.setText(task.getTaskTitle());
        holder.cbTaskCompleted.setChecked(task.isDone());

        holder.cbTaskCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            if (task.getTaskId() != null) {
                dbRef.child(task.getTaskId()).child("isCompleted").setValue(isChecked);
            }
        });

        holder.btnDeleteTask.setOnClickListener(v -> {
            if (task.getTaskId() != null) {
                dbRef.child(task.getTaskId()).removeValue().addOnSuccessListener(aVoid -> {
                    taskList.remove(position);
                    notifyItemRemoved(position);
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        CheckBox cbTaskCompleted;
        Button btnDeleteTask;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.tasktitle);
            cbTaskCompleted = itemView.findViewById(R.id.cbtaskcompleted);
            btnDeleteTask = itemView.findViewById(R.id.btndeletetask);
        }
    }
}
