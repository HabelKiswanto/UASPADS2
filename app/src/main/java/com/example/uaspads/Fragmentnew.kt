package com.example.uaspads

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView

class Fragmentnew : Fragment() {

    private lateinit var taskButtonsLayout: LinearLayout // Declare as class-level property

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new, container, false)

        // Initialize taskButtonsLayout
        taskButtonsLayout = view.findViewById(R.id.taskButtonsLayout)

        // Fetch data from the server initially
        fetchDataFromServer("new", "Normal") { taskDataList ->
            displayFilteredTasks(taskDataList)
        }

        val backButton: Button = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentnew_to_fragmenthome)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_Normal -> {
                    fetchDataFromServer("new", "Normal") { taskDataList ->
                        displayFilteredTasks(taskDataList)
                    }
                    true
                }
                R.id.action_Urgent -> {
                    fetchDataFromServer("new", "Urgent") { taskDataList ->
                        displayFilteredTasks(taskDataList)
                    }
                    true
                }
                R.id.action_Important -> {
                    fetchDataFromServer("new", "Important") { taskDataList ->
                        displayFilteredTasks(taskDataList)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun displayFilteredTasks(taskDataList: List<TaskInfo>?) {
        taskButtonsLayout.removeAllViews()
        taskDataList?.forEach { taskData ->
            val button = Button(requireContext())
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            button.text = "${taskData.task_name}\nUrgency: ${taskData.urgency}\nCreated Time: ${taskData.createdTime}"
            button.setOnClickListener {
                Log.d("Fragmentnew", "Task ID: ${taskData.id}")
                val bundle = Bundle().apply {
                    putString("taskId", taskData.id?.toString())
                }
                findNavController().navigate(R.id.action_fragmentnew_to_fragmentaction, bundle)
            }
            // Add button to the layout
            taskButtonsLayout.addView(button)
        }
    }

    private fun fetchDataFromServer(status: String, urgency: String, callback: (List<TaskInfo>?) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = "http://10.0.2.2:5000/tasks"
                val response = URL(url).readText()
                val taskDataList = Gson().fromJson(response, Array<TaskInfo>::class.java).toList()
                val filteredTasks = taskDataList.filter { it.status == status && it.urgency == urgency }
                withContext(Dispatchers.Main) {
                    callback(filteredTasks)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }
}
