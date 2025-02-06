package iss.nus.edu.sg.sa4106.kebunjio.data

import java.io.Serializable

data class ActivityLog(
    val id: String,
    val userId: String,
    val plantId: String?,
    val activityType: String,
    val activityDescription: String,
    val timestamp: String
) : Serializable
