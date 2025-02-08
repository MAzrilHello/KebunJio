package iss.nus.edu.sg.sa4106.kebunjio.data


import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullBoolean
import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullString
import org.json.JSONObject
import java.io.Serializable

data class User(
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val isAdmin: Boolean
) : Serializable {
    companion object {


        fun getFromResponseObject(responseObject: JSONObject): User {
            val id = ifNullString(responseObject.getString("id"))
            val username = ifNullString(responseObject.getString("username"))
            val email = ifNullString(responseObject.getString("email"))
            val phoneNumber = ifNullString(responseObject.getString("phoneNumber"))
            val admin = ifNullBoolean(responseObject.getBoolean("admin"))
            return User(id,username,email,phoneNumber,admin)

        }
    }
}
