package iss.nus.edu.sg.sa4106.kebunjio.features.logactivities

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iss.nus.edu.sg.sa4106.kebunjio.DummyData
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.TimeClassHandler
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityLogActivitiesBinding

class LogActivitiesActivity : AppCompatActivity() {

    private var currentUserId: String? = ""

    private var updateLogId: String? = null

    // for ui
    private var _binding: ActivityLogActivitiesBinding? = null
    private val binding get() = _binding!!

    lateinit var timeStampHandler: TimeClassHandler
    lateinit var timeStampText: TextView
    lateinit var changeDateBtn: Button
    lateinit var changeTimeBtn: Button

    lateinit var logActivitiesBtn: Button
    lateinit var activityTypeSpinner: Spinner
    lateinit var activityDescText: EditText
    lateinit var plantSpinner: Spinner
    private var plantSpinnerIdxToId = mutableListOf<String>()
    private var dummyData: DummyData = DummyData()

    lateinit var logActTypes: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityLogActivitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timeStampText = binding.timeStampText
        changeDateBtn = binding.changeDateBtn
        changeTimeBtn = binding.changeTimeBtn

        timeStampHandler = TimeClassHandler(timeStampText,changeDateBtn,changeTimeBtn,this)

        activityTypeSpinner = binding.activityTypeSpinner
        activityDescText = binding.activityDescText
        logActivitiesBtn = binding.logActivitiesBtn
        plantSpinner = binding.plantSpinner

        logActTypes = mutableListOf<String>()
        logActTypes.add("Water")
        logActTypes.add("Fertilize")
        logActTypes.add("Harvest")
        logActTypes.add("Withered")

        val logActAdapter = ArrayAdapter(this,
                                    android.R.layout.simple_spinner_item,
                                    logActTypes)
        activityTypeSpinner.adapter = logActAdapter

        logActivitiesBtn.setOnClickListener {
            logNewActivity()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // get the current user id
        currentUserId = intent.getStringExtra("userId")
        Log.d("LogActivitiesActivity","userId: ${currentUserId}")
        if (currentUserId != null) {
            setUserPlants(currentUserId!!)
        }
        if (intent.getBooleanExtra("update",false)) {
            Log.d("LogActivitiesActivity","We are in update mode")
            binding.titlePart.text = "Update Activity Log"
            binding.logActivitiesBtn.text = "Update Log"
            val logId = intent.getStringExtra("logId")
            Log.d("LogActivitiesActivity","logId: ${logId}")
            if (logId != null) {
                val chosenActLog = dummyData.getActivityLogById(logId)
                if (chosenActLog != null) {
                    setData(chosenActLog)
                }
            }


        }
    }


    private fun setUserPlants(id: String) {
        plantSpinnerIdxToId.clear()
        plantSpinnerIdxToId.add("")
        val userPlants = dummyData.getUserPlants(id)
        val userPlantNames: MutableList<String> = mutableListOf<String>("NO PLANT")
        for (i in 0..userPlants.size-1) {
            userPlantNames.add(userPlants[i].name)
            plantSpinnerIdxToId.add(userPlants[i].id)
        }
        val plantAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            userPlantNames)
        plantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        plantSpinner.adapter = plantAdapter
    }


    public fun setData(actLog: ActivityLog) {
        updateLogId = actLog.id
        // Never set the user in setData
        //currentUserId = actLog.userId
        Log.d("LogActivitiesActivity","plantId: ${actLog.plantId}")
        if (actLog.plantId!=null) {
            plantSpinner.setSelection(plantSpinnerIdxToId.indexOf(actLog.plantId))
        } else {
            plantSpinner.setSelection(0)
        }
        Log.d("LogActivitiesActivity","Activity Type: ${actLog.activityType}")
        activityTypeSpinner.setSelection(logActTypes.indexOf(actLog.activityType))
        activityDescText.setText(actLog.activityDescription)
        timeStampText.text = actLog.timestamp
    }


    private fun logNewActivity() {
        var logId = "" // Must assign a proper id later
        if (updateLogId!=null){
            logId = updateLogId!!
        }
        var userId = ""
        if (currentUserId!=null) {
            userId = currentUserId!! // must assign a proper id later
        }
        var plantId: String? = null // must assign a proper id later
        if (plantSpinner.selectedItemPosition > 0) {
            plantId = plantSpinnerIdxToId[plantSpinner.selectedItemPosition]
        }
        val activityType = activityTypeSpinner.selectedItem.toString()
        val activityDesc = activityDescText.text.toString()
        val timeStamp = timeStampText.text.toString()

        var actLog = ActivityLog(logId,userId,plantId,activityType,activityDesc,timeStamp)
        // TODO: log the new activity
    }

}