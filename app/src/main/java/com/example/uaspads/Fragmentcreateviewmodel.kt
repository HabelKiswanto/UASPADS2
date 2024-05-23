package com.example.uaspads

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Fragmentcreateviewmodel : ViewModel() {

    private val client = OkHttpClient()

    fun postTaskData(taskName: String, taskDetail: String, urgency: String) {
        val status = "new" // Default value for status
        val createdTime = getCurrentDateTime() // Default value for createdTime
        val finishedTime = "" // Default value for finishedTime
        val duration = "" // Default value for duration

        val jsonData = JSONObject()
        jsonData.put("task_name", taskName)
        jsonData.put("task_detail", taskDetail)
        jsonData.put("urgency", urgency)
        jsonData.put("status", status)
        jsonData.put("createdTime", createdTime)
        jsonData.put("finishedTime", finishedTime)
        jsonData.put("duration", duration)

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = jsonData.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://10.0.2.2:5000/tasks")
            .post(requestBody)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }
                // Handle successful response if needed
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle failure if needed
            }
        }
    }

    // Function to get current date and time in string format
    private fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }
}