package com.example.uaspads

data class TaskInfo(

    val id: Int?,
    val task_name: String,
    val task_detail: String,
    val urgency: String,
    val status: String,
    val createdTime: String,
    val finishedTime: String,
    val duration: String
)

