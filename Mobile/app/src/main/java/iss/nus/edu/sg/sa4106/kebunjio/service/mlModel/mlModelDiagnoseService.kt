package iss.nus.edu.sg.sa4106.kebunjio.service.mlModel

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL


class mlModelDiagnoseService : MlModelService() {

    inner class LocalBinder : Binder() {
        fun getService(): mlModelDiagnoseService {
            return this@mlModelDiagnoseService
        }
    }

    private val binder: IBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun diagnosePlant(imageFile: File): String? {

        val urlString = "http://localhost:8080/api/diagnose"

        val connection = (URL(urlString).openConnection() as HttpURLConnection)


        val url = "http://localhost:8080/api/diagnose"


        try {
            val boundary = "Boundary-${System.currentTimeMillis()}"
            val lineEnd = "\r\n"
            val twoHyphens = "--"

            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

            val outputStream = DataOutputStream(connection.outputStream)

            outputStream.writeBytes("$twoHyphens$boundary$lineEnd")
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"${imageFile.name}\"$lineEnd")
            outputStream.writeBytes("Content-Type: image/jpeg$lineEnd")
            outputStream.writeBytes("Content-Transfer-Encoding: binary$lineEnd")
            outputStream.writeBytes(lineEnd)
            outputStream.write(imageFile.readBytes())
            outputStream.writeBytes(lineEnd)
            outputStream.writeBytes("$twoHyphens$boundary$twoHyphens$lineEnd")
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            Log.d("mlModelDiagnoseService", "Response Code: $responseCode")
            Log.d("mlModelDiagnoseService", "Response Message: $responseMessage")

            val responseObject = JSONObject(responseMessage)
            return responseObject.getString("diagnosis")

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("mlModelDiagnoseService", "Failed to send image for diagnosis.")
        } finally {
            connection.disconnect()
        }
        return null
    }
}