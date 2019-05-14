package com.asura.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asura.todo.database.TaskDBHelper
import com.asura.todo.database.addTaskToDB
import com.asura.todo.database.editTaskInDB
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TaskInputDialog.AddTaskListener,
    TaskFragment.FragmentListener {

    private val tag = "ToDoMainActivity"
    private val create = 1
    private val edit = 2

    private lateinit var dialogFragment: TaskInputDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            askTaskDetails()
        }

        viewPager.adapter = TaskPagerAdapter(supportFragmentManager,this)
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun askTaskDetails() {
        dialogFragment = TaskInputDialog.newInstance(this, create)
        dialogFragment.show(supportFragmentManager, "Task Input Dialog")
    }

    override fun addTask(task: Task) {
        dialogFragment.dismiss()
        addTaskToDB(TaskDBHelper(this),task)
        viewPager.adapter?.notifyDataSetChanged()
    }

    override fun notifyDataSetChanged() {
        viewPager.adapter?.notifyDataSetChanged()
    }

    override fun onItemClick(task: Task) {
        dialogFragment = TaskInputDialog.newInstance(this, edit)
        dialogFragment.setTask(task)
        dialogFragment.show(supportFragmentManager, "Task Input Dialog")
    }

    override fun editTask(taskId: Int, name: String, description: String) {
        dialogFragment.dismiss()
        editTaskInDB(TaskDBHelper(this),taskId,name,description)
        viewPager.adapter?.notifyDataSetChanged()
    }
}
