package com.asura.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.task_input.*

class TaskInputDialog: DialogFragment() {

    interface AddTaskListener{
        fun addTask(task: Task)
    }

    private lateinit var addTaskListener: AddTaskListener

    private fun TaskInputDialog(){

    }

    companion object {
        fun newInstance(addTaskListener: AddTaskListener): TaskInputDialog {
            val dialogFragment = TaskInputDialog()
            dialogFragment.addTaskListener = addTaskListener
            return dialogFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.ThemeOverlay_Material_Dialog_Alert)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.task_input,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTask.setOnClickListener { onAddClick() }
    }

    private fun onAddClick(){
        val taskName = taskNameInput.text.toString()
        val taskDescription = taskDescriptionInput.text.toString()
        if(taskName!=""){
            addTaskListener.addTask(Task(name = taskName,description = taskDescription))
        }
    }
}