package iss.nus.edu.sg.sa4106.kebunjio.service.mlModel

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MlModelDiagnoseService : MlModelService() {

    inner class LocalBinder : Binder() {
        fun getService(): MlModelDiagnoseService {
            return this@MlModelDiagnoseService
        }
    }

    private val binder: IBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun diagnosePlant(imageFile: File): String? {

        val urlString = "http://10.0.2.2:5000/api/diagnose"
        var connection: HttpURLConnection? = null

        return try {
            val boundary = "Boundary-${System.currentTimeMillis()}"
            val lineEnd = "\r\n"
            val twoHyphens = "--"
            Log.d("mlModelDiagnoseService", "Connecting to Flask endpoint: $urlString")

            val url = URL(urlString)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            connection.doOutput = true
            connection.doInput = true
            connection.useCaches = false
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

            Log.d("mlModelDiagnoseService", "Sending image file: ${imageFile.name}")

            DataOutputStream(connection.outputStream).use { outputStream ->
                outputStream.writeBytes("$twoHyphens$boundary$lineEnd")
                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"${imageFile.name}\"$lineEnd")
                outputStream.writeBytes("Content-Type: image/jpeg$lineEnd")
                outputStream.writeBytes("Content-Transfer-Encoding: binary$lineEnd")
                outputStream.writeBytes(lineEnd)
                outputStream.write(imageFile.readBytes())
                outputStream.writeBytes(lineEnd)
                outputStream.writeBytes("$twoHyphens$boundary$twoHyphens$lineEnd")
                outputStream.flush()
            }

            val responseCode = connection.responseCode
            val responseMessage = if (responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error: No response"
            }

            Log.d("mlModelDiagnoseService", "Response Code: $responseCode")
            Log.d("mlModelDiagnoseService", "Response Message: $responseMessage")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseObject = JSONObject(responseMessage)
                val status = responseObject.getString("status")
                val details = responseObject.getString("details")
                "Diagnosis: $status, Details: $details"
            } else {
                Log.d("mlModelDiagnoseService", "Unexpected response code: $responseCode")
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("mlModelDiagnoseService", "Failed to send image for diagnosis.")
            null
        } finally {
            connection?.disconnect()
        }
    }
}