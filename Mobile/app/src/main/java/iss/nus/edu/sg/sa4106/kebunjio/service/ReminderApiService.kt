package iss.nus.edu.sg.sa4106.kebunjio.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object ReminderApiService {
    private const val BASE_URL = "http://10.0.2.2:8080/api/reminders"

    suspend fun getRemindersByUser(userId: String): String? {
        val fullUrl = "$BASE_URL/user/$userId"
        return sendGetRequest(fullUrl)
    }
    private suspend fun sendGetRequest(urlString: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                val responseText = if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    connection.errorStream?.bufferedReader()?.use { it.readText() }
                }
                connection.disconnect()
                Log.d("ReminderApiService", "GET Response Code: $responseCode")
                Log.d("ReminderApiService", "GET Response Body: $responseText")
                responseText
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun addReminder(reminderData: JSONObject): String? {
        val fullUrl = "$BASE_URL/add"
        return sendPostRequest(fullUrl, reminderData.toString())
    }

    private suspend fun sendPostRequest(urlString: String, jsonInput: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("ReminderApiService", "Sending POST request to: $urlString")
                Log.d("ReminderApiService", "Request Body: $jsonInput")

                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                connection.outputStream.use { os ->
                    os.write(jsonInput.toByteArray(Charsets.UTF_8))
                    os.flush()
                }

                val responseCode = connection.responseCode
                val responseText = if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    connection.errorStream?.bufferedReader()?.use { it.readText() }
                }

                connection.disconnect()
                Log.d("ReminderApiService", "Response Code: $responseCode")
                Log.d("ReminderApiService", "Response Body: $responseText")
                responseText
            } catch (e: Exception) {
                Log.e("ReminderApiService", "Exception occurred: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }
}