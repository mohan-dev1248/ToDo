package com.asura.todo

import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.asura.todo.database.TaskContract
import com.asura.todo.database.TaskDBHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), TaskInputDialog.AddTaskListener,
    TaskAdapter.TaskItemClickListener {

    private val tag = "ToDoMainActivity"
    private val create = 1
    private val edit = 2

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
    }

    private fun fetchTasksFromDb(dbHelper: TaskDBHelper): List<Task> {
        val db = dbHelper.readableDatabase
        val projection: Array<String> = arrayOf(
            //TODO - Here need to replace the BaseColumns._ID to TaskContract.TaskEntry._ID since TaskEntry implements BaseColumns
            BaseColumns._ID,
            TaskContract.TaskEntry.TASK_NAME,
            TaskContract.TaskEntry.TASK_DESCRIPTION,
            TaskContract.TaskEntry.TASK_COMPLETE_STATUS
        )
        val cursor = db.query(
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
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.TASK_NAME))
            val desc = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.TASK_DESCRIPTION))
            val comp = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TASK_COMPLETE_STATUS))
            taskList.add(Task(id, name, desc, comp == 1))
        }
        return taskList
    }

    private fun updateCoroutine(dbHelper: TaskDBHelper) = GlobalScope.launch {
        updateList(dbHelper)
    }

    private suspend fun updateList(dbHelper: TaskDBHelper) {
        val deferred = GlobalScope.async {
            fetchTasksFromDb(dbHelper)
        }

        withContext(Dispatchers.Main) {
            val list = deferred.await()
            if (list.isNotEmpty()) {
                emptyMessageTextView.visibility = View.INVISIBLE
                taskRecyclerView.visibility = View.VISIBLE
                val taskAdapter = TaskAdapter(list, this@MainActivity)
                //ToDo - need to check removeAndRecyclerExistingViews option and put appropriate boolean value
                taskRecyclerView.swapAdapter(taskAdapter, true)
            } else {
                emptyMessageTextView.visibility = View.VISIBLE
                taskRecyclerView.visibility = View.INVISIBLE
            }
        }
    }

    private fun askTaskDetails() {
        dialogFragment = TaskInputDialog.newInstance(this)
        dialogFragment.show(supportFragmentManager, "Task Input Dialog")
    }

    override fun addTask(task: Task) {
        dialogFragment.dismiss()
        addToDB(task)
    }

    private fun addToDB(task: Task) {
        val values = ContentValues()
        values.apply {
            put(TaskContract.TaskEntry.TASK_NAME, task.getName())
            put(TaskContract.TaskEntry.TASK_DESCRIPTION, task.getDescription())
            put(TaskContract.TaskEntry.TASK_COMPLETE_STATUS, 0)
        }
        val db = dbHelper.writableDatabase
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
        updateCoroutine(dbHelper).start()
    }

    override fun delete(taskId: Int) {
        val db = dbHelper.writableDatabase
        db.delete(
            TaskContract.TaskEntry.TABLE_NAME,
            BaseColumns._ID + " = ?",
            arrayOf(taskId.toString())
        )
        updateCoroutine(dbHelper).start()
    }

    override fun updateTaskCompleteStatus(taskId: Int, completeFlag: Boolean) {
        Log.i(tag, "$taskId $completeFlag")
        val db = dbHelper.writableDatabase
        db.update(
            TaskContract.TaskEntry.TABLE_NAME,
            ContentValues().apply {
                put(TaskContract.TaskEntry.TASK_COMPLETE_STATUS, if(completeFlag) 1 else 0)
            },
            BaseColumns._ID + " = ?",
            arrayOf(taskId.toString())
        )
    }

    override fun onItemClick(task: Task) {

    }

    override fun onDestroy() {
        super.onDestroy()
        //ToDo - Need to check whether this actually cancels the coRoutine when the underlying task is huge
        if (updateCoroutine(dbHelper).isActive) updateCoroutine(dbHelper).cancel()
    }
}
