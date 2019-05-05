package com.asura.todo

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.task_input.*

class TaskInputDialog: DialogFragment() {

    interface AddTaskListener{
        fun addTask()
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
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.ThemeOverlay_Material_Dialog_Alert)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.task_input,container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTask.setOnClickListener { addTaskListener.addTask() }
    }
}