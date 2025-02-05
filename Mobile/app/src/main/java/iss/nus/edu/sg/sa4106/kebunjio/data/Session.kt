package iss.nus.edu.sg.sa4106.kebunjio.data

data class Session(
    val id: String,
    val userId: String?,
    val startDateTime: String,
    val lastActionDateTime: String,
    val endDateTime: String?
)
