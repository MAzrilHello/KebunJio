package iss.nus.edu.sg.sa4106.kebunjio.features.logactivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityChooseLogToViewBinding

// for testing
import iss.nus.edu.sg.sa4106.kebunjio.DummyData
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity

class ChooseLogToViewFragment : Fragment() {

    private var _binding: ActivityChooseLogToViewBinding? = null
    private val binding get() = _binding!!
    //private val dummy = DummyData()
    private var userId = "a"
    private var usersActivityLogList: ArrayList<ActivityLog> = ArrayList()
    private var plantIdToNameDict: HashMap<String, String> = hashMapOf()

    lateinit var logToViewText: TextView
    lateinit var actLogList: ListView
    lateinit var addFAB: FloatingActionButton


    fun loadNewData(userId: String, plantIdToNameDict: HashMap<String, String>, usersActivityLogList: ArrayList<ActivityLog>){
        this.userId = userId
        this.plantIdToNameDict = plantIdToNameDict
        this.usersActivityLogList = usersActivityLogList
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityChooseLogToViewBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

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

        //val userActLogList = dummy.getUserLogs(userId)
        //val plantList = dummy.PlantDummy
        val plantIdToName: HashMap<String, String> = hashMapOf<String, String>()

        //for (i in 0..userActLogList.size-1) {
        //    Log.d("ChooseLogToViewActivity","plantId for logId ${userActLogList[i].id}: ${userActLogList[i].plantId}")
        //    var plantId = userActLogList[i].plantId
        //    if (plantId == null) {
        //    } else if (plantIdToName.contains(plantId)) {
        //    } else {
        //        val plantName = dummy.getPlantById(plantId)!!.name
        //        plantIdToName[plantId] = plantName
        //    }
        //}

        if (usersActivityLogList.size==0) {
            logToViewText.text = "No activity log"
        } else {
            logToViewText.text = "Choose activity log"
        }

        //addFAB.setOnClickListener {
        //    val intent = Intent(requireContext(),LogActivitiesActivity::class.java)
        //    intent.putExtra("userId",userId)
        //    this.startActivity(intent)
        //}

        //actLogList.adapter = LogToChooseAdapter(requireContext(),usersActivityLogList,plantIdToName)
    }
}