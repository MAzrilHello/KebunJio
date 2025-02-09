package iss.nus.edu.sg.sa4106.kebunjio.data

import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullBoolean
import java.io.Serializable
import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullString
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Reminder(
    val id: String,
    val userId: String,
    val plantId: String,
    val reminderType: String,
    val reminderDateTime: String,
    val isRecurring: Boolean,
    val recurrenceInterval: String,
    val status: String, // Status of the reminder (e.g., "Active", "Completed")
    val createdDateTime: String
) : Serializable {
    companion object {
        fun getFromResponseObject(responseObject: JSONObject): Reminder {
            val id = ifNullString(responseObject.getString("id"))
            val userId = ifNullString(responseObject.getString("userId"))
            val plantId = ifNullString(responseObject.getString("plantId"))
            val reminderType = ifNullString(responseObject.getString("reminderType"))
            val reminderDateTime = ifNullString(responseObject.getString("reminderDateTime"))
            val isRecurring = ifNullBoolean(responseObject.getBoolean("isRecurring"))
            val recurrenceInterval = ifNullString(responseObject.getString("recurrenceInterval"))
            val status = ifNullString(responseObject.getString("status"))
            val createdDateTime = ifNullString(responseObject.getString("createdDateTime"))
            return Reminder(id,userId,plantId,reminderType,reminderDateTime,isRecurring,recurrenceInterval,
                            status,createdDateTime)
        }
    }

    private fun stringToCalendar(dateText: String, pattern: String): Calendar {
        var thisCalendar = Calendar.getInstance()
        val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
        thisCalendar.time = sdf.parse(dateText)!!
        return thisCalendar
    }

    public fun getCreatedDateTimeDt(pattern: String = "yyyy-MM-dd'T'hh:mm:ss"): Calendar {
        return stringToCalendar(createdDateTime,pattern)
    }

    public fun getReminderDateTimeDt(pattern: String = "yyyy-MM-dd'T'hh:mm:ss"): Calendar {
        return stringToCalendar(reminderDateTime,pattern)
    }
}
