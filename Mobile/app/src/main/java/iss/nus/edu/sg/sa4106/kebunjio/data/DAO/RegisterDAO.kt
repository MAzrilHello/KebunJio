package iss.nus.edu.sg.sa4106.kebunjio.data.DAO

import java.io.Serializable

data class RegisterDAO(
    val email: String,
    val username: String,
    val password: String,
    val confirmPassword: String,
    val contactPhone: String
) : Serializable {
}