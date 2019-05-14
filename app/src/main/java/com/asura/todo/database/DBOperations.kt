package com.asura.todo.database

import android.content.ContentValues
import android.provider.BaseColumns
import com.asura.todo.Task

fun addTaskToDB(dbHelper: TaskDBHelper, task: Task) {
    val values = ContentValues()
    values.apply {
        put(TaskContract.TaskEntry.TASK_NAME, task.getName())
        put(TaskContract.TaskEntry.TASK_DESCRIPTION, task.getDescription())
        put(TaskContract.TaskEntry.TASK_COMPLETE_STATUS, 0)
    }
    val db = dbHelper.writableDatabase
    db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
}

fun deleteFromDB(dbHelper: TaskDBHelper, taskId: Int) {
    val db = dbHelper.writableDatabase
    db.delete(
        TaskContract.TaskEntry.TABLE_NAME,
        BaseColumns._ID + " = ?",
        arrayOf(taskId.toString())
    )
}

fun updateTaskCompleteStatusInDB(dbHelper: TaskDBHelper, taskId: Int, completeFlag: Boolean) {
    val db = dbHelper.writableDatabase
    db.update(
        TaskContract.TaskEntry.TABLE_NAME,
        ContentValues().apply {
            put(TaskContract.TaskEntry.TASK_COMPLETE_STATUS, if (completeFlag) 1 else 0)
        },
        BaseColumns._ID + " = ?",
        arrayOf(taskId.toString())
    )
}

fun editTaskInDB(dbHelper: TaskDBHelper, taskId: Int, name: String, description: String) {
    val db = dbHelper.writableDatabase
    db.update(
        TaskContract.TaskEntry.TABLE_NAME,
        ContentValues().apply {
            put(TaskContract.TaskEntry.TASK_NAME, name)
            put(TaskContract.TaskEntry.TASK_DESCRIPTION, description)
        },
        BaseColumns._ID + " = ?",
        arrayOf(taskId.toString())
    )
}