package iss.nus.edu.sg.sa4106.kebunjio.data

import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullBoolean
import java.io.Serializable
import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls.Companion.ifNullString
import org.json.JSONObject

data class Plant(
    val id: String,
    val ediblePlantSpeciesId: String,
    val userId: String,
    val name: String,
    val disease: String,
    val plantedDate: String,
    val harvestStartDate: String,
    val plantHealth: String,
    val harvested: Boolean
) : Serializable {
    public fun dataIsGood(): Boolean {
        return (!ediblePlantSpeciesId.equals("") &&
                !userId.equals("") &&
                !name.equals("") && !plantedDate.equals(""))
    }

    companion object {
        fun getFromResponseObject(responseObject: JSONObject): Plant {
            val id = if (responseObject.has("_id")) {
                ifNullString(responseObject.getString("_id"))
            } else {
                ifNullString(responseObject.getString("id"))
            }
            val ediblePlantSpeciesId = ifNullString(responseObject.getString("ediblePlantSpeciesId"))
            val userId = ifNullString(responseObject.getString("userId"))
            val name = ifNullString(responseObject.getString("name"))
            val disease = ifNullString(responseObject.getString("disease"))
            val plantedDate = ifNullString(responseObject.getString("plantedDate"))
            val harvestStartDate = ifNullString(responseObject.getString("harvestStartDate"))
            val plantHealth = ifNullString(responseObject.getString("plantHealth"))
            val harvested = ifNullBoolean(responseObject.getBoolean("harvested"))
            return Plant(id,ediblePlantSpeciesId,userId,name,disease,plantedDate,harvestStartDate,plantHealth,harvested)
        }
    }
}
