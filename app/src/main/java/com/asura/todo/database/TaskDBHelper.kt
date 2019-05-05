package com.asura.todo.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns



private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${TaskContract.TaskEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${TaskContract.TaskEntry.TASK_NAME} TEXT," +
            "${TaskContract.TaskEntry.TASK_DESCRIPTION} TEXT," +
            "${TaskContract.TaskEntry.TASK_DEADLINE} TEXT)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TaskContract.TaskEntry.TABLE_NAME}"

class TaskDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    companion object {
        const val DATABASE_NAME = "todo.db"
        const val DATABASE_VERSION = 1
    }
}