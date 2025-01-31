package iss.nus.edu.sg.sa4106.kebunjio.features.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.adapter.ReminderAdapter
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityReminderBinding
import iss.nus.edu.sg.sa4106.kebunjio.service.reminders.ReminderService
import java.util.Calendar



class ReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderBinding
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

        // Initialize spinners and frequency pickers
        initReminderTypeSpinner()
        initFrequencyPickers()
        setupClickListeners()
    }

    private fun initReminderTypeSpinner() {
        val reminderTypeOptions = listOf("Water", "Fertilizer")
        val typeAdapter = ReminderAdapter(this, reminderTypeOptions)
        binding.reminderType.adapter = typeAdapter
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

    private fun setupClickListeners() {
        binding.reminderTime.setOnClickListener {
            showTimePicker()
        }

        binding.confirmButton.setOnClickListener {
            confirmReminder()
        }
    }

    private fun showTimePicker() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = android.app.TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.reminderTime.text = formattedTime
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun confirmReminder() {
        val reminderType = binding.reminderType.selectedItem?.toString() ?: "None"
        val reminderTime = binding.reminderTime.text.toString()
        val frequencyValue = binding.frequencyNumberPicker.value
        val frequencyInterval = binding.frequencyIntervalPicker.selectedItem?.toString() ?: "Days"

        if (reminderTime.isBlank() || reminderType.isBlank()) {
            showToast("Please complete all fields.")
        } else {
            // Pass reminder details to the ReminderService
            val intent = Intent(this, ReminderService::class.java).apply {
                putExtra("reminderType", reminderType)
                putExtra("reminderTime", reminderTime)
                putExtra("frequencyValue", frequencyValue)
                putExtra("frequencyInterval", frequencyInterval)
            }
            startService(intent)
            showToast("Reminder set for $reminderType at $reminderTime, every $frequencyValue $frequencyInterval.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}
