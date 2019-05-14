package com.asura.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.task_input.*



class TaskInputDialog : DialogFragment() {

    private val create = 1
    private val edit = 2

    private var mode: Int = create
    private var task: Task? = null

    interface AddTaskListener {
        fun addTask(task: Task)
        fun editTask(taskId: Int, name: String, description: String)
    }

    private lateinit var addTaskListener: AddTaskListener

    private fun TaskInputDialog() {

    }

    companion object {
        fun newInstance(addTaskListener: AddTaskListener, mode:Int): TaskInputDialog {
            val dialogFragment = TaskInputDialog()
            dialogFragment.mode = mode
            dialogFragment.addTaskListener = addTaskListener
            return dialogFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.ThemeOverlay_Material_Dialog_Alert)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.task_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mode == create) {
            addTask.setOnClickListener { onAddClick() }
        }
        if (mode == edit) {
            taskName.setText(task!!.getName())
            taskDescription.setText(task!!.getDescription())
            addTask.text = getText(R.string.save)
            addTask.setOnClickListener { onEditClick() }
        }
    }

    private fun onAddClick() {
        val taskName = taskName.text.toString()
        val taskDescription = taskDescription.text.toString()
        if (taskName != "") {
            addTaskListener.addTask(Task(name = taskName, description = taskDescription))
        }
    }

    private fun onEditClick() {
        val taskName = taskName.text.toString()
        val taskDescription = taskDescription.text.toString()
        if (taskName != "") {
            addTaskListener.editTask(task!!.getId(), taskName, taskDescription)
        }
    }

    fun setTask(task: Task) {
        this.task = task
    }
}