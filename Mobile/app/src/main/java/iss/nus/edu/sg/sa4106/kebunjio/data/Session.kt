package iss.nus.edu.sg.sa4106.kebunjio.data

data class Session(
    val sessionId: Int,
    val userId: Int?,
    val startDateTime: String,
    val lastActionDateTime: String,
    val endDateTime: String?
)
