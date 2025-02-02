package iss.nus.edu.sg.sa4106.kebunjio

import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.data.EdiblePlantSpecies
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant

class DummyData {
    
    val SpeciesDummy:MutableList<EdiblePlantSpecies> = mutableListOf<EdiblePlantSpecies>()
    val PlantDummy: MutableList<Plant> = mutableListOf<Plant>()
    val ActivityLogDummy: MutableList<ActivityLog> = mutableListOf<ActivityLog>()

    init {
        setupSpeciesDummy()
        setupPlantDummy()
        setupLogDummy()
    }


    private fun setupLogDummy() {
        val log1 = ActivityLog(0,0,0,"Water","500ml","01/01/2020 15:30")
        ActivityLogDummy.add(log1)
        val log2 = ActivityLog(0,0,0,"Water","500ml","02/01/2020 15:26")
        ActivityLogDummy.add(log2)
        val log3 = ActivityLog(0,0,1,"Fertiliser","500ml","02/01/2020 15:28")
        ActivityLogDummy.add(log3)
    }


    public fun getPlantLogs(plantId: Int): MutableList<ActivityLog> {
        val toReturn = mutableListOf<ActivityLog>()
        for (i in 0..ActivityLogDummy.size-1) {
            if (ActivityLogDummy[i].plantId == plantId) {
                toReturn.add(ActivityLogDummy[i])
            }
        }
        return toReturn
    }


    public fun getUserLogs(userId: Int): MutableList<ActivityLog> {
        val toReturn = mutableListOf<ActivityLog>()
        for (i in 0..ActivityLogDummy.size-1) {
            if (ActivityLogDummy[i].userId == userId) {
                toReturn.add(ActivityLogDummy[i])
            }
        }
        return toReturn
    }


    private fun setupPlantDummy() {
        val plant1 = Plant(0,0,0,"User 0 Type 0")
        PlantDummy.add(plant1)
        val plant2 = Plant(1,1,0,"User 0 Type 1")
        PlantDummy.add(plant2)
        val plant3 = Plant(2,1,1,"Plant 1 Type 1")
        PlantDummy.add(plant3)
        val plant4 = Plant(3,2,1,"User 1 Type 2")
        PlantDummy.add(plant4)
    }


    public fun getUserPlants(userId: Int): MutableList<Plant> {
        val toReturn = mutableListOf<Plant>()
        for (i in 0..PlantDummy.size-1) {
            if (PlantDummy[i].userId == userId) {
                toReturn.add(PlantDummy[i])
            }
        }
        return toReturn
    }


    private fun setupSpeciesDummy() {
        val species1 = EdiblePlantSpecies(
            0,
            "Bang Kwang",
            "Pachyrhizus erosus",
            "Group 1",
            "Bang Kwang is a large, climbing vine that is harvested for its starchy tuber.",
            "Bang Kwang watering tips",
            "Bang Kwang sunlight",
            "Bang Kwang soil type",
            "Bang Kwang harvest time",
            "Bang Kwang pests",
            "Bang Kwang growing space",
            "Bang Kwang fertilizer tips",
            "Bang Kwang special needs",
            "https://gardeningsg.nparks.gov.sg/images/Hardscapes/Trellis_JacChua.jpg")
        SpeciesDummy.add(species1)
        val species2 = EdiblePlantSpecies(
            1,
            "Xiao Bai Cai",
            "Brassica rapa Pak Choi Group",
            "Group 2",
            "Xiao Bai Cai is a popular leafy vegetable used in Chinese cooking, and is typically stir-fired, blanched, or steamed.",
            "Xiao Bai Cai watering tips",
            "Xiao Bai Cai sunlight",
            "Xiao Bai Cai soil type",
            "Xiao Bai Cai harvest time",
            "Xiao Bai Cai pests",
            "Xiao Bai Cai growing space",
            "Xiao Bai Cai fertilizer tips",
            "Xiao Bai Cai special needs",
            "https://gardeningsg.nparks.gov.sg/images/Plants/XiaoBaiCai_JacChua%20(1).jpg")
        SpeciesDummy.add(species2)
        val species3 = EdiblePlantSpecies(
            2,
            "Winter Melon",
            "Benincasa hispida",
            "Group 3",
            "Winter Melons are one of the most impressive crops an edible gardener can grow, with singular fruit growing as heavy as 30kg!",
            "Winter Melon watering tips",
            "Winter Melon sunlight",
            "Winter Melon soil type",
            "Winter Melon harvest time",
            "Winter Melon pests",
            "Winter Melon growing space",
            "Winter Melon fertilizer tips",
            "Winter Melon special needs",
            "https://gardeningsg.nparks.gov.sg/images/Gardeners/Harvesting%20(8).jpg")
        SpeciesDummy.add(species3)
    }


    public fun nameList(): MutableList<String> {
        var theList = mutableListOf<String>()
        for (i in 0..SpeciesDummy.size-1) {
            theList.add(SpeciesDummy[i].name)
        }
        return theList
    }

    public fun urlList(): MutableList<String> {
        var theList = mutableListOf<String>()
        for (i in 0..SpeciesDummy.size-1) {
            theList.add(SpeciesDummy[i].imageURL)
        }
        return theList
    }

    public fun idList(): MutableList<Int> {
        var theList = mutableListOf<Int>()
        for (i in 0..SpeciesDummy.size-1) {
            theList.add(SpeciesDummy[i].ediblePlantSpeciesId)
        }
        return theList
    }

}