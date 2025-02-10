package iss.nus.edu.sg.sa4106.kebunjio.data

data class ActivityLog(
    val logId: Int,
    val userId: Int,
    val plantId: Int?,
    val activityType: String,
    val activityDescription: String,
    val timestamp: String
)
