package iss.nus.edu.sg.sa4106.kebunjio.data

import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullBoolean
import java.io.Serializable
import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullString
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
            val id = ifNullString(responseObject.optString("id"))
            val userId = ifNullString(responseObject.optString("userId"))
            val plantId = ifNullString(responseObject.optString("plantId"))
            val reminderType = ifNullString(responseObject.optString("reminderType"))
            val isRecurring = ifNullBoolean(responseObject.optBoolean("isRecurring"))
            val recurrenceInterval = ifNullString(responseObject.optString("recurrenceInterval"))
            val status = ifNullString(responseObject.optString("status"))

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

            val reminderDateTimeString = ifNullString(responseObject.optString("reminderDateTime"))
            val reminderDateTime = try {
                LocalDateTime.parse(reminderDateTimeString, formatter)
            } catch (e: DateTimeParseException) {
                LocalDateTime.now() // Fallback if parsing fails
            }

            val createdDateTimeString = ifNullString(responseObject.optString("createdDateTime"))
            val createdDateTime = try {
                LocalDateTime.parse(createdDateTimeString, formatter)
            } catch (e: DateTimeParseException) {
                LocalDateTime.now()
            }
            return Reminder(id, userId, plantId, reminderType, reminderDateTime, isRecurring, recurrenceInterval, status, createdDateTime)
        }
    }
}
