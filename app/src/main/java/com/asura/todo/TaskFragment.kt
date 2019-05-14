package com.asura.todo


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asura.todo.database.TaskDBHelper
import com.asura.todo.database.deleteFromDB
import com.asura.todo.database.updateCoroutine
import com.asura.todo.database.updateTaskCompleteStatusInDB
import kotlinx.android.synthetic.main.task_page.*

private const val ARG_TASK_BY_COMPLETE_STATUS = "taskByComplete"

class TaskFragment : Fragment(), TaskAdapter.TaskItemClickListener {

    private var completeStatus: String? = null
    private lateinit var dbHelper: TaskDBHelper

    interface FragmentListener {
        fun onItemClick(task: Task)
        fun notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            completeStatus = it.getString(ARG_TASK_BY_COMPLETE_STATUS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.task_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskRecyclerView.layoutManager = LinearLayoutManager(activity)
        dbHelper = TaskDBHelper(activity as Context)
        when (completeStatus) {
            COMPLETED -> updateCoroutine(dbHelper, 1){ list -> updateCallback(list)}.start()
            INCOMPLETE -> updateCoroutine(dbHelper, 0){ list -> updateCallback(list)}.start()
            ALL -> updateCoroutine(dbHelper, null){ list -> updateCallback(list)}.start()
        }

    }

    private fun updateCallback(list:List<Task>) {
        if (list.isNotEmpty()) {
            emptyMessageTextView.visibility = View.INVISIBLE
            taskRecyclerView.visibility = View.VISIBLE
            val taskAdapter = TaskAdapter(list, this@TaskFragment)
            //ToDo - need to check removeAndRecyclerExistingViews option and put appropriate boolean value
            taskRecyclerView.swapAdapter(taskAdapter, true)
        } else {
            emptyMessageTextView.visibility = View.VISIBLE
            taskRecyclerView.visibility = View.INVISIBLE
        }
    }

    override fun onItemClick(task: Task) {
        fragmentListener.onItemClick(task)
    }

    override fun delete(taskID: Int) {
        deleteFromDB(dbHelper, taskID)
        fragmentListener.notifyDataSetChanged()
    }

    override fun updateTaskCompleteStatus(taskId: Int, completeFlag: Boolean) {
        updateTaskCompleteStatusInDB(dbHelper, taskId, completeFlag)
        fragmentListener.notifyDataSetChanged()
    }

    companion object {
        const val ALL = "ALL"
        const val COMPLETED = "COMPLETED"
        const val INCOMPLETE = "INCOMPLETE"
        private lateinit var fragmentListener: FragmentListener

        @JvmStatic
        fun newInstance(completeStatus: String, fragmentListener: FragmentListener): TaskFragment {
            this.fragmentListener = fragmentListener
            return TaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TASK_BY_COMPLETE_STATUS, completeStatus)
                }
            }
        }
    }
}