package iss.nus.edu.sg.sa4106.kebunjio.data

data class Reminder(
    val reminderId: Int,
    val userId: Int,
    val plantId: Int,
    val reminderType: String,
    val reminderDateTime: String, // Use proper date format handling
    val isRecurring: Boolean,
    val recurrenceInterval: String?,
    val status: String,
    val createdDateTime: String
)
