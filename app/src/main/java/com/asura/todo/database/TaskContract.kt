package com.asura.todo.database

import android.provider.BaseColumns

object TaskContract {

    object TaskEntry : BaseColumns{
        const val TABLE_NAME = "task"
        const val TASK_NAME = "name"
        const val TASK_DESCRIPTION = "description"
    }
}