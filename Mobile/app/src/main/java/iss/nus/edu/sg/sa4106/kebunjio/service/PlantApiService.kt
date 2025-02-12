package iss.nus.edu.sg.sa4106.kebunjio.service

import android.util.Log
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


//Singleton object that can be used throughout the app, doesn't need to be instantiated

object PlantApiService  {

    //private const val BASE_URL = "https://localhost.com/api/"
    private const val BASE_URL = "http://34.124.209.141:8080/api"

    // Use Coroutine to make network request
    suspend fun getPlantsByUser(userId: String, sessionCookie: String): List<Plant> {
        return withContext(Dispatchers.IO) {  // Perform the network call in the background
            val url = URL("$BASE_URL/plants/Users/$userId")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Cookie", sessionCookie)
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            // Get the response from the server
            val response = getResponse(connection)
            val plantList = parsePlantsResponse(response)
            connection.disconnect()

            plantList  // Return the list of plants
        }
    }

    private fun getResponse(connection: HttpURLConnection): String {
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
    }

    private fun parsePlantsResponse(response: String): List<Plant> {
        val plantList = mutableListOf<Plant>()
        try {
            val jsonArray = JSONArray(response)
            for (i in 0 until jsonArray.length()) {
                val plantJson = jsonArray.getJSONObject(i)
                val plant = Plant(
                    id = plantJson.getString("id"),
                    ediblePlantSpeciesId = plantJson.getString("ediblePlantSpeciesId"),
                    userId = plantJson.getString("userId"),
                    name = plantJson.getString("name"),
                    disease = plantJson.getString("disease"),
                    plantedDate = plantJson.getString("plantedDate"),
                    harvestStartDate = plantJson.getString("harvestStartDate"),
                    plantHealth = plantJson.getString("plantHealth"),
                    harvested = plantJson.getBoolean("harvested")
                )
                plantList.add(plant)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return plantList
    }
}
