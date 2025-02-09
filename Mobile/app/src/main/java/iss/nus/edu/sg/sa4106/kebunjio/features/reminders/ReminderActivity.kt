package iss.nus.edu.sg.sa4106.kebunjio.features.reminders


import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.adapter.ReminderAdapter
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityReminderBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails.ViewPlantDetailsActivity
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
        initButtons()
        initReminderTypeSpinner()
        initFrequencyPickers()
    }

    private fun initButtons() {

        binding.viewPlantDetailsButton.setOnClickListener {
            val intent = Intent(this, ViewPlantDetailsActivity::class.java)
            startActivity(intent)
        }

        binding.timeButton.setOnClickListener {
            showHourPicker()
        }

        binding.confirmButton.setOnClickListener {
            confirmReminder()
        }

    }

    private fun initReminderTypeSpinner() {
        val reminderTypeOptions = listOf("Water", "Fertilizer")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, reminderTypeOptions)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reminderType.adapter = typeAdapter
    }

    private fun showHourPicker() {
        val myCalendar = Calendar.getInstance()
        val hour = myCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = myCalendar.get(Calendar.MINUTE)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.timeButton.text = formattedTime // Update the TextView with the selected time
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
