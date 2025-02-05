package iss.nus.edu.sg.sa4106.kebunjio.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class PlantSpeciesLogService : Service() {

    companion object {
        val startUrl = ""

        fun getPlantById(id: String): Plant? {
            var toReturn: Plant? = null
            val fullUrl = "${startUrl}/Plants/${id}"
            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false

            val outputStream = DataOutputStream(connection.outputStream)

            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            val responseObject = JSONObject(responseMessage)

            if (responseCode == 200) {
                toReturn = Plant(
                    id = responseObject.getString("id"),
                    ediblePlantSpeciesId = responseObject.getString("ediblePlantSpeciesId"),
                    userId = responseObject.getString("userId"),
                    name = responseObject.getString("name"),
                    disease = responseObject.getString("disease"),
                    plantedDate = responseObject.getString("plantedDate"),
                    harvestStartDate = responseObject.getString("harvestStartDate"),
                    plantHealth = responseObject.getString("plantHealth"),
                    harvested = responseObject.getBoolean("harvested")
                    )
            }
            connection.disconnect()
            return toReturn
        }
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}