package com.example.uaspads

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Tasks
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.Task
import java.net.URL

class Fragmenthome : Fragment() {
    private lateinit var newTaskButton: Button
    private lateinit var inProgressButton: Button
    private lateinit var doneButton: Button
    private lateinit var viewModel: Fragmenthomeviewmodel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(Fragmenthomeviewmodel::class.java)

        newTaskButton = view.findViewById(R.id.new_task_button)

        newTaskButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmenthome_to_fragmentnew)
        }

        inProgressButton = view.findViewById(R.id.in_progress_button)

        inProgressButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmenthome_to_fragmentprogress)
        }
        doneButton = view.findViewById(R.id.done_button)

        doneButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmenthome_to_fragmentdone)
        }

        val addTaskButton = view.findViewById<Button>(R.id.add_task)
        addTaskButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmenthome_to_fragmentcreate)
        }

        // Update button counts
        viewModel.newTaskCount.observe(viewLifecycleOwner) { count ->
            newTaskButton.text = "New ($count)"
        }
        viewModel.inProgressCount.observe(viewLifecycleOwner) { count ->
            inProgressButton.text = "In Progress ($count)"
        }
        viewModel.doneCount.observe(viewLifecycleOwner) { count ->
            doneButton.text = "Done ($count)"
        }
    }
}


//    private fun fetchDataFromDatabase(status: String): Int {
//        // You need to modify this function to fetch data directly from the database
//        // For demonstration purposes, I'm returning a dummy count here
//        return when (status) {
//            "New" -> 10 // Dummy count
//            "In Progress" -> 5 // Dummy count
//            "Done" -> 3 // Dummy count
//            else -> 0
//        }
//    }


