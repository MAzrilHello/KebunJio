package iss.nus.edu.sg.sa4106.kebunjio.service

import android.util.Log
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


//Singleton object that can be used throughout the app, doesn't need to be instantiated

object PlantApiService  {

    private const val BASE_URL = "https://localhost.com/api/"

    //Http GET request to retrieve plant objects
    fun getPlants(): List<Plant> {
        val url = URL(BASE_URL + "plants")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000

        val response = getResponse(connection)
        return parsePlantsResponse(response)
    }

    private fun parsePlantsResponse(response: String): List<Plant> {
        val plantsList = mutableListOf<Plant>()
        try {
            val jsonArray = JSONObject(response).getJSONArray("plants")
            for (i in 0 until jsonArray.length()) {
                val plantJson = jsonArray.getJSONObject(i)
                val plant = Plant(
                    plantId = plantJson.getInt("plantId"),
                    ediblePlantSpeciesId = plantJson.getInt("ediblePlantSpeciesId"),
                    userId = plantJson.getInt("userId"),
                    name = plantJson.getString("name")
                )
                plantsList.add(plant)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return plantsList
    }

    //Http PUT request to update plant data by Id
    private fun updatePlant(id: String, updatedData: String): String {
        val url = URL(BASE_URL + "plants/$id")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "PUT"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        //send data as json
        val plantData = JSONObject(updatedData)
        val outputStream = connection.outputStream
        val writer = BufferedWriter(OutputStreamWriter(outputStream))
        writer.write(plantData.toString())
        writer.flush()

        return getResponse(connection)
    }

    // Perform HTTP DELETE request to remove plant data by ID
    fun deletePlant(id: String): String {
        val url = URL(BASE_URL + "plants/$id")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "DELETE"
        return getResponse(connection)
    }

    // Helper function to handle the response from the server
    private fun getResponse(connection: HttpURLConnection): String {
        try {
            val responseCode = connection.responseCode
            val inputStream = if (responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            return stringBuilder.toString()
        } catch (e: Exception) {
            Log.e("PlantApiService", "Error during HTTP request", e)
            return "Error"
        } finally {
            connection.disconnect()
        }
    }
}