package iss.nus.edu.sg.sa4106.kebunjio.data

data class EdiblePlantSpecies(
    val id: String,
    val name: String,
    val scientificName: String,
    val ediblePlantGroup: String,
    val description: String,
    val wateringTips: String,
    val sunlight: String,
    val soilType: String,
    val harvestTime: String,
    val commonPests: String,
    val growingSpace: String,
    val fertilizerTips: String,
    val specialNeeds: String,
    val imageURL: String
)
