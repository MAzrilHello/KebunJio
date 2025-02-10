package iss.nus.edu.sg.sa4106.kebunjio.data

import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullBoolean
import java.io.Serializable
import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullString
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Reminder(
    val id: String,
    val userId: String,
    val plantId: String,
    val reminderType: String,
    val reminderDateTime: LocalDateTime,
    val isRecurring: Boolean,
    val recurrenceInterval: String,
    val status: String,
    val createdDateTime: LocalDateTime
) : Serializable {
    companion object {
        fun getFromResponseObject(responseObject: JSONObject): Reminder {
            val thisFormatter = DateTimeFormatter.ISO_DATE_TIME
            val id = ifNullString(responseObject.getString("id"))
            val userId = ifNullString(responseObject.getString("userId"))
            val plantId = ifNullString(responseObject.getString("plantId"))
            val reminderType = ifNullString(responseObject.getString("reminderType"))
            val reminderDateTime = LocalDateTime.parse(responseObject.getString("reminderDateTime"),thisFormatter)
            val isRecurring = ifNullBoolean(responseObject.getBoolean("isRecurring"))
            val recurrenceInterval = ifNullString(responseObject.getString("recurrenceInterval"))
            val status = ifNullString(responseObject.getString("status"))
            val createdDateTimeString = ifNullString(responseObject.getString("createdDateTime"))

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            //val createdDateTime = LocalDateTime.parse(createdDateTimeString, formatter)
            val createdDateTime = if (responseObject.has("createdDateTime") && !responseObject.isNull("createdDateTime")) {
                LocalDateTime.parse(responseObject.getString("createdDateTime"), formatter)
            } else {
                LocalDateTime.now() // Default value if missing
            }

            return Reminder(id, userId, plantId, reminderType, reminderDateTime, isRecurring, recurrenceInterval, status, createdDateTime)
        }
    }
}
