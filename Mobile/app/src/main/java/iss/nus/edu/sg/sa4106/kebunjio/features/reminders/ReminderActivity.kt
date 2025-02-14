package iss.nus.edu.sg.sa4106.kebunjio.features.reminders


import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.adapter.ReminderAdapter
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityReminderBinding
import iss.nus.edu.sg.sa4106.kebunjio.service.PlantApiService
import iss.nus.edu.sg.sa4106.kebunjio.service.ReminderApiService
import iss.nus.edu.sg.sa4106.kebunjio.service.ReminderApiService.addReminder
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Calendar

class ReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderBinding
    private var sessionCookie: String? = null
    private var plantId: String? = null
    private var plantName: String? = null
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showToast("Notification permission is required for reminders.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionCookie = intent.getStringExtra("SESSION_COOKIE")
        if (sessionCookie.isNullOrEmpty()) {
            Log.e("ReminderActivity", "SESSION_COOKIE not passed!")
            showToast("Session expired. Please log in again.")
            finish() // Exit activity if session is invalid
            return
        } else {
            Log.d("ReminderActivity", "Received SESSION_COOKIE: $sessionCookie")
        }

        // Store plantId and plantName as well
        plantId = intent.getStringExtra("plantId")
        if (plantId.isNullOrEmpty()) {
            Log.e("ReminderActivity", "plantId is NULL in Intent extras!")
            showToast("Error: Plant ID not found")
            return
        }
        Log.d("ReminderActivity", "Received plantId: $plantId")

        plantName = intent.getStringExtra("plantName") ?: "Unknown Plant"
        Log.d("ReminderActivity", "Received Plant Name: $plantName")

        initButtons()
//        initReminderTypeSpinner()
        initFrequencyPickers()

        fetchPlantName(plantId!!)

//        fetchReminders(userId, plantId)
    }

    private fun initButtons() {
        binding.viewPlantDetailsButton.setOnClickListener {
            finish()
        }

        binding.timeButton.setOnClickListener {
            showHourPicker()
        }

        binding.confirmButton.setOnClickListener {
            if (sessionCookie.isNullOrEmpty()) {
                showToast("Error: Session not passed correctly to add reminder")
                Log.e("ReminderActivity", "Cannot add reminder: Session cookie is null")
            } else {
                addReminder(sessionCookie!!)
            }
        }
    }

//    private fun initReminderTypeSpinner() {
//        val reminderTypeOptions = listOf("Water", "Fertilizer")
//        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, reminderTypeOptions)
//        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.reminderType.adapter = typeAdapter
//    }

    private fun showHourPicker() {
        val myCalendar = Calendar.getInstance()
        val hour = myCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = myCalendar.get(Calendar.MINUTE)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.timeButton.text = formattedTime
        }

        val timePickerDialog = TimePickerDialog(
            this,
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            timeSetListener,
            hour,
            minute,
            true
        )
        timePickerDialog.setTitle("Choose hour:")
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    private fun initFrequencyPickers() {
        binding.frequencyNumberPicker.apply {
            minValue = 1
            maxValue = 60
            wrapSelectorWheel = true
        }

        val intervalOptions = listOf("Days", "Weeks", "Months")
        val intervalAdapter = ReminderAdapter(this, intervalOptions)
        binding.frequencyIntervalPicker.adapter = intervalAdapter
    }

    private fun fetchPlantName(plantId: String) {
        Log.d("ReminderActivity", "fetchPlantName() called with Plant ID: $plantId")

        lifecycleScope.launch {
            try {
                Log.d("ReminderActivity", "Calling PlantApiService.getPlantNameById()...")
                val plantName = PlantApiService.getPlantNameById(plantId)

                if (plantName != null) {
                    Log.d("ReminderActivity", "Successfully fetched plant name: $plantName")
                    runOnUiThread {
                        binding.plantName.text = plantName
                    }
                } else {
                    Log.e("ReminderActivity", "No plant name found for ID: $plantId")
                    showToast("Failed to fetch plant name")
                }
            } catch (e: Exception) {
                Log.e("ReminderActivity", "Error fetching plant name", e)
                showToast("Error fetching plant name")
            }
        }
    }

//    private fun fetchReminders(userId: String, plantId: String) {
//        lifecycleScope.launch {
//            val response = ReminderApiService.getRemindersByUser(userId)
//            if (response != null) {
//                Log.d("ReminderActivity", "Fetched reminders: $response")
//                // Optionally parse response and populate UI fields
//            } else {
//                Log.e("ReminderActivity", "Failed to fetch reminders.")
//            }
//        }
//    }

    private fun addReminder(sessionCookie: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("USER_ID", null) ?: return showToast("Error: User ID not found")
        val plantId = intent.getStringExtra("plantId") ?: return showToast("Error: Plant ID not found")
        val plantName = binding.plantName.text.toString().takeIf { it.isNotBlank() } ?: "Unknown Plant"

        val reminderType = binding.reminderType.text.toString().trim()
        val reminderTime = binding.timeButton.text.toString()
        val frequencyValue = binding.frequencyNumberPicker.value
        val frequencyInterval = binding.frequencyIntervalPicker.selectedItem?.toString() ?: "Days"

        if (!reminderTime.matches(Regex("\\d{2}:\\d{2}"))) {
            showToast("Invalid reminder time. Please select a valid time.")
            return
        }

        Log.d("ReminderActivity", "Adding new reminder: User ID: $userId, Plant ID: $plantId, Type: $reminderType, Time: $reminderTime, Frequency: $frequencyValue $frequencyInterval")

        val reminderJson = JSONObject().apply {
            put("userId", userId)
            put("plantId", plantId)
            put("reminderType", reminderType)
            put("reminderDateTime", "2025-02-10T$reminderTime:00")
            put("isRecurring", true)
            put("recurrenceInterval", "$frequencyValue $frequencyInterval")
            put("status", "Active")
        }

        lifecycleScope.launch {
            val response = addReminder(this@ReminderActivity, reminderJson, sessionCookie)
            if (response != null) {
                showToast("Reminder set for $plantName at $reminderTime, every $frequencyValue $frequencyInterval.")
                Log.d("ReminderActivity", "Reminder successfully added: $response")
            } else {
                showToast("Failed to add reminder.")
                Log.e("ReminderActivity", "Failed to add reminder to backend.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}
