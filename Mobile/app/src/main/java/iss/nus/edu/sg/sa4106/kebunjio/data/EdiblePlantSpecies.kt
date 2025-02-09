package iss.nus.edu.sg.sa4106.kebunjio.data

import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullString
import org.json.JSONObject
import java.io.Serializable

data class EdiblePlantSpecies(
    val id: String,
    val name: String,
    val scientificName: String,
    val description: String,
    val ediblePlantGroup: String,
    val wateringTips: String,
    val sunlight: String,
    val soilType: String,
    val harvestTime: String,
    val commonPests: String,
    val growingSpace: String,
    val fertilizerTips: String,
    val specialNeeds: String,
    val imageURL: String
): Serializable {
    companion object {

        fun getFromResponseObject(responseObject: JSONObject): EdiblePlantSpecies {
            val id = ifNullString(responseObject.getString("id"))
            val name = ifNullString(responseObject.getString("name"))
            val scientificName = ifNullString(responseObject.getString("scientificName"))
            val description = ifNullString(responseObject.getString("description"))
            var ediblePlantGroup: String = ""
            if (responseObject.has("ediblePlantGroup")) {
                ediblePlantGroup = ifNullString(responseObject.getString("ediblePlantGroup"))
            }
            val wateringTips = ifNullString(responseObject.getString("wateringTips"))
            val sunlight = ifNullString(responseObject.getString("sunlight"))
            val soilType = ifNullString(responseObject.getString("soilType"))
            val harvestTime = ifNullString(responseObject.getString("harvestTime"))
            val commonPests = ifNullString(responseObject.getString("commonPests"))
            val growingSpace = ifNullString(responseObject.getString("growingSpace"))
            val fertilizerTips = ifNullString(responseObject.getString("fertilizerTips"))
            val specialNeeds = ifNullString(responseObject.getString("specialNeeds"))
            val imageURL = ifNullString(responseObject.getString("imageUrl"))
            return EdiblePlantSpecies(id,
                name,
                scientificName,
                description,
                ediblePlantGroup,
                wateringTips,
                sunlight,
                soilType,
                harvestTime,
                commonPests,
                growingSpace,
                fertilizerTips,
                specialNeeds,
                imageURL)
        }
    }
}
