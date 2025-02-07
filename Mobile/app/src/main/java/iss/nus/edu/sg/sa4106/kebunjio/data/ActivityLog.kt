package iss.nus.edu.sg.sa4106.kebunjio.data

import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullString
import org.json.JSONObject
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

    companion object {
        fun getFromResponseObject(responseObject: JSONObject): ActivityLog {
            val id = ifNullString(responseObject.getString("id"))
            val userId = ifNullString(responseObject.getString("userId"))
            val plantId = responseObject.getString("plantId")
            val activityType = ifNullString(responseObject.getString("activityType"))
            val activityDescription = ifNullString(responseObject.getString("activityDescription"))
            val timestamp = ifNullString(responseObject.getString("timestamp"))
            return ActivityLog(id, userId, plantId, activityType, activityDescription, timestamp)
        }
    }
}
