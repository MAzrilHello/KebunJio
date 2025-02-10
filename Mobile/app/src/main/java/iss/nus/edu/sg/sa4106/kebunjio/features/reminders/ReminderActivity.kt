package iss.nus.edu.sg.sa4106.kebunjio.features.reminders

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.adapter.TimeSpinnerAdapter
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityReminderBinding

class ReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinners()
    }

    private fun initSpinners() {
        val reminderTypeOptions = listOf("Water", "Fertilizer")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, reminderTypeOptions)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reminderType.adapter = typeAdapter


        val timeSlots = (0..23).map {
                hour -> String.format("%02d:00", hour)
        }
        val timeAdapter = TimeSpinnerAdapter(this, timeSlots)
        binding.reminderTime.adapter = timeAdapter

        val frequencyOptions = (1..60).flatMap {
                interval -> listOf ("$interval day", "$interval week", "$interval month")
        }
        val frequencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, frequencyOptions)
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reminderFrequency.adapter = frequencyAdapter
    }

    private fun setSpinnerListeners() {
        // Listener for reminderFrequency Spinner
        binding.reminderFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedFrequency = parent?.getItemAtPosition(position).toString()
                println("Selected reminder frequency: $selectedFrequency")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional: Handle case where no selection is made
            }
        }
    }
}
