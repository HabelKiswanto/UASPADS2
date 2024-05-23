package com.example.uaspads

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

class Fragmentaction : Fragment() {

    private val viewModel = Fragmentactionviewmodel()

    private var fetchedTaskName: String? = null
    private var fetchedUrgency: String? = null
    private var fetchedCreatedTime: String? = null
    private var fetchedStatus: String? = null
    private var fetchedFinishedTime: String? = null
    private var fetchedDuration: String? = null

    private fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }

    private fun calculateDuration(createdTime: String, finishedTime: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val createdDateTime = LocalDateTime.parse(createdTime, formatter)
        val finishedDateTime = LocalDateTime.parse(finishedTime, formatter)

        val duration = Duration.between(createdDateTime, finishedDateTime)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        return "$hours hours, $minutes minutes"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_action, container, false)

        val notesEditText: EditText = view.findViewById(R.id.notesEditText)
        val createTaskButton: Button = view.findViewById(R.id.actionButton)
        val backButton: Button = view.findViewById(R.id.backactionButton)

        val taskId = arguments?.getString("taskId")
        Log.d("FragmentAction", "Task ID: $taskId")

        if (taskId != null) {
            viewModel.fetchTaskDetails(taskId.toInt()) { taskData ->
                fetchedTaskName = taskData.task_name
                fetchedUrgency = taskData.urgency
                fetchedCreatedTime = taskData.createdTime
                fetchedStatus = taskData.status
                fetchedFinishedTime = taskData.finishedTime
                fetchedDuration = if (taskData.status == "done" && taskData.finishedTime != null) {
                    calculateDuration(taskData.createdTime, taskData.finishedTime)
                } else {
                    null
                }
                displayTaskDetails(taskData, view)

                // Dynamically set the text of the createTaskButton based on fetchedStatus
                createTaskButton.text = when (fetchedStatus) {
                    "new" -> "Take"
                    "in progress" -> "Done"
                    else -> "Action"
                }
                createTaskButton.visibility = when (fetchedStatus) {
                    "new", "in progress" -> View.VISIBLE
                    else -> View.GONE
                }
                notesEditText.visibility = when (fetchedStatus) {
                    "new", "in progress" -> View.VISIBLE
                    else -> View.GONE
                }
            }
        }

        val navController = findNavController()

        createTaskButton.setOnClickListener {
            val taskName = fetchedTaskName ?: "test"
            val urgency = fetchedUrgency ?: "test"
            val taskDetail = notesEditText.text.toString()
            val createdTime = fetchedCreatedTime ?: "test"
            val status = when (fetchedStatus) {
                "new" -> "in progress"
                "in progress" -> "done"
                else -> fetchedStatus ?: "unknown"
            }
            val finishedTime = when (fetchedStatus) {
                "in progress" -> getCurrentDateTime()
                else -> ""
            }
            val duration =
                if (status == "done") calculateDuration(createdTime, finishedTime) else ""

            val taskIdToDelete = arguments?.getString("taskId")
            if (!taskIdToDelete.isNullOrEmpty()) {
                viewModel.deleteTask(taskIdToDelete.toInt())
            }

            viewModel.createNewTask(
                taskName,
                taskDetail,
                urgency,
                status,
                createdTime,
                finishedTime,
                duration
            ) {
                navController.navigate(R.id.action_fragmentaction_to_fragmenthome)
            }
        }

        backButton.setOnClickListener {
            navController.navigate(R.id.action_fragmentaction_to_fragmenthome)
        }

        return view
    }

    private fun displayTaskDetails(taskData: TaskInfo, view: View) {
        val taskDetailsTextView: TextView = view.findViewById(R.id.taskDetailsTextView)
        val additionalDetails = if (taskData.status == "done") {
            """
        Finished Time: 
        
        ${taskData.finishedTime ?: "Not Finished Yet"}
        Duration: ${
                calculateDuration(
                    taskData.createdTime,
                    taskData.finishedTime ?: "Not Available"
                )
            }
        """.trimIndent()
        } else {
            ""
        }

        taskDetailsTextView.text = """
Task Name:
    
${taskData.task_name}
    
Created Time:
    
  ${taskData.createdTime}
    
  Task Detail:
   
  ${taskData.task_detail}
    
  $additionalDetails
""".trimIndent()
    }
}
