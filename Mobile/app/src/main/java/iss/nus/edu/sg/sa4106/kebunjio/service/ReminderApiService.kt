package iss.nus.edu.sg.sa4106.kebunjio.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object ReminderApiService {
    private const val BASE_URL = "http://10.0.2.2:8080/api/Reminders"

    suspend fun getRemindersByPlant(plantId: String): String? {
        return sendGetRequest("$BASE_URL/Plant/$plantId")
    }

    suspend fun getRemindersByUser(userId: String): String? {
        return sendGetRequest("$BASE_URL/user/$userId")
    }

    suspend fun addReminder(context: Context, reminderData: JSONObject, sessionCookie: String?): String? {
        if (sessionCookie == null) {
            Log.e("ReminderApiService", "Session cookie missing! User may not be logged in.")
            return null
        }

        val fullUrl = "$BASE_URL/Add"
        return sendPostRequest(fullUrl, reminderData.toString(), sessionCookie)
    }


    suspend fun editReminder(context: Context, reminderId: String, updatedData: JSONObject): String? {
        return sendPutRequest(context, "$BASE_URL/$reminderId", updatedData)
    }

    suspend fun deleteReminder(context: Context, reminderId: String): Boolean {
        return sendDeleteRequest(context, "$BASE_URL/$reminderId")
    }

    private suspend fun sendGetRequest(urlString: String): String? {
        return withContext(Dispatchers.IO) {
            runCatching {
                Log.d("ReminderApiService", "Sending GET request to: $urlString")
                val connection = (URL(urlString).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                }
                connection.handleResponse()
            }.getOrElse {
                Log.e("ReminderApiService", "GET request failed: ${it.message}")
                null
            }
        }
    }

    private suspend fun sendPostRequest(urlString: String, jsonInput: String, sessionCookie: String?): String? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("ReminderApiService", "Sending POST request to: $urlString with SESSION_COOKIE: $sessionCookie")
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")

                if (!sessionCookie.isNullOrEmpty()) {
                    connection.setRequestProperty("Cookie", sessionCookie)
                } else {
                    Log.e("ReminderApiService", "Session cookie is missing!")
                    return@withContext null
                }

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

                if (responseCode in 200..299) responseText else null
            } catch (e: Exception) {
                Log.e("ReminderApiService", "POST request failed: $urlString", e)
                null
            }

        }
    }

    private suspend fun sendPutRequest(context: Context, urlString: String, jsonInput: JSONObject): String? {
        return sendRequestWithBody(context, "PUT", urlString, jsonInput)
    }

    /** Generic DELETE request */
    private suspend fun sendDeleteRequest(context: Context, urlString: String): Boolean {
        return withContext(Dispatchers.IO) {
            runCatching {
                val connection = (URL(urlString).openConnection() as HttpURLConnection).apply {
                    requestMethod = "DELETE"
                    attachSessionCookie(context)
                }
                connection.responseCode == HttpURLConnection.HTTP_OK
            }.getOrElse {
                Log.e("ReminderApiService", "DELETE request failed: ${it.message}")
                false
            }
        }
    }

    private suspend fun sendRequestWithBody(context: Context, method: String, urlString: String, jsonInput: JSONObject): String? {
        return withContext(Dispatchers.IO) {
            runCatching {
                val connection = (URL(urlString).openConnection() as HttpURLConnection).apply {
                    requestMethod = method
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                    attachSessionCookie(context)
                }

                connection.outputStream.use { os ->
                    os.write(jsonInput.toString().toByteArray(Charsets.UTF_8))
                    os.flush()
                }
                connection.handleResponse()
            }.getOrElse {
                Log.e("ReminderApiService", "$method request failed: ${it.message}")
                null
            }
        }
    }

    /** Attaches session cookie */
    private fun HttpURLConnection.attachSessionCookie(context: Context) {
        val sessionId = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("SESSION_COOKIE", null)
        sessionId?.let { setRequestProperty("Cookie", it) }
    }

    private fun HttpURLConnection.handleResponse(): String? {
        val responseText = if (responseCode in 200..299) {
            inputStream.bufferedReader().use { it.readText() }
        } else {
            errorStream?.bufferedReader()?.use { it.readText() }
        }
        Log.d("ReminderApiService", "Response Body: $responseText")
        Log.d("ReminderApiService", "Response Code: $responseCode, Body: $responseText")
        return responseText.takeIf { responseCode in 200..299 }
    }
}
