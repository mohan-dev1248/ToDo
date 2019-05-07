package com.asura.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asura.todo.database.TaskDBHelper

class TaskViewModel(private val dbHelper: TaskDBHelper) :
    ViewModel() {
    private var liveData: TaskLiveData = TaskLiveData(dbHelper)

    fun getData(): LiveData<List<Task>>{
        return liveData
    }

    fun updateData(){
        liveData.loadData()
    }
}

class TaskViewModelFactory(private val dbHelper: TaskDBHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaskViewModel(dbHelper) as T
    }
}