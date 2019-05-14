package com.asura.todo

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.asura.todo.TaskFragment.Companion.ALL
import com.asura.todo.TaskFragment.Companion.COMPLETED
import com.asura.todo.TaskFragment.Companion.INCOMPLETE

class TaskPagerAdapter(manager: FragmentManager,private val context: Context) : FragmentStatePagerAdapter(manager) {

    private var fragmentList: List<TaskFragment> = listOf(
        TaskFragment.newInstance(ALL, context as TaskFragment.FragmentListener),
        TaskFragment.newInstance(COMPLETED, context as TaskFragment.FragmentListener),
        TaskFragment.newInstance(INCOMPLETE, context as TaskFragment.FragmentListener)
    )

    override fun getItem(position: Int) = fragmentList[position]

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> context.getString(R.string.all)
            1 -> context.getString(R.string.completed)
            2 -> context.getString(R.string.incomplete)
            else -> null
        }
    }

    fun updateRecyclerView(){
        for(i in 0 until fragmentList.size){
            getItem(i).updateList()
        }
    }
}