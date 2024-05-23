package com.example.uaspads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class Fragmenthomeviewmodel : ViewModel() {
    private val _newTaskCount = MutableLiveData<Int>()
    val newTaskCount: LiveData<Int> get() = _newTaskCount

    private val _inProgressCount = MutableLiveData<Int>()
    val inProgressCount: LiveData<Int> get() = _inProgressCount

    private val _doneCount = MutableLiveData<Int>()
    val doneCount: LiveData<Int> get() = _doneCount

    init {
        fetchDataAndUpdateCounts()
    }

    private fun fetchDataAndUpdateCounts() {
        GlobalScope.launch(Dispatchers.IO) {
            _newTaskCount.postValue(fetchDataFromDatabase("new"))
            _inProgressCount.postValue(fetchDataFromDatabase("in progress"))
            _doneCount.postValue(fetchDataFromDatabase("done"))
        }
    }

    private fun fetchDataFromDatabase(status: String): Int {
        try {
            val url = "http://10.0.2.2:5000/tasks?status=$status"
            val response = URL(url).readText()
            val taskList = Gson().fromJson(response, Array<TaskInfo>::class.java)

            // Filter the taskList to include only tasks with the specified status
            val filteredTasks = taskList.filter { it.status == status }

            // Return the size of the filteredTasks
            return filteredTasks.size
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }
}
