package com.asura.todo


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_item.view.*

class TaskAdapter(
    private val taskList: List<Task>,
    private val taskItemClickListener: TaskItemClickListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface TaskItemClickListener {
        fun onItemClick(task: Task)
        fun delete(taskID: Int)
        fun updateTaskCompleteStatus(taskId: Int, completFlag: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.taskName.text = taskList[position].getName()
        holder.taskDescription.text = taskList[position].getDescription()
        holder.taskCompleteStatus.isChecked = taskList[position].getCompleteStatus()

        holder.taskDelete.setOnClickListener {
            taskItemClickListener.delete(taskList[position].getId())
        }
        holder.taskCompleteStatus.setOnClickListener {

            taskItemClickListener.updateTaskCompleteStatus(
                taskList[position].getId(),
                holder.taskCompleteStatus.isChecked
            )
        }
        holder.itemView.setOnClickListener {

        }
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.taskName
        val taskDescription: TextView = view.taskDescription
        val taskDelete: ImageView = view.deleteTask
        val taskCompleteStatus: CheckBox = view.completeCheck
    }
}