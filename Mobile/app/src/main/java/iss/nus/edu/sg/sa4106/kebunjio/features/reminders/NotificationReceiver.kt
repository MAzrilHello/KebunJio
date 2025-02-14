package iss.nus.edu.sg.sa4106.kebunjio.features.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationReceiver", "NotificationReceiver triggered!")
        NotificationHelper.showNotification(context)
    }
}