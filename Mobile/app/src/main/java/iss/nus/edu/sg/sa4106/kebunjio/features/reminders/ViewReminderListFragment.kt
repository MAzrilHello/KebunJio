package iss.nus.edu.sg.sa4106.kebunjio.features.reminders

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import iss.nus.edu.sg.sa4106.kebunjio.adapter.ReminderGroupAdapter
import iss.nus.edu.sg.sa4106.kebunjio.data.Reminder
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentViewReminderListBinding
import iss.nus.edu.sg.sa4106.kebunjio.service.ReminderApiService
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ViewReminderListFragment : Fragment() {
    private var _binding: FragmentViewReminderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var reminderAdapter: ReminderGroupAdapter
    private var groupedReminders: MutableMap<String, List<Reminder>> = mutableMapOf()
    private var userId: String? = null

    private val reminderReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            Log.d(TAG, "Broadcast received: $action")

            if (action == "fetch_reminders") {
                val reminderList =
                    intent.getSerializableExtra("reminderList") as? ArrayList<Reminder>
                Log.d(TAG, "Received ${reminderList?.size ?: 0} reminders")

                if (reminderList.isNullOrEmpty()) {
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyStateText.visibility = View.VISIBLE
                    Log.e(TAG, "No reminders received!")
                } else {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyStateText.visibility = View.GONE
                    groupedReminders = groupRemindersByDate(reminderList)
                    reminderAdapter.updateData(groupedReminders)
                    Log.d(TAG, "Reminders updated in adapter")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString("userId") ?: requireActivity()
            .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("USER_ID", null)

        if (userId.isNullOrEmpty()) {
            Log.e(TAG, "Error: User ID not found.")
        } else {
            Log.d(TAG, "User ID received: $userId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewReminderListBinding.inflate(inflater, container, false)

        setupRecyclerView()

        Log.d(TAG, "Arguments in ViewReminderListFragment: ${arguments?.toString()}")

        val plantId = arguments?.getString("plantId") ?: ""
        if (plantId.isEmpty()) {
            // Fallback handling for missing plantId
            Log.e(TAG, "Error: plantId is missing! Check navigation from LoggedInFragment.")
            binding.recyclerView.visibility = View.GONE
            binding.emptyStateText.visibility = View.VISIBLE
            binding.emptyStateText.text = "No reminders available. Please select a plant."
            return binding.root
        } else {
            Log.d(TAG, "Received plantId: $plantId")
            fetchRemindersFromBackend(plantId)
        }

        initButtons()
        return binding.root
    }

    private fun initButtons() {
        binding.createReminderButton.setOnClickListener {
            val intent = Intent(requireContext(), ReminderActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        reminderAdapter = ReminderGroupAdapter(groupedReminders)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reminderAdapter
        }
    }

    private fun fetchRemindersFromBackend(plantId: String) {

        if (plantId.isEmpty()) {
            Log.e(TAG, "Error: plantId is missing! Check navigation from LoggedInFragment.")
        }


        lifecycleScope.launch {
            Log.d(TAG, "Fetching reminders for plantId: $plantId")

            try {
                val response = ReminderApiService.getRemindersByPlant(plantId)

                if (response.isNullOrEmpty()) {
                    Log.e(TAG, "No reminders found for plantId: $plantId")
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyStateText.visibility = View.VISIBLE
                } else {
                    Log.d(TAG, "Fetched reminders from API: $response")
                    val reminders = parseReminderList(response)
                    groupedReminders = groupRemindersByDate(reminders)
                    reminderAdapter.updateData(groupedReminders)
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyStateText.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching reminders: ${e.message}", e)
                binding.recyclerView.visibility = View.GONE
                binding.emptyStateText.visibility = View.VISIBLE
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
                val reminderDateTime =
                    jsonObject.optString("reminderDateTime", "").takeIf { it.isNotEmpty() }
                        ?.let {
                            LocalDateTime.parse(it, formatter)
                        } ?: LocalDateTime.now()

                val createdDateTime =
                    jsonObject.optString("createdDateTime", "").takeIf { it.isNotEmpty() }
                        ?.let {
                            LocalDateTime.parse(it, formatter)
                        } ?: LocalDateTime.now()

                val reminder = Reminder(
                    id = jsonObject.getString("_id"),
                    userId = jsonObject.getString("userId"),
                    plantId = jsonObject.getString("plantId"),
                    reminderType = jsonObject.getString("reminderType"),
                    reminderDateTime = reminderDateTime,
                    isRecurring = jsonObject.getBoolean("isRecurring"),
                    recurrenceInterval = jsonObject.optString("recurrenceInterval", ""),
                    status = jsonObject.getString("status"),
                    createdDateTime = createdDateTime
                )
                reminderList.add(reminder)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing reminder: ${e.message}", e)
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

        for (reminder in reminders) {
            val reminderDate = reminder.reminderDateTime.toLocalDate()

            when {
                reminderDate == today -> todayReminders.add(reminder)
                reminderDate == tomorrow -> tomorrowReminders.add(reminder)
                reminderDate.isAfter(today) && reminderDate.isBefore(weekEnd.plusDays(1)) -> remainingWeekReminders.add(
                    reminder
                )
            }
        }

        return mutableMapOf(
            "Today" to todayReminders,
            "Tomorrow" to tomorrowReminders,
            "This Week" to remainingWeekReminders
        )
    }

    override fun onStart() {
        super.onStart()
        requireContext().registerReceiver(
            reminderReceiver,
            IntentFilter("fetch_reminders"),
            Context.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onStop() {
        super.onStop()
        try {
            requireContext().unregisterReceiver(reminderReceiver)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Receiver not registered: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAG = "ViewReminderListFragment"
    }
}

