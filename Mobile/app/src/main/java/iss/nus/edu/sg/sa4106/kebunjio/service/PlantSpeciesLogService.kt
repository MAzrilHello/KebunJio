package iss.nus.edu.sg.sa4106.kebunjio.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.data.EdiblePlantSpecies
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.data.Reminder
import org.json.JSONArray
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class PlantSpeciesLogService : Service() {

    companion object {
        const val startUrl = "http://10.0.2.2:8080/api"
        //const val startUrl = "http://34.124.209.141:8080/api"
        const val predictSpeciesUrl = "http://10.0.2.2:5000/predictSpecies"
        const val timeoutTime = 15000


        fun createOrUpdatePlant(thePlant: Plant,isUpdate: Boolean, forIntent: Intent?, sessionCookie: String): Plant? {
            // url for adding and updating situation
            val fullUrl: String = if (isUpdate) {"${startUrl}/Plants/${thePlant.id}"} else {"${startUrl}/Plants"}
            val action: String
            if (isUpdate) {
                action = "update_plant"
            } else {
                action = "create_plant"
            }
            forIntent?.setAction(action)

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            Log.d("PlantSpeciesLogService","${action} URL: ${fullUrl}")

            var returnPlant: Plant? = null
            var responseCode = -1
            var finalCookie = sessionCookie
            Log.d("PlantSpeciesLogService","${action} Cookie: ${finalCookie}")

            try {
                connection.requestMethod = if (isUpdate) {"PUT"} else {"POST"}
                connection.connectTimeout = timeoutTime
                connection.readTimeout = timeoutTime
                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false
                CookieHandling.setSessionCookie(connection,sessionCookie)

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

                responseCode = connection.responseCode
                forIntent?.putExtra("responseCode",responseCode)
                Log.d("PlantSpeciesLogService","${action} Response Code: ${responseCode}")

                if (responseCode in 200..299) {
                    val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }
                    val responseObject = JSONObject(responseMessage)
                    returnPlant = Plant.getFromResponseObject(responseObject)
                    finalCookie = CookieHandling.extractSessionCookie(connection)
                    Log.d("PlantSpeciesLogService","${action} Updated Cookie: ${finalCookie}")
                }
            } catch (e: Exception) {
                forIntent?.putExtra("exception",e.toString())
                Log.d("PlantSpeciesLogService","${action} error: ${e.toString()}")
            } finally {
                forIntent?.putExtra("responseCode",responseCode)
                forIntent?.putExtra("plant",returnPlant)
                if (finalCookie == "") {
                    Log.d("PlantSpeciesLogService","${action} will not update the previous cookie. Passing ${sessionCookie}.")
                    forIntent?.putExtra("sessionCookie",sessionCookie)
                } else {
                    forIntent?.putExtra("sessionCookie",finalCookie)
                }

                connection.disconnect()
            }

            return returnPlant
        }


        fun deletePlant(id: String, forIntent: Intent?, sessionCookie: String): Int {
            val fullUrl = "${startUrl}/Plants/${id}"

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "DELETE"
            forIntent?.setAction("delete_plant")
            var responseCode = -1
            var finalCookie = sessionCookie
            Log.d("PlantSpeciesLogService","delete_plant Cookie: ${finalCookie}")

            try {
                connection.connectTimeout = timeoutTime
                connection.readTimeout = timeoutTime
                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false
                CookieHandling.setSessionCookie(connection,sessionCookie)

                val outputStream = DataOutputStream(connection.outputStream)

                outputStream.flush()
                outputStream.close()

                responseCode = connection.responseCode
                //val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            } catch (e: Exception) {
                forIntent?.putExtra("exception",e.toString())
                Log.d("PlantSpeciesLogService","delete_plant error: ${e.toString()}")
            } finally {
                connection.disconnect()
                forIntent?.putExtra("responseCode",responseCode)
                if (finalCookie == "") {
                    Log.d("PlantSpeciesLogService","delete_plant will not update the previous cookie. Passing ${sessionCookie}.")
                    forIntent?.putExtra("sessionCookie",sessionCookie)
                } else {
                    forIntent?.putExtra("sessionCookie",finalCookie)
                }
            }



            return responseCode
        }


        fun deleteLog(id: String, forIntent: Intent?, sessionCookie: String): Int {
            val fullUrl = "${startUrl}/ActivityLog/${id}"

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "DELETE"
            val action = "delete_activity_log"
            forIntent?.setAction(action)
            var responseCode = -1
            var finalCookie = sessionCookie

            try {
                connection.connectTimeout = timeoutTime
                connection.readTimeout = timeoutTime
                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false
                CookieHandling.setSessionCookie(connection,sessionCookie)

                val outputStream = DataOutputStream(connection.outputStream)

                outputStream.flush()
                outputStream.close()

                responseCode = connection.responseCode
                Log.d("PlantSpeciesLogService","${action} responseCode: ${responseCode}")
                //val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }


            } catch (e: Exception) {
                forIntent?.putExtra("exception",e.toString())
                Log.d("PlantSpeciesLogService","${action} error: ${e.toString()}")
            } finally {
                forIntent?.putExtra("responseCode",responseCode)
                if (finalCookie == "") {
                    Log.d("PlantSpeciesLogService","${action} will not update the previous cookie. Passing ${sessionCookie}.")
                    forIntent?.putExtra("sessionCookie",sessionCookie)
                } else {
                    forIntent?.putExtra("sessionCookie",finalCookie)
                }
                connection.disconnect()
            }

            return responseCode
        }


        fun createOrUpdateLog(theLog: ActivityLog,isUpdate: Boolean, forIntent: Intent?, sessionCookie: String): ActivityLog? {
            // url for adding and updating situation
            val fullUrl: String = if (isUpdate) {"${startUrl}/ActivityLog/${theLog.id}"} else {"${startUrl}/ActivityLog"}
            val action: String
            if (isUpdate) {
                action = "update_activity_log"
                forIntent?.setAction(action)
            } else {
                action = "create_activity_log"
                forIntent?.setAction(action)
            }

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            Log.d("PlantSpeciesLogService","${action} URL: ${fullUrl}")
            connection.requestMethod = if (isUpdate) {"PUT"} else {"POST"}
            var returnLog: ActivityLog? = null
            var responseCode = -1
            var finalCookie = sessionCookie
            Log.d("PlantSpeciesLogService","${action} Cookie: ${finalCookie}")

            try {
                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false
                CookieHandling.setSessionCookie(connection,sessionCookie)

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

                responseCode = connection.responseCode

                val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

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
                    finalCookie = CookieHandling.extractSessionCookie(connection)
                }
            } catch (e: Exception) {
                forIntent?.putExtra("exception",e.toString())
                Log.d("PlantSpeciesLogService","${action} error: ${e.toString()}")
            } finally {
                connection.disconnect()
                forIntent?.putExtra("activityLog",returnLog)
                forIntent?.putExtra("responseCode",responseCode)
                if (finalCookie == "") {
                    Log.d("PlantSpeciesLogService","${action} will not update the previous cookie. Passing ${sessionCookie}.")
                    forIntent?.putExtra("sessionCookie",sessionCookie)
                } else {
                    forIntent?.putExtra("sessionCookie",finalCookie)
                }
            }


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
                forIntent?.setAction("get_activity_log_byuser")
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

            Log.d("PlantSpeciesLogService","get_activity_logs URL: ${fullUrl} isList: ${isList}")

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            var responseCode = -1
            val logList = mutableListOf<ActivityLog>()
            var addFailErrors = 0

            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = timeoutTime
                connection.readTimeout = timeoutTime
                connection.doInput = true
                connection.useCaches = false

                responseCode = connection.responseCode
                Log.d("PlantSpeciesLogService","get_activity_logs responseCode: ${responseCode}")
                val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }
                Log.d("PlantSpeciesLogService","get_activity_logs responseMessage: ${responseMessage}")

                if (responseCode in 200..299) {
                    if (isList) {
                        val jsonArray = JSONArray(responseMessage)
                        for (i in 0..<jsonArray.length()) {
                            try {
                                val responseObject = jsonArray.getJSONObject(i)
                                val oneLog = ActivityLog.getFromResponseObject(responseObject)
                                logList.add(oneLog)
                            } catch (e: Error) {
                                addFailErrors += 1
                            }
                        }
                    } else {
                        try {
                            val responseObject = JSONObject(responseMessage)
                            val oneLog = ActivityLog.getFromResponseObject(responseObject)
                            logList.add(oneLog)
                        } catch (e: Error) {
                            addFailErrors += 1
                        }
                    }
                }
            } catch (e: Exception) {
                forIntent?.putExtra("exception",e.toString())
            } finally {
                forIntent?.putExtra("responseCode",responseCode)
                forIntent?.putExtra("addFailErrors",addFailErrors)
                forIntent?.putExtra("logList",ArrayList(logList))
                connection.disconnect()
            }


            return logList
        }

        fun getSpecies(id: String?, byName: Boolean = false, forIntent: Intent? = null): MutableList<EdiblePlantSpecies> {
            val isList: Boolean
            val fullUrl: String
            if (id == null|| id == "") {
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
            Log.d("PlantSpeciesLogService","get_species URL: ${fullUrl} isList: ${isList}")
            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            val speciesList = mutableListOf<EdiblePlantSpecies>()
            var responseCode = -1

            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = timeoutTime
                connection.readTimeout = timeoutTime
                connection.doInput = true
                connection.useCaches = false

                responseCode = connection.responseCode
                Log.d("PlantSpeciesLogService","get_species responseCode: ${responseCode}")
                val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }
                //Log.d("PlantSpeciesLogService","get_species responseMessage: ${responseMessage}")

                if (responseCode in  200..299) {
                    if (isList) {
                        Log.d("PlantSpeciesLogService","get_species Converting to json array")
                        val jsonArray = JSONArray(responseMessage)
                        Log.d("PlantSpeciesLogService","get_species Array size: ${jsonArray.length()}")
                        for (i in 0..<jsonArray.length()) {
                            Log.d("PlantSpeciesLogService","get_species Return No. ${i+1}")
                            Log.d("PlantSpeciesLogService","get_species Reading Response Object")
                            val responseObject = jsonArray.getJSONObject(i)
                            val oneSpecies = EdiblePlantSpecies.getFromResponseObject(responseObject)
                            speciesList.add(oneSpecies)
                        }
                    } else {
                        Log.d("PlantSpeciesLogService","get_species Reading Response Object")
                        val responseObject = JSONObject(responseMessage)
                        val oneSpecies = EdiblePlantSpecies.getFromResponseObject(responseObject)
                        speciesList.add(oneSpecies)
                    }
                }
            } catch (e: Exception) {
                forIntent?.putExtra("exception",e.toString())
            } finally {
                connection.disconnect()
                forIntent?.putExtra("responseCode",responseCode)
                forIntent?.putExtra("speciesList",ArrayList(speciesList))
            }
            return speciesList
        }

        fun getPlants(id: String, byUser: Boolean = false, forIntent: Intent? = null): MutableList<Plant> {
            val isList: Boolean
            val fullUrl: String
            if (byUser) {
                fullUrl = "${startUrl}/Plants/Users/${id}"
                isList = true
                forIntent?.setAction("get_plants_byuser")
            } else {
                fullUrl = "${startUrl}/plants/${id}"
                isList = false
                forIntent?.setAction("get_plants")
            }
            Log.d("PlantSpeciesLogService","get_plants URL: ${fullUrl}")
            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            var responseCode = -1
            val plantList = mutableListOf<Plant>()

            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = timeoutTime
                connection.readTimeout = timeoutTime
                connection.doInput = true
                connection.useCaches = false

                responseCode = connection.responseCode
                val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

                Log.d("PlantSpeciesLogService","get_plants Response Code: ${responseCode}")

                if (responseCode in 200..299) {

                    if (isList) {
                        val jsonArray = JSONArray(responseMessage)
                        for (i in 0..<jsonArray.length()) {
                            val responseObject = jsonArray.getJSONObject(i)
                            val onePlant = Plant.getFromResponseObject(responseObject)
                            plantList.add(onePlant)
                        }
                    } else {
                        val responseObject = JSONObject(responseMessage)
                        val onePlant = Plant.getFromResponseObject(responseObject)
                        plantList.add(onePlant)
                    }
                }
                Log.d("PlantSpeciesLogService","get_plants Array Size: ${plantList.size}")
            } catch (e: Exception) {
                forIntent?.putExtra("exception",e.toString())
            } finally {
                connection.disconnect()
                forIntent?.putExtra("responseCode",responseCode)
                forIntent?.putExtra("plantList",ArrayList(plantList))
            }
            return plantList
        }


        fun getReminders(id: String, byUser: Boolean = false, forIntent: Intent? = null): MutableList<Reminder> {
            val isList: Boolean
            val fullUrl: String
            if (byUser) {
                fullUrl = "${startUrl}/Reminders/user/${id}"
                isList = true
                forIntent?.setAction("get_reminders_byuser")
            } else {
                fullUrl = "${startUrl}/Reminders/${id}"
                isList = false
                forIntent?.setAction("get_reminders")
            }
            Log.d("PlantSpeciesLogService","get_reminders URL: ${fullUrl}")
            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            var responseCode = -1
            val reminderList = mutableListOf<Reminder>()

            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = timeoutTime
                connection.readTimeout = timeoutTime
                connection.doInput = true
                connection.useCaches = false

                responseCode = connection.responseCode
                val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

                Log.d("PlantSpeciesLogService","get_reminders Response Code: ${responseCode}")

                if (responseCode in 200..299) {

                    if (isList) {
                        val jsonArray = JSONArray(responseMessage)
                        for (i in 0..<jsonArray.length()) {
                            val responseObject = jsonArray.getJSONObject(i)
                            val oneReminder = Reminder.getFromResponseObject(responseObject)
                            reminderList.add(oneReminder)
                        }
                    } else {
                        val responseObject = JSONObject(responseMessage)
                        val oneReminder = Reminder.getFromResponseObject(responseObject)
                        reminderList.add(oneReminder)
                    }
                }
                Log.d("PlantSpeciesLogService","get_reminders Array Size: ${reminderList.size}")
            } catch (e: Exception) {
                forIntent?.putExtra("exception",e.toString())
            } finally {
                connection.disconnect()
                forIntent?.putExtra("responseCode",responseCode)
                forIntent?.putExtra("reminderList",ArrayList(reminderList))
            }
            return reminderList
        }


        fun createOrUpdateReminder(theReminder: Reminder, isUpdate: Boolean, forIntent: Intent?, sessionCookie: String): Reminder? {
            // url for adding and updating situation
            val fullUrl: String = if (isUpdate) {"${startUrl}/Reminders/${theReminder.id}"} else {"${startUrl}/Reminders"}
            val action: String
            if (isUpdate) {
                action = "update_reminder"
            } else {
                action = "create_reminder"
            }
            forIntent?.setAction(action)

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            Log.d("PlantSpeciesLogService","${action} URL: ${fullUrl}")

            var returnReminder: Reminder? = null
            var responseCode = -1
            var finalCookie = sessionCookie
            Log.d("PlantSpeciesLogService","${action} Cookie: ${finalCookie}")

            try {
                connection.requestMethod = if (isUpdate) {"PUT"} else {"POST"}
                connection.connectTimeout = timeoutTime
                connection.readTimeout = timeoutTime
                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false
                CookieHandling.setSessionCookie(connection,sessionCookie)

                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")

                val reminderJson = JSONObject().apply {
                    put("id", theReminder.id)
                    put("userId", theReminder.userId)
                    put("plantId", theReminder.plantId)
                    put("reminderType", theReminder.reminderType)
                    put("reminderDateTime", theReminder.reminderDateTime)
                    put("isRecurring", theReminder.isRecurring)
                    put("recurrenceInterval", theReminder.recurrenceInterval)
                    put("status", theReminder.status)
                    put("createdDateTime", theReminder.createdDateTime)
                }

                val outputStream = DataOutputStream(connection.outputStream)
                outputStream.writeBytes(reminderJson.toString())
                outputStream.flush()
                outputStream.close()

                responseCode = connection.responseCode
                forIntent?.putExtra("responseCode",responseCode)
                Log.d("PlantSpeciesLogService","${action} Response Code: ${responseCode}")

                if (responseCode in 200..299) {
                    val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }
                    val responseObject = JSONObject(responseMessage)
                    returnReminder = Reminder.getFromResponseObject(responseObject)
                    finalCookie = CookieHandling.extractSessionCookie(connection)
                    Log.d("PlantSpeciesLogService","${action} Updated Cookie: ${finalCookie}")
                }
            } catch (e: Exception) {
                forIntent?.putExtra("exception",e.toString())
                Log.d("PlantSpeciesLogService","${action} error: ${e.toString()}")
            } finally {
                forIntent?.putExtra("responseCode",responseCode)
                forIntent?.putExtra("reminder",returnReminder)
                if (finalCookie == "") {
                    Log.d("PlantSpeciesLogService","${action} will not update the previous cookie. Passing ${sessionCookie}.")
                    forIntent?.putExtra("sessionCookie",sessionCookie)
                } else {
                    forIntent?.putExtra("sessionCookie",finalCookie)
                }

                connection.disconnect()
            }

            return returnReminder
        }


        fun deleteReminder(id: String, forIntent: Intent?, sessionCookie: String): Int {
            val fullUrl = "${startUrl}/Reminders/${id}"

            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "DELETE"
            forIntent?.setAction("delete_reminder")
            var responseCode = -1
            var finalCookie = sessionCookie
            Log.d("PlantSpeciesLogService","delete_reminder Cookie: ${finalCookie}")

            try {
                connection.connectTimeout = timeoutTime
                connection.readTimeout = timeoutTime
                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false
                CookieHandling.setSessionCookie(connection,sessionCookie)

                val outputStream = DataOutputStream(connection.outputStream)

                outputStream.flush()
                outputStream.close()

                responseCode = connection.responseCode
                //val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

            } catch (e: Exception) {
                forIntent?.putExtra("exception",e.toString())
                Log.d("PlantSpeciesLogService","delete_reminder error: ${e.toString()}")
            } finally {
                connection.disconnect()
                forIntent?.putExtra("responseCode",responseCode)
                if (finalCookie == "") {
                    Log.d("PlantSpeciesLogService","delete_reminder will not update the previous cookie. Passing ${sessionCookie}.")
                    forIntent?.putExtra("sessionCookie",sessionCookie)
                } else {
                    forIntent?.putExtra("sessionCookie",finalCookie)
                }
            }
            return responseCode
        }
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val returnIntent = Intent()
        Log.d("PlantSpeciesLogService","start intent action name: ${intent.action}")
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
            Log.d("PlantSpeciesLogService","get_plants $id $byUser")
            Thread{
                getPlants(id,byUser,returnIntent)
                Log.d("PlantSpeciesLogService","get_plants completed")
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("change_plant")) {
            // add or update plants
            val thePlant = intent.getSerializableExtra("plant") as? Plant
            val isUpdate: Boolean = intent.getBooleanExtra("isUpdate",false)
            val sessionCookie: String = HandleNulls.ifNullString(intent.getStringExtra("sessionCookie"))
            Thread{
                createOrUpdatePlant(thePlant!!,isUpdate,returnIntent,sessionCookie)
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("delete_plant")) {
            val idTry: String? = intent.getStringExtra("id")
            val id: String
            id = if (idTry == null) {""} else {idTry}
            val sessionCookie: String = HandleNulls.ifNullString(intent.getStringExtra("sessionCookie"))
            Thread{
                //val sessionCookie: String = HandleNulls.ifNullString(intent.getStringExtra("sessionCookie"))
                deletePlant(id,returnIntent,sessionCookie)
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
            val sessionCookie: String = HandleNulls.ifNullString(intent.getStringExtra("sessionCookie"))
            Thread{
                createOrUpdateLog(theLog!!,isUpdate,returnIntent,sessionCookie)
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("delete_activity_log")) {
            val idTry: String? = intent.getStringExtra("id")
            val id: String
            id = if (idTry == null) {""} else {idTry}
            val sessionCookie: String = HandleNulls.ifNullString(intent.getStringExtra("sessionCookie"))
            Thread{
                deleteLog(id,returnIntent,sessionCookie)
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("get_reminders")) {
            // get reminders
            val idTry: String? = intent.getStringExtra("id")
            val id: String
            id = if (idTry == null) {""} else {idTry}
            val byUser: Boolean = intent.getBooleanExtra("byUser",false)
            Log.d("PlantSpeciesLogService","get_plants $id $byUser")
            Thread{
                getReminders(id,byUser,returnIntent)
                Log.d("PlantSpeciesLogService","get_plants completed")
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("change_reminder")) {
            // add or update reminders
            val theReminder = intent.getSerializableExtra("reminder") as? Reminder
            val isUpdate: Boolean = intent.getBooleanExtra("isUpdate",false)
            val sessionCookie: String = HandleNulls.ifNullString(intent.getStringExtra("sessionCookie"))
            Thread{
                createOrUpdateReminder(theReminder!!,isUpdate,returnIntent,sessionCookie)
                sendBroadcast(returnIntent)
            }.start()
        } else if (intent.action.equals("delete_reminder")) {
            val idTry: String? = intent.getStringExtra("id")
            val id: String
            id = if (idTry == null) {""} else {idTry}
            val sessionCookie: String = HandleNulls.ifNullString(intent.getStringExtra("sessionCookie"))
            Thread{
                //val sessionCookie: String = HandleNulls.ifNullString(intent.getStringExtra("sessionCookie"))
                deleteReminder(id,returnIntent,sessionCookie)
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
