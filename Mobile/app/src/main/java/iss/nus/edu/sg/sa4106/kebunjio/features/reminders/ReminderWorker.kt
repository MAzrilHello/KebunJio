package iss.nus.edu.sg.sa4106.kebunjio.features.reminders

import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.Worker

class ReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        // Show notification when WorkManager triggers the task
        NotificationHelper.showNotification(applicationContext)
        return Result.success()
    }
}
