package com.asura.todo

import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.asura.todo.database.TaskContract
import com.asura.todo.database.TaskDBHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), TaskInputDialog.AddTaskListener {

    private val tag = "ToDoMainActivity"

    private lateinit var dialogFragment: TaskInputDialog
    private lateinit var dbHelper: TaskDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            askTaskDetails()
        }

        taskRecyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = TaskDBHelper(this)

        updateCoroutine(dbHelper).start()
        Log.i(tag,"After calling the coroutine")
    }

    private fun fetchTasksFromDb(dbHelper: TaskDBHelper): List<Task>{
        val db = dbHelper.readableDatabase
        val projection: Array<String> = arrayOf(
            //TODO - Here need to replace the BaseColumns._ID to TaskContract.TaskEntry._ID since TaskEntry implements BaseColumns
            BaseColumns._ID,
            TaskContract.TaskEntry.TASK_NAME,
            TaskContract.TaskEntry.TASK_DESCRIPTION
        )
        val cursor =  db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val taskList = mutableListOf<Task>()
        while(cursor.moveToNext()){
            val name = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TASK_NAME))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TASK_DESCRIPTION))
            taskList.add(Task(name,desc))
        }
        return taskList
    }

    private fun updateCoroutine(dbHelper: TaskDBHelper) = GlobalScope.launch {
        updateList(dbHelper)
    }

    private suspend fun updateList(dbHelper: TaskDBHelper){
        val deferred = GlobalScope.async{
            fetchTasksFromDb(dbHelper)
        }

        withContext(Dispatchers.Main){
            val list = deferred.await()
            val taskAdapter = TaskAdapter(list)
            //ToDo - need to check removeAndRecyclerExistingViews option and put appropriate boolean value
            taskRecyclerView.swapAdapter(taskAdapter,true)
            Log.i(tag,"After updating ")
        }
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
        updateCoroutine(dbHelper).start()
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

    override fun onDestroy() {
        super.onDestroy()
        //ToDo - Need to check whether this actually cancels the coRoutine when the underlying task is huge
        if(updateCoroutine(dbHelper).isActive) updateCoroutine(dbHelper).cancel()
    }
}
