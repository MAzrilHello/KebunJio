package iss.nus.edu.sg.sa4106.kebunjio.service.reminders

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import iss.nus.edu.sg.sa4106.kebunjio.features.reminders.NotificationHelper
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ReminderService : Service() {


    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private val handler = Handler(Looper.getMainLooper())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val reminderType = intent?.getStringExtra("reminderType") ?: "None"
        val reminderTime = intent?.getStringExtra("reminderTime") ?: "00:00"
        val frequencyValue = intent?.getIntExtra("frequencyValue", 1) ?: 1
        val frequencyInterval = intent?.getStringExtra("frequencyInterval") ?: "Days"

        // Schedule the reminder based on the interval
        val delay = calculateDelay(frequencyValue, frequencyInterval)
        executorService.scheduleAtFixedRate({
            handler.post {
                // Trigger the notification
                NotificationHelper.showNotification(
                    this,
                    "Reminder",
                    "Time to $reminderType at $reminderTime"
                )
            }
        }, delay, delay, TimeUnit.MILLISECONDS)

        return START_STICKY
    }

    private fun calculateDelay(frequencyValue: Int, frequencyInterval: String): Long {
        return when (frequencyInterval) {
            "Days" -> TimeUnit.DAYS.toMillis(frequencyValue.toLong())
            "Weeks" -> TimeUnit.DAYS.toMillis((frequencyValue * 7).toLong())
            "Months" -> TimeUnit.DAYS.toMillis((frequencyValue * 30).toLong())
            else -> TimeUnit.DAYS.toMillis(frequencyValue.toLong()) // Default to days
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executorService.shutdown()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}