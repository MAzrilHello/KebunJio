package iss.nus.edu.sg.sa4106.kebunjio.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.data.EdiblePlantSpecies
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import org.json.JSONArray
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class PlantSpeciesLogService : Service() {

    companion object {
        val startUrl = "http://localhost:8080/api"




        fun createOrUpdatePlant(thePlant: Plant,isUpdate: Boolean, forIntent: Intent?): Plant? {
            // url for adding and updating situation
            val fullUrl: String = if (isUpdate) {"${startUrl}/Plants/${thePlant.id}"} else {"${startUrl}/Plants"}

            if (isUpdate) {
                forIntent?.setAction("update_plant")
            } else {
                forIntent?.setAction("create_plant")
            }

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = if (isUpdate) {"PUT"} else {"POST"}

            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false

            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")

            val plantJson = JSONObject().apply {
                put("id", thePlant.id)
                put("ediblePlantSpeciesId", thePlant.ediblePlantSpeciesId)
                put("userId", thePlant.userId)
                put("name", thePlant.name)
                put("disease", thePlant.disease)
                put("plantedDate", thePlant.plantedDate)
                put("harvestStartDate", thePlant.harvestStartDate)
                put("plantHealth", thePlant.plantHealth)
                put("harvested", thePlant.harvested)
            }

            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.writeBytes(plantJson.toString())
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            forIntent?.putExtra("responseCode",responseCode)
            val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            var returnPlant: Plant? = null

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseObject = JSONObject(responseMessage)
                returnPlant = Plant(
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
            forIntent?.putExtra("plant",returnPlant)
            return returnPlant
        }


        fun deletePlant(id: String, forIntent: Intent?): Int {
            val fullUrl = "${startUrl}/Plants/${id}"

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "DELETE"
            forIntent?.setAction("delete_plant")

            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false

            val outputStream = DataOutputStream(connection.outputStream)

            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            //val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            forIntent?.putExtra("responseCode",responseCode)

            connection.disconnect()

            return responseCode
        }


        fun deleteLog(id: String, forIntent: Intent?): Int {
            val fullUrl = "${startUrl}/ActivityLog/${id}"

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "DELETE"
            forIntent?.setAction("delete_activity_log")

            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false

            val outputStream = DataOutputStream(connection.outputStream)

            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            //val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            forIntent?.putExtra("responseCode",responseCode)

            connection.disconnect()

            return responseCode
        }


        fun createOrUpdateLog(theLog: ActivityLog,isUpdate: Boolean, forIntent: Intent?): ActivityLog? {
            // url for adding and updating situation
            val fullUrl: String = if (isUpdate) {"${startUrl}/ActivityLog/${theLog.id}"} else {"${startUrl}/ActivityLog"}

            if (isUpdate) {
                forIntent?.setAction("update_activity_log")
            } else {
                forIntent?.setAction("create_activity_log")
            }

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = if (isUpdate) {"PUT"} else {"POST"}

            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false

            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")

            val logJson = JSONObject().apply {
                put("id", theLog.id)
                put("userId", theLog.userId)
                put("plantId", theLog.plantId)
                put("activityType", theLog.activityType)
                put("activityDescription", theLog.activityDescription)
                put("timestamp", theLog.timestamp)
            }

            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.writeBytes(logJson.toString())
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            forIntent?.putExtra("responseCode",responseCode)
            val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            var returnLog: ActivityLog? = null

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseObject = JSONObject(responseMessage)
                returnLog = ActivityLog(
                    id = responseObject.getString("id"),
                    userId = responseObject.getString("userId"),
                    plantId = responseObject.getString("plantId"),
                    activityType = responseObject.getString("activityType"),
                    activityDescription = responseObject.getString("activityDescription"),
                    timestamp = responseObject.getString("timestamp")
                )
            }

            connection.disconnect()
            forIntent?.putExtra("activityLog",returnLog)
            return returnLog
        }


        fun getActivityLog(id: String, singleUserOrPlant: String, forIntent: Intent? = null): MutableList<ActivityLog> {
            val isList: Boolean
            val fullUrl: String
            if (singleUserOrPlant == "single") {
                // find by id
                fullUrl = "${startUrl}/ActivityLog/${id}"
                isList = false
                forIntent?.setAction("get_activity_log")
            } else if (singleUserOrPlant == "user") {
                // find by userId
                fullUrl = "${startUrl}/ActivityLog/Users/${id}"
                isList = true
                forIntent?.setAction("get_activity_log_byname")
            } else if (singleUserOrPlant == "plant") {
                // find by plantId
                fullUrl = "${startUrl}/ActivityLog/Plants/${id}"
                isList = true
                forIntent?.setAction("get_activity_log_byplant")
            } else {
                // if single, user or plant is not properly indicated, search single by default
                fullUrl = "${startUrl}/ActivityLog/${id}"
                isList = false
                forIntent?.setAction("get_activity_log")
            }

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

            forIntent?.putExtra("responseCode",responseCode)

            val logList = mutableListOf<ActivityLog>()

            if (responseCode == HttpURLConnection.HTTP_OK) {
                if (isList) {
                    val jsonArray = JSONArray(responseMessage)
                    for (i in 0..<jsonArray.length()) {
                        val responseObject = JSONObject(responseMessage)
                        val oneLog = ActivityLog(
                            id = responseObject.getString("id"),
                            userId = responseObject.getString("userId"),
                            plantId = responseObject.getString("plantId"),
                            activityType = responseObject.getString("activityType"),
                            activityDescription = responseObject.getString("activityDescription"),
                            timestamp = responseObject.getString("timestamp")
                        )
                        logList.add(oneLog)
                    }
                } else {
                    val responseObject = JSONObject(responseMessage)
                    val oneLog = ActivityLog(
                        id = responseObject.getString("id"),
                        userId = responseObject.getString("userId"),
                        plantId = responseObject.getString("plantId"),
                        activityType = responseObject.getString("activityType"),
                        activityDescription = responseObject.getString("activityDescription"),
                        timestamp = responseObject.getString("timestamp")
                    )
                    logList.add(oneLog)
                }
            }
            forIntent?.putExtra("logList",ArrayList(logList))
            connection.disconnect()
            return logList
        }

        fun getSpecies(id: String?, byName: Boolean = false, forIntent: Intent? = null): MutableList<EdiblePlantSpecies> {
            val isList: Boolean
            val fullUrl: String
            if (id == null) {
                fullUrl = "${startUrl}/EdiblePlant"
                isList = true
                forIntent?.setAction("get_species_all")
            } else if (byName) {
                fullUrl = "${startUrl}/EdiblePlant/byname/${id}"
                isList = true
                forIntent?.setAction("get_species_byname")
            } else {
                fullUrl = "${startUrl}/EdiblePlant/${id}"
                isList = false
                forIntent?.setAction("get_species_byid")
            }

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

            forIntent?.putExtra("responseCode",responseCode)

            val speciesList = mutableListOf<EdiblePlantSpecies>()

            if (responseCode == HttpURLConnection.HTTP_OK) {

                if (isList) {
                    val jsonArray = JSONArray(responseMessage)
                    for (i in 0..<jsonArray.length()) {
                        val responseObject = JSONObject(responseMessage)
                        val oneSpecies = EdiblePlantSpecies(
                            id = responseObject.getString("id"),
                            name = responseObject.getString("name"),
                            scientificName = responseObject.getString("scientificName"),
                            ediblePlantGroup = responseObject.getString("ediblePlantGroup"),
                            description = responseObject.getString("description"),
                            wateringTips = responseObject.getString("wateringTips"),
                            sunlight = responseObject.getString("sunlight"),
                            soilType = responseObject.getString("soilType"),
                            harvestTime = responseObject.getString("harvestTime"),
                            commonPests = responseObject.getString("commonPests"),
                            growingSpace = responseObject.getString("growingSpace"),
                            fertilizerTips = responseObject.getString("fertilizerTips"),
                            specialNeeds = responseObject.getString("specialNeeds"),
                            imageURL = responseObject.getString("imageURL")
                        )
                        speciesList.add(oneSpecies)
                    }
                } else {
                    val responseObject = JSONObject(responseMessage)
                    val oneSpecies = EdiblePlantSpecies(
                        id = responseObject.getString("id"),
                        name = responseObject.getString("name"),
                        scientificName = responseObject.getString("scientificName"),
                        ediblePlantGroup = responseObject.getString("ediblePlantGroup"),
                        description = responseObject.getString("description"),
                        wateringTips = responseObject.getString("wateringTips"),
                        sunlight = responseObject.getString("sunlight"),
                        soilType = responseObject.getString("soilType"),
                        harvestTime = responseObject.getString("harvestTime"),
                        commonPests = responseObject.getString("commonPests"),
                        growingSpace = responseObject.getString("growingSpace"),
                        fertilizerTips = responseObject.getString("fertilizerTips"),
                        specialNeeds = responseObject.getString("specialNeeds"),
                        imageURL = responseObject.getString("imageURL")
                    )
                    speciesList.add(oneSpecies)
                }
            }
            forIntent?.putExtra("speciesList",ArrayList(speciesList))
            connection.disconnect()
            return speciesList
        }

        fun getPlants(id: String, byUser: Boolean = false, forIntent: Intent? = null): MutableList<Plant> {
            val isList: Boolean
            val fullUrl: String
            //if (id == null) {
            //    fullUrl = "${startUrl}/Plants"
            //    isList = true
            //}
            if (byUser) {
                fullUrl = "${startUrl}/Plants/Users/${id}"
                isList = true
                forIntent?.setAction("get_plants_byname")
            } else {
                fullUrl = "${startUrl}/Plants/${id}"
                isList = false
                forIntent?.setAction("get_plants")
            }

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

            forIntent?.putExtra("responseCode",responseCode)

            val plantList = mutableListOf<Plant>()

            if (responseCode == HttpURLConnection.HTTP_OK) {

                if (isList) {
                    val jsonArray = JSONArray(responseMessage)
                    for (i in 0..<jsonArray.length()) {
                        val responseObject = jsonArray.getJSONObject(i)
                        val onePlant = Plant(
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
                        plantList.add(onePlant)
                    }
                } else {
                    val responseObject = JSONObject(responseMessage)
                    val onePlant = Plant(
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
                    plantList.add(onePlant)
                }
            }
            forIntent?.putExtra("plantList",ArrayList(plantList))
            connection.disconnect()
            return plantList
        }
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val returnIntent = Intent()
        if (intent.action.equals("get_species")) {
            // get edible plant species
            val id: String? = intent.getStringExtra("id")
            val byName: Boolean = intent.getBooleanExtra("byName",false)
            Thread{
                getSpecies(id,byName,returnIntent)
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("get_plants")) {
            // get plant
            val idTry: String? = intent.getStringExtra("id")
            val id: String
            id = if (idTry == null) {""} else {idTry}
            val byUser: Boolean = intent.getBooleanExtra("byUser",false)
            Thread{
                getPlants(id,byUser,returnIntent)
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("change_plant")) {
            // add or update plants
            val thePlant = intent.getSerializableExtra("plant") as? Plant
            val isUpdate: Boolean = intent.getBooleanExtra("isUpdate",false)
            Thread{
                createOrUpdatePlant(thePlant!!,isUpdate,returnIntent)
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("delete_plant")) {
            val idTry: String? = intent.getStringExtra("id")
            val id: String
            id = if (idTry == null) {""} else {idTry}
            Thread{
                deletePlant(id,returnIntent)
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("get_activity_logs")) {
            val idTry: String? = intent.getStringExtra("id")
            val id: String
            id = if (idTry == null) {""} else {idTry}
            val singleUserOrPlantTry: String? = intent.getStringExtra("singleUserOrPlant")
            val singleUserOrPlant = if (singleUserOrPlantTry==null) {""} else {singleUserOrPlantTry.lowercase()}
            Thread {
                getActivityLog(id, singleUserOrPlant, returnIntent)
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("change_activity_log")) {
            val theLog = intent.getSerializableExtra("activityLog") as? ActivityLog
            val isUpdate: Boolean = intent.getBooleanExtra("isUpdate",false)
            Thread{
                createOrUpdateLog(theLog!!,isUpdate,returnIntent)
                sendBroadcast(returnIntent)
            }
        } else if (intent.action.equals("delete_activity_log")) {
            val idTry: String? = intent.getStringExtra("id")
            val id: String
            id = if (idTry == null) {""} else {idTry}
            Thread{
                deletePlant(id,returnIntent)
                sendBroadcast(returnIntent)
            }.start()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    inner class LocalBinder: Binder(){
        fun getService(): PlantSpeciesLogService {
            return this@PlantSpeciesLogService
        }
    }

    private val binder: IBinder = LocalBinder()

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }


}