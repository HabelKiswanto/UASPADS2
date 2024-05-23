package com.example.uaspads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class Fragmentactionviewmodel : ViewModel() {

    fun fetchTaskDetails(taskId: Int, onComplete: (TaskInfo) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = "http://10.0.2.2:5000/tasks/$taskId"
                val response = URL(url).readText()
                val taskData = Gson().fromJson(response, TaskInfo::class.java)
                withContext(Dispatchers.Main) {
                    onComplete(taskData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createNewTask(
        taskName: String,
        taskDetail: String,
        urgency: String,
        status: String,
        createdTime: String,
        finishedTime: String,
        duration: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = "http://10.0.2.2:5000/tasks"
                val requestBody = "{\"task_name\": \"$taskName\", \"task_detail\": \"$taskDetail\", \"urgency\": \"$urgency\", \"status\": \"$status\", \"createdTime\": \"$createdTime\", \"finishedTime\": \"$finishedTime\", \"duration\": \"$duration\"}"
                val response = URL(url).apply {
                    openConnection().run {
                        doOutput = true
                        setRequestProperty("Content-Type", "application/json; charset=utf-8")
                        getOutputStream().bufferedWriter().use {
                            it.write(requestBody)
                            it.flush()
                        }
                        inputStream.bufferedReader().readText()
                    }
                }
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = "http://10.0.2.2:5000/tasks/$taskId"
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"
                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    println("Task deleted successfully")
                } else {
                    println("Error deleting task. Response code: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

