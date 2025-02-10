package iss.nus.edu.sg.sa4106.kebunjio.data

import java.time.LocalDateTime

data class Reminder(
    val id: String,
    val userId: String,
    val plantId: String,
    val reminderType: String,
    val reminderDateTime: LocalDateTime,
    val isRecurring: Boolean,
    val recurrenceInterval: String,
    val status: String, // Status of the reminder (e.g., "Active", "Completed")
    val createdDateTime: LocalDateTime
) : java.io.Serializable
