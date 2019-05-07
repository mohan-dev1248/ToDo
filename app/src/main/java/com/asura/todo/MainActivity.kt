package com.asura.todo

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.asura.todo.database.TaskContract
import com.asura.todo.database.TaskDBHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), TaskInputDialog.AddTaskListener {

    private val tag = "ToDoMainActivity"

    private lateinit var dialogFragment: TaskInputDialog
    private lateinit var dbHelper: TaskDBHelper

    private lateinit var taskAdapter: TaskAdapter

    private lateinit var model: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            askTaskDetails()
        }

        taskRecyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = TaskDBHelper(this)


        model = ViewModelProviders.of(
            this as FragmentActivity,
            TaskViewModelFactory(dbHelper)
        ).get(TaskViewModel::class.java)

        model.getData().observe( this , Observer {
            taskAdapter = TaskAdapter(it)
            //ToDo - need to check removeAndRecyclerExistingViews option and put appropriate boolean value
            taskRecyclerView.swapAdapter(taskAdapter, true)
        })
    }

    private fun askTaskDetails() {
        dialogFragment = TaskInputDialog.newInstance(this)
        dialogFragment.show(supportFragmentManager, "Task Input Dialog")
    }

    //ToDo - Need to Remove this and try to add directly to the database
    override fun addTask(task: Task) {
        Log.i(tag,"addTask() being called")
        dialogFragment.dismiss()
        addToDB(task)
    }

    //ToDo - Need to Remove this and try to add directly to the database
    private fun addToDB(task: Task) {
        Log.i(tag, "addToDB() being called")
        val values = ContentValues()
        values.apply {
            put(TaskContract.TaskEntry.TASK_NAME, task.getName())
            put(TaskContract.TaskEntry.TASK_DESCRIPTION, task.getDescription())
        }
        val db = dbHelper.writableDatabase
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
        model.updateData()
        Log.i(tag, "Added to DB successfully")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
