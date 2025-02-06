package iss.nus.edu.sg.sa4106.kebunjio.data

import java.io.Serializable

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
}
