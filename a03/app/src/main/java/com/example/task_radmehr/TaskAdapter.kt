package com.example.task_radmehr

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: List<Task>,
    private val onEditClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.textViewTask.text = task.name

        if (task.isHighPriority) {
            holder.textViewTask.setTextColor(Color.RED)
        } else {
            holder.textViewTask.setTextColor(Color.BLUE)
        }
        holder.buttonEditTask.setOnClickListener { onEditClick(position) }
        holder.buttonDeleteTask.setOnClickListener { onDeleteClick(position) }
    }

    override fun getItemCount(): Int = tasks.size

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTask: TextView = itemView.findViewById(R.id.textViewTask)
        val buttonEditTask: ImageView = itemView.findViewById(R.id.buttonEditTask)
        val buttonDeleteTask: ImageView = itemView.findViewById(R.id.buttonDeleteTask)
    }
}