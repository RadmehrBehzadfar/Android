package com.example.task_radmehr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var editTextTaskName: EditText
    private lateinit var switchPriority: Switch
    private lateinit var buttonAddTask: Button
    private lateinit var buttonUpdateTask: Button
    private lateinit var recyclerViewTasks: RecyclerView

    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()

    private var selectedTaskIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTaskName = findViewById(R.id.editTextTaskName)
        switchPriority = findViewById(R.id.switchPriority)
        buttonAddTask = findViewById(R.id.buttonAddTask)
        buttonUpdateTask = findViewById(R.id.buttonUpdateTask)
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks)

        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(
            tasks,
            onEditClick = { position -> editTask(position) },
            onDeleteClick = { position -> deleteTask(position) }
        )
        recyclerViewTasks.adapter = taskAdapter

        buttonAddTask.setOnClickListener {
            val taskName = editTextTaskName.text.toString().trim()
            if (taskName.isNotEmpty()) {
                tasks.add(Task(taskName, switchPriority.isChecked))
                taskAdapter.notifyDataSetChanged()

                editTextTaskName.text.clear()
                switchPriority.isChecked = false
            } else {
                Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show()
            }
        }

        buttonUpdateTask.setOnClickListener {
            if (selectedTaskIndex != -1) {
                val updatedTaskName = editTextTaskName.text.toString().trim()
                if (updatedTaskName.isNotEmpty()) {
                    tasks[selectedTaskIndex].name = updatedTaskName
                    tasks[selectedTaskIndex].isHighPriority = switchPriority.isChecked
                    taskAdapter.notifyDataSetChanged()
                    resetForm()
                } else {
                    Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun editTask(position: Int) {
        selectedTaskIndex = position
        editTextTaskName.setText(tasks[position].name)
        switchPriority.isChecked = tasks[position].isHighPriority

        buttonAddTask.isEnabled = false
        buttonUpdateTask.isEnabled = true
    }

    private fun deleteTask(position: Int) {
        tasks.removeAt(position)
        taskAdapter.notifyDataSetChanged()
        resetForm()
    }

    private fun resetForm() {
        editTextTaskName.text.clear()
        switchPriority.isChecked = false
        selectedTaskIndex = -1
        buttonAddTask.isEnabled = true
        buttonUpdateTask.isEnabled = false
    }
}