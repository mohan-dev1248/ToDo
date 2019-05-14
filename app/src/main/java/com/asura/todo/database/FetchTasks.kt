package com.asura.todo.database

import android.provider.BaseColumns
import com.asura.todo.Task
import kotlinx.coroutines.*

fun fetchTasksFromDb(dbHelper: TaskDBHelper, complete: Int?): List<Task> {
    var selection: String? = null
    var selectionArgs: Array<String>? = null
    if (complete != null) {
        selection = "${TaskContract.TaskEntry.TASK_COMPLETE_STATUS} = ?"
        selectionArgs = arrayOf("$complete")
    }

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
        selection,
        selectionArgs,
        null,
        null,
        BaseColumns._ID + " DESC",
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

fun updateCoroutine(dbHelper: TaskDBHelper, complete: Int? = null, callback: (List<Task>) -> Unit) = GlobalScope.launch {
    updateList(dbHelper, complete, callback)
}

suspend fun updateList(dbHelper: TaskDBHelper, complete: Int?, callback: (List<Task>) -> Unit) {
    val deferred = GlobalScope.async {
        fetchTasksFromDb(dbHelper, complete)
    }

    withContext(Dispatchers.Main) {
        callback(deferred.await())
    }
}