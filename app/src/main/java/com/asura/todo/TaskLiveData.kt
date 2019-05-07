package com.asura.todo

import androidx.lifecycle.LiveData
import com.asura.todo.database.TaskDBHelper

class TaskLiveData (private val dbHelper: TaskDBHelper):
    LiveData<List<Task>>(), TaskLoadTask.UpdateList {
    init {
        loadData()
    }

    fun loadData(){
        TaskLoadTask(dbHelper,this).execute()
    }

    override fun updateTaskList(taskList: List<Task>?) {
        value = taskList
    }
}

//TODO - Need to check and see if LiveData needs to be in Singleton. If so need to add the below code
//open class SingletonHolder<out T, in A>(creator: (A) -> T) {
//    private var creator: ((A) -> T)? = creator
//    @Volatile private var instance: T? = null
//
//    fun getInstance(arg: A): T {
//        val i = instance
//        if (i != null) {
//            return i
//        }
//
//        return synchronized(this) {
//            val i2 = instance
//            if (i2 != null) {
//                i2
//            } else {
//                val created = creator!!(arg)
//                instance = created
//                creator = null
//                created
//            }
//        }
//    }
//}