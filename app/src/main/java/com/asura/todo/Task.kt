package com.asura.todo

class Task(
    private val id: Int = -1,
    private val name: String,
    private val description: String,
    private val completeFlag: Boolean = false) {

    fun getId() = id
    fun getName() = name
    fun getDescription() = description
    fun getCompleteStatus() = completeFlag
}
