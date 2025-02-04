package iss.nus.edu.sg.sa4106.kebunjio.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ReminderApiSerrvice : Service() {

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080"

        fun addReminder(reminderData: JSONObject): String? {
            val fullUrl = "$BASE_URL/reminders/add"
            return sendPostRequest(fullUrl, reminderData.toString())
        }

        fun getRemindersByUser(userId: String): String? {
            val fullUrl = "$BASE_URL/reminders/user/$userId"
            return sendGetRequest(fullUrl)
        }

        private fun sendPostRequest(urlString: String, jsonInput: String): String? {
            return try {
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
                responseText
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        private fun sendGetRequest(urlString: String): String? {
            return try {
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
                responseText
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}