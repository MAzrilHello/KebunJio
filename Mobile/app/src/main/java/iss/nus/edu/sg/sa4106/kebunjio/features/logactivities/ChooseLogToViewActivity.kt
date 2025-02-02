package iss.nus.edu.sg.sa4106.kebunjio.features.logactivities

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityChooseLogToViewBinding

// for testing
import iss.nus.edu.sg.sa4106.kebunjio.DummyData
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity

class ChooseLogToViewActivity : AppCompatActivity() {

    private var _binding: ActivityChooseLogToViewBinding? = null
    private val binding get() = _binding!!
    private val dummy = DummyData()
    private val userId = 0

    lateinit var logToViewText: TextView
    lateinit var actLogList: ListView
    lateinit var addFAB: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        _binding = ActivityChooseLogToViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logToViewText = binding.logToViewText
        // get the species list view
        actLogList = binding.actLogList
        addFAB = binding.addFab

        //val userPlants = dummy.getUserPlants(0)
        //val idList: MutableList<Int> = mutableListOf<Int>()
        //val nameList: MutableList<String> = mutableListOf<String>()

        //for (i in 0..userPlants.size-1) {
        //    idList.add(userPlants[i].plantId)
        //    nameList.add(userPlants[i].name)
        //}

        val userActLogList = dummy.getUserLogs(userId)
        val plantList = dummy.PlantDummy
        val plantIdToName: MutableMap<Int, String> = mutableMapOf<Int, String>()

        for (i in 0..userActLogList.size-1) {
            var plantId = userActLogList[i].plantId
            if (plantId != null) {
                plantIdToName[plantId] = plantList[plantId].name
            }
        }

        if (userActLogList.size==0) {
            logToViewText.text = "No activity log"
        } else {
            logToViewText.text = "Choose activity log"
        }

        addFAB.setOnClickListener {
            val intent = Intent(this,LogActivitiesActivity::class.java)
            this.startActivity(intent)
        }

        actLogList.adapter = LogToChooseAdapter(this,userActLogList,plantIdToName)
    }
}