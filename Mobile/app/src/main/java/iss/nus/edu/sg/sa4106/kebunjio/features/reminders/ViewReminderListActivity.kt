package iss.nus.edu.sg.sa4106.kebunjio.features.reminders

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.adapter.ReminderGroupAdapter
import iss.nus.edu.sg.sa4106.kebunjio.data.Reminder
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityViewReminderListBinding
import iss.nus.edu.sg.sa4106.kebunjio.service.ReminderApiService
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ViewReminderListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewReminderListBinding
    private lateinit var reminderAdapter: ReminderGroupAdapter
    private var groupedReminders: MutableMap<String, List<Reminder>> = mutableMapOf()
    private var plantId: String? = null
    private var plantName: String? = null
    private var sessionCookie: String? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewReminderListBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        sessionCookie = intent.getStringExtra("SESSION_COOKIE")
        if (sessionCookie == null) {
            Log.e("ViewReminderListActivity", "SESSION_COOKIE is missing!")
        } else {
            Log.d("ViewReminderListActivity", "Received SESSION_COOKIE: $sessionCookie")
        }

        userId = intent.getStringExtra("userId")
        if (userId.isNullOrEmpty()) {
            Log.e("ViewReminderListActivity", "userId is missing!")
        } else {
            Log.d("ViewReminderListActivity", "Received userId: $userId")
        }

        plantId = intent.getStringExtra("plantId")
        Log.d("ViewReminderListActivity", "Received plantId: $plantId")

        if (plantId.isNullOrEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyStateText.visibility = View.VISIBLE
            binding.emptyStateText.text = "No reminders available. Please select a plant."
        } else {
            setupRecyclerView()
            fetchReminders(plantId!!)
        }

        plantName = intent.getStringExtra("plantName")

        initButtons()
    }

    private fun setupRecyclerView() {
        if (!::reminderAdapter.isInitialized) {
            reminderAdapter = ReminderGroupAdapter(groupedReminders)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewReminderListActivity)
            adapter = reminderAdapter
        }
    }

    private fun initButtons() {

        binding.selectPlantButton.setOnClickListener {
            finish()
        }
        binding.createReminderButton.setOnClickListener {
            Log.d("ViewReminderListActivity", "Attempting to pass sessionCookie: $sessionCookie to ReminderActivity")
            Log.d("ViewReminderListActivity", "Attempting to pass plantId: $plantId to ReminderActivity")
            Log.d("ViewReminderListActivity", "Attempting to pass plantName: $plantName to ReminderActivity")

            binding.createReminderButton.setOnClickListener {
                Log.d(
                    "ViewReminderListActivity",
                    "Passing sessionCookie: $sessionCookie to ReminderActivity"
                )

                val intent = Intent(this, ReminderActivity::class.java)
                intent.putExtra("SESSION_COOKIE", sessionCookie)
                intent.putExtra("userId", userId)
                intent.putExtra("plantId", plantId)
                intent.putExtra("plantName", plantName)
                startActivity(intent)
            }
        }

    }

    private fun fetchReminders(plantId: String) {
        lifecycleScope.launch {
            Log.d("ViewReminderListActivity", "Fetch reminders for plantId: $plantId")
            try {
                val response = ReminderApiService.getRemindersByPlant(plantId)
                Log.d("ViewReminderListActivity", "Response from API: $response")

                if (response.isNullOrEmpty()) {
                    showEmptyState()
                } else {
                    val reminders = parseReminderList(response)
                    groupedReminders = groupRemindersByDate(reminders)

                    Log.d("ViewReminderListActivity", "Grouped reminders: $groupedReminders")

                    runOnUiThread {
                        if (!isDestroyed) {
                            reminderAdapter.updateData(groupedReminders)
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.emptyStateText.visibility = View.GONE
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewReminderListActivity", "Error fetching reminders: ${e.message}")
                showEmptyState()
            }
        }
    }

    private fun parseReminderList(response: String): List<Reminder> {
        val reminderList = mutableListOf<Reminder>()
        val jsonArray = JSONArray(response)
        val formatter = DateTimeFormatter.ISO_DATE_TIME

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            try {
                val reminderDateTime = jsonObject.optString("reminderDateTime", "").takeIf { it.isNotEmpty() }
                    ?.let { LocalDateTime.parse(it, formatter) } ?: LocalDateTime.now()

                val createdDateTime = jsonObject.optString("createdDateTime", "").takeIf { it.isNotEmpty() }
                    ?.let { LocalDateTime.parse(it, formatter) } ?: LocalDateTime.now()

                // Ensure `isRecurring` is properly retrieved as a Boolean
                val isRecurring = jsonObject.optBoolean("isRecurring", false)

                // Ensure `recurrenceInterval` is a String (default to "1 Days" if missing)
                val recurrenceInterval = jsonObject.optString("recurrenceInterval", "1 Days")

                val reminder = Reminder(
                    id = jsonObject.getString("id"),
                    userId = jsonObject.getString("userId"),
                    plantId = jsonObject.getString("plantId"),
                    reminderType = jsonObject.getString("reminderType"),
                    reminderDateTime = reminderDateTime,
                    isRecurring = isRecurring,
                    recurrenceInterval = recurrenceInterval,
                    status = jsonObject.getString("status"),
                    createdDateTime = createdDateTime
                )
                reminderList.add(reminder)
            } catch (e: Exception) {
                Log.e("ViewReminderListActivity", "Error parsing reminder: ${e.message}", e)
            }
        }
        return reminderList
    }

    private fun groupRemindersByDate(reminders: List<Reminder>): MutableMap<String, List<Reminder>> {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val weekEnd = today.plusDays(6)

        val todayReminders = mutableListOf<Reminder>()
        val tomorrowReminders = mutableListOf<Reminder>()
        val remainingWeekReminders = mutableListOf<Reminder>()

        Log.d("ViewReminderListActivity", "Today's date: $today")
        Log.d("ViewReminderListActivity", "Reminders received: $reminders")

        for (reminder in reminders) {
            val nextReminderDate = calculateNextReminderDate(reminder)
            Log.d("ViewReminderListActivity", "Next Reminder Date: $nextReminderDate")

            when {
                nextReminderDate.isEqual(today) -> {
                    todayReminders.add(reminder)
                    Log.d("ViewReminderListActivity", "Added to Today: $reminder")
                }
                nextReminderDate.isEqual(tomorrow) -> {
                    tomorrowReminders.add(reminder)
                    Log.d("ViewReminderListActivity", "Added to Tomorrow: $reminder")
                }
                nextReminderDate.isAfter(today) && nextReminderDate.isBefore(weekEnd.plusDays(1)) -> {
                    remainingWeekReminders.add(reminder)
                    Log.d("ViewReminderListActivity", "Added to This Week: $reminder")
                }
            }
        }

        return mutableMapOf<String, List<Reminder>>().apply {
            if (todayReminders.isNotEmpty()) put("Today", todayReminders)
            if (tomorrowReminders.isNotEmpty()) put("Tomorrow", tomorrowReminders)
            if (remainingWeekReminders.isNotEmpty()) put("This Week", remainingWeekReminders)
        }
    }


    private fun calculateNextReminderDate(reminder: Reminder): LocalDate {
        var nextReminderDate = reminder.reminderDateTime.toLocalDate()

        // If it's recurring, adjust nextReminderDate based on recurrenceInterval
        if (reminder.isRecurring) {
            val intervalParts = reminder.recurrenceInterval.split(" ")

            if (intervalParts.size == 2) {
                val intervalValue = intervalParts[0].toIntOrNull()
                val intervalUnit = intervalParts[1].lowercase()

                if (intervalValue != null) {
                    while (nextReminderDate.isBefore(LocalDate.now())) {
                        nextReminderDate = when {
                            intervalUnit.contains("day") -> nextReminderDate.plusDays(intervalValue.toLong())
                            intervalUnit.contains("week") -> nextReminderDate.plusWeeks(intervalValue.toLong())
                            intervalUnit.contains("month") -> nextReminderDate.plusMonths(intervalValue.toLong())
                            else -> nextReminderDate
                        }
                    }
                }
            }
        }

        return nextReminderDate
    }

    private fun showEmptyState() {
        runOnUiThread {
            if (!isDestroyed) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyStateText.visibility = View.VISIBLE
            }
        }
    }

}