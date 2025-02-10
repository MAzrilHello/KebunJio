package iss.nus.edu.sg.sa4106.kebunjio.features.logactivities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iss.nus.edu.sg.sa4106.kebunjio.DummyData
import iss.nus.edu.sg.sa4106.kebunjio.HandleNulls
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.TimeClassHandler
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityLogActivitiesBinding
import iss.nus.edu.sg.sa4106.kebunjio.service.PlantSpeciesLogService

class LogActivitiesActivity : AppCompatActivity() {

    private var updateLogId: String? = null
    private var updateUserId: String? = null
    private var sessionCookie: String = ""

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

    private var logActTypes: MutableList<String> = mutableListOf("","Water","Fertilize","Harvest","Withered")


    // for communicating with spring boot api
    protected var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val responseCode = intent.getIntExtra("responseCode",-2)
            if (responseCode in 200..299) {
                Log.d("LogActivitiesActivity","${action} successful: ${responseCode}")
            } else if (responseCode == -1) {
                Log.d("LogActivitiesActivity","${action}: response error: ${intent.getStringExtra("exception")}")
                makeToast("Error in adding/ updating: ${responseCode}")
                return
            } else {
                Log.d("LogActivitiesActivity","${action}: response with no error: ${responseCode}")
                makeToast("Error in adding/ updating: ${responseCode}")
                return
            }
            if (action == "create_activity_log") {
                makeToast("Successfully logged activity")
                goBack(true)
            } else if (action == "update_activity_log") {
                makeToast("Successfully updated log")
                goBack(true)

            }
        }
    }


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

        val logActAdapter = ArrayAdapter(this,
                                    android.R.layout.simple_spinner_item,
                                    logActTypes)
        activityTypeSpinner.adapter = logActAdapter

        binding.backBtn.setOnClickListener {
            goBack(false)
        }

        logActivitiesBtn.setOnClickListener {
            logNewActivity()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initReceiver()

        // get data from intent
        updateUserId = intent.getStringExtra("userId")
        sessionCookie = HandleNulls.ifNullString(intent.getStringExtra("sessionCookie"))
        val plantIdToNameDict = (intent.getSerializableExtra("plantIdToNameDict") as HashMap<String, String>)!!
        setupPlantSpinner(plantIdToNameDict)
        if (intent.getBooleanExtra("update",false)) {
            binding.titlePart.text = "Update Log"
            binding.logActivitiesBtn.text = "Update Log"
            val currentActivityLog = intent.getSerializableExtra("currentActivityLog") as ActivityLog
            setData(currentActivityLog)
        }
    }


    private fun setupPlantSpinner(plantIdToNameDict: HashMap<String, String>) {
        plantSpinnerIdxToId.clear()
        plantSpinnerIdxToId.add("")
        val spinnerOptions = mutableListOf<String>("")
        for ( key in plantIdToNameDict.keys) {
            plantSpinnerIdxToId.add(key)
            spinnerOptions.add(plantIdToNameDict[key]!!)
        }
        val spinAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item,
            spinnerOptions)
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        plantSpinner.adapter = spinAdapter
    }


    protected fun initReceiver() {
        val filter = IntentFilter()
        filter.addAction("create_activity_log")
        filter.addAction("update_activity_log")
        ContextCompat.registerReceiver(this, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
        Log.d("LogActivitiesActivity","initReceiver completed")
    }


    public fun setData(actLog: ActivityLog) {
        updateLogId = actLog.id
        Log.d("LogActivitiesActivity","plantId: ${actLog.plantId}")
        var idx = plantSpinnerIdxToId.indexOf(actLog.plantId)
        if (idx != -1) {
            plantSpinner.setSelection(plantSpinnerIdxToId.indexOf(actLog.plantId))
        } else {
            plantSpinner.setSelection(0)
        }
        Log.d("LogActivitiesActivity","Activity Type: ${actLog.activityType}")
        idx = logActTypes.indexOf(actLog.activityType)
        if (idx != -1) {
            activityTypeSpinner.setSelection(idx)
        } else {
            activityTypeSpinner.setSelection(0)
        }
        activityDescText.setText(actLog.activityDescription)
        timeStampText.text = actLog.timestamp
        if (timeStampText.text.toString() == "null") {
            timeStampText.text = ""
        }
    }


    private fun logNewActivity() {
        var logId = "" // Must assign a proper id later
        if (updateLogId!=null){
            logId = updateLogId!!
        }
        var userId = ""
        if (updateUserId!=null) {
            userId = updateUserId!! // must assign a proper id later
        }
        val plantId: String = plantSpinnerIdxToId[plantSpinner.selectedItemPosition]
        val activityType = activityTypeSpinner.selectedItem.toString()
        val activityDesc = activityDescText.text.toString()
        val timestamp = timeStampText.text.toString()
        // check that all values are good
        if (activityType == "" || timestamp == "") {
            makeToast("Please ensure name and planted date are filled")
            return
        }
        var actLog = ActivityLog(logId,userId,plantId,activityType,activityDesc,timestamp)
        // TODO: log the new activity
        val intent = Intent(this, PlantSpeciesLogService::class.java)
        intent.setAction("change_activity_log")
        intent.putExtra("activityLog",actLog)
        intent.putExtra("isUpdate",logId != "")
        intent.putExtra("sessionCookie",sessionCookie)
        this.startService(intent)
    }


    private fun makeToast(text: String,length: Int = Toast.LENGTH_LONG) {
        val msg = Toast.makeText(
            this,
            text, length
        )
        msg.show()
    }


    private fun goBack(haveUpdate: Boolean) {
        val response = Intent()
        setResult(RESULT_OK, response)
        response.putExtra("haveUpdate",haveUpdate)
        finish()
    }

}