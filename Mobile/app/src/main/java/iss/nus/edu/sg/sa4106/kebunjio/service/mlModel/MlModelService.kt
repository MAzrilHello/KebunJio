package iss.nus.edu.sg.sa4106.kebunjio.service.mlModel

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

open class MlModelService : Service() {

    inner class LocalBinder: Binder(){
        fun getService(): MlModelService{
            return this@MlModelService
        }
    }

    private val binder: IBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }


    private fun makePredictPlantSpeciesConnection(requestMethod: String="GET"): HttpURLConnection {
        val use_as_ip = "10.0.2.2"
        val flaskUrl = "http://$use_as_ip:5000/predictSpecies"
        Log.d("makePredictPlantSpeciesConnection","flaskUrl: $flaskUrl")

        val url = URL(flaskUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = requestMethod
        return connection
    }


    fun getPlantSpecies(): List<String> {

        val connection = makePredictPlantSpeciesConnection("POST")

        try {

            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false

            // Create output stream
            Log.d("predictPlantSpecies","Create output stream")
            val outputStream = DataOutputStream(connection.outputStream)

            // Write the end of the multipart request
            Log.d("predictPlantSpecies","Write the end of the multipart request")
            outputStream.flush()
            outputStream.close()

            // Get response
            Log.d("getPlantSpecies","Get response")
            val responseCode = connection.responseCode
            val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            // you can get values from the response via responseObject.getString("prediction")
            val responseObject = JSONObject(responseMessage)

            Log.d("predictPlantSpecies","Response Code: $responseCode")
            Log.d("predictPlantSpecies","Response Message: $responseMessage")

            val classesArray = responseObject.getJSONArray("classes")

            // Convert JSONArray to a List<String>
            val classesList = List(classesArray.length()) { index -> classesArray.getString(index) }

            return classesList

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("predictImage","Failed to send image to Flask API.")
            Log.d("predictImage",e.stackTraceToString())
        } finally {
            connection.disconnect()
        }

        return emptyList<String>()
    }


    fun predictPlantSpecies(imageData: ByteArray): JSONObject? {
        val boundary = "Boundary-${System.currentTimeMillis()}" // Unique boundary for multipart form
        val lineEnd = "\r\n"
        val twoHyphens = "--"

        val connection = makePredictPlantSpeciesConnection("POST")

        try {

            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false

            // Set headers
            Log.d("predictPlantSpecies","Set headers")
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

            // Create output stream
            Log.d("predictPlantSpecies","Create output stream")
            val outputStream = DataOutputStream(connection.outputStream)

            // Write the image file part
            Log.d("predictPlantSpecies","Write the image file part")
            outputStream.writeBytes("$twoHyphens$boundary$lineEnd")
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"image.jpg\"$lineEnd")
            outputStream.writeBytes("Content-Type: image/jpeg$lineEnd")
            outputStream.writeBytes("Content-Transfer-Encoding: binary$lineEnd")
            outputStream.writeBytes(lineEnd)

            // Write image data
            Log.d("predictPlantSpecies","Write image data")
            outputStream.write(imageData)
            outputStream.writeBytes(lineEnd)

            // Write the end of the multipart request
            Log.d("predictPlantSpecies","Write the end of the multipart request")
            outputStream.writeBytes("$twoHyphens$boundary$twoHyphens$lineEnd")
            outputStream.flush()
            outputStream.close()

            // Get response
            Log.d("predictPlantSpecies","Get response")
            val responseCode = connection.responseCode
            val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            // you can get values from the response via responseObject.getString("prediction")
            val responseObject = JSONObject(responseMessage)

            Log.d("predictPlantSpecies","Response Code: $responseCode")
            Log.d("predictPlantSpecies","Response Message: $responseMessage")

            //connection.disconnect()

            return responseObject

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("predictImage","Failed to send image to Flask API.")
            Log.d("predictImage",e.stackTraceToString())
        } finally {
            connection.disconnect()
        }

        return null
    }

    //ML model to diagnose plants
    fun diagnosePlant(imageFile: File): String? {
        val connection = makePredictPlantSpeciesConnection()

        try {
            val boundary = "Boundary-${System.currentTimeMillis()}"
            val lineEnd = "\r\n"
            val twoHyphens = "--"

            // Set headers for multipart/form-data
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

            // Create output stream
            val outputStream = DataOutputStream(connection.outputStream)

            // Write the image file part
            outputStream.writeBytes("$twoHyphens$boundary$lineEnd")
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"${imageFile.name}\"$lineEnd")
            outputStream.writeBytes("Content-Type: image/jpeg$lineEnd")
            outputStream.writeBytes("Content-Transfer-Encoding: binary$lineEnd")
            outputStream.writeBytes(lineEnd)

            // Write image data to the output stream
            outputStream.write(imageFile.readBytes())
            outputStream.writeBytes(lineEnd)

            // End the multipart request
            outputStream.writeBytes("$twoHyphens$boundary$twoHyphens$lineEnd")
            outputStream.flush()
            outputStream.close()

            // Get the server's response
            val responseCode = connection.responseCode
            val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            Log.d("MlModelService", "Response Code: $responseCode")
            Log.d("MlModelService", "Response Message: $responseMessage")

            // Parse and return the diagnosis result
            val responseObject = JSONObject(responseMessage)
            return responseObject.getString("diagnosis") // Assume the result is under the "diagnosis" key

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("MlModelService", "Failed to send image for diagnosis.")
        } finally {
            connection.disconnect()
        }
        return null
    }
}