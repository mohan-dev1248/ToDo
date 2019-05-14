package com.asura.todo

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.asura.todo.TaskFragment.Companion.ALL
import com.asura.todo.TaskFragment.Companion.COMPLETED
import com.asura.todo.TaskFragment.Companion.INCOMPLETE

class TaskPagerAdapter(manager: FragmentManager,private val context: Context) : FragmentStatePagerAdapter(manager) {
    /**
     * Return the Fragment associated with a specified position.
     */
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> TaskFragment.newInstance(ALL, context as TaskFragment.FragmentListener)
            1 -> TaskFragment.newInstance(COMPLETED, context as TaskFragment.FragmentListener)
            2 -> TaskFragment.newInstance(INCOMPLETE, context as TaskFragment.FragmentListener)
            else -> Fragment()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
    /**
     * Return the number of views available.
     */
    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> context.getString(R.string.all)
            1 -> context.getString(R.string.completed)
            2 -> context.getString(R.string.incomplete)
            else -> null
        }
    }

}