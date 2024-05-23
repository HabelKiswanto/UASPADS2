package com.example.uaspads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

class Fragmentcreate : Fragment() {

    private lateinit var taskNameEditText: EditText
    private lateinit var taskDetailEditText: EditText
    private lateinit var urgencySpinner: Spinner
    private lateinit var confirmButton: Button
    private lateinit var returnButton: Button

    // Use viewModels delegate to initialize FragmentCreateViewModel
    private val viewModel: Fragmentcreateviewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        taskNameEditText = view.findViewById(R.id.task_name)
        taskDetailEditText = view.findViewById(R.id.task_detail)
        urgencySpinner = view.findViewById(R.id.spinner_urgency)
        confirmButton = view.findViewById(R.id.confirm_button)
        returnButton = view.findViewById(R.id.return_button)

        // Populate urgency spinner with data
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.urgency_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            urgencySpinner.adapter = adapter
        }

        confirmButton.setOnClickListener {
            viewModel.postTaskData(
                taskNameEditText.text.toString(),
                taskDetailEditText.text.toString(),
                urgencySpinner.selectedItem.toString()
            )
            findNavController().navigate(R.id.action_fragmentcreate_to_fragmenthome)
        }

        returnButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentcreate_to_fragmenthome)
        }

        return view
    }
}
