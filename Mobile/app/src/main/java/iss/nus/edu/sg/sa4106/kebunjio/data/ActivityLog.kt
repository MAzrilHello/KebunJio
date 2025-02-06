package iss.nus.edu.sg.sa4106.kebunjio.data

import java.io.Serializable

data class ActivityLog(
    val id: String,
    val userId: String,
    val plantId: String?,
    val activityType: String,
    val activityDescription: String, // can be blank
    val timestamp: String


) : Serializable {
    public fun dataIsGood(): Boolean {
        return (!plantId.equals("") &&
                !activityType.equals("") &&
                !timestamp.equals(""))
    }
}
