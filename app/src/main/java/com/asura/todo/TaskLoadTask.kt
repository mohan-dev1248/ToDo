package com.asura.todo

import android.os.AsyncTask
import android.provider.BaseColumns
import com.asura.todo.database.TaskContract
import com.asura.todo.database.TaskDBHelper

class TaskLoadTask(private val dbHelper: TaskDBHelper, private val updater: UpdateList):
    AsyncTask<Unit,Unit, List<Task>>() {

    interface UpdateList{
        fun updateTaskList(taskList: List<Task>?)
    }

    override fun doInBackground(vararg p0: Unit?): List<Task> {
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

    override fun onPostExecute(result: List<Task>?) {
        updater.updateTaskList(result)
    }
}