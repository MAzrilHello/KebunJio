package iss.nus.edu.sg.sa4106.kebunjio.features.logactivities;

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import iss.nus.edu.sg.sa4106.kebunjio.LoggedInFragment
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ViewActLogToChooseBinding
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ViewPlantToChooseBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity
import iss.nus.edu.sg.sa4106.kebunjio.service.DownloadImageService
import iss.nus.edu.sg.sa4106.kebunjio.service.PlantSpeciesLogService
import java.io.File


class LogToChooseAdapter(private val context: Context,
                         protected var loggedInFragment: LoggedInFragment,
                         protected var haveUpdateLauncher: ActivityResultLauncher<Intent>,
                         protected var sessionCookie: String,
                         protected var userId: String,
                         protected var userActLogList: ArrayList<ActivityLog>,
                         protected var plantIdToNameDict: HashMap<String, String>
        ): ArrayAdapter<Any?>(context, R.layout.view_act_log_to_choose) {


    init {
        addAll(*arrayOfNulls<Any>(userActLogList.size))
    }

    public fun resetData(userId: String,
                         userActLogList: ArrayList<ActivityLog>,
                         plantIdToNameDict: HashMap<String, String>){
        Log.d("ChooseLogToViewFragment","Reset data begins ${userId}, ${userActLogList.size}, ${plantIdToNameDict.size}")
        this.userId = userId
        this.userActLogList.clear()
        this.userActLogList.addAll(userActLogList)
        // this weird method is used here for reasons
        val plantIdToNameDictCopy = HashMap<String,String>(plantIdToNameDict)
        this.plantIdToNameDict.clear()
        for (key in plantIdToNameDictCopy.keys) {
            this.plantIdToNameDict[key] = plantIdToNameDictCopy[key]!!
        }
        //this.plantIdToNameDict.putAll(plantIdToNameDict)

        //Log.d("LogToChooseAdapter","plantIdToNameDict new size: ${this.plantIdToNameDict.size}")
        Log.d("LogToChooseAdapter","notifyDataSetChanged size ${this.userActLogList.size}")
        notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var _view = view
        val binding: ViewActLogToChooseBinding

        if (_view == null) {
            val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // if we are not responsible for adding the view to the parent,
            // then attachToRoot should be 'false' (which is in our case)
            //_view = inflater.inflate(R.layout.view_plant_to_choose, parent, false)
            binding = ViewActLogToChooseBinding.inflate(inflater,parent,false)
            _view = binding.root
        } else {
            binding = ViewActLogToChooseBinding.bind(_view)
        }

        // hide view if it's data does not exist
        if (position >= userActLogList.size) {
            _view.visibility = View.GONE
            return _view
        }

        val positionId = userActLogList[position].id
        val currentActivityLog = userActLogList[position]
        val userId = userActLogList[position].userId
        val activityType = userActLogList[position].activityType
        val timestamp = userActLogList[position].timestamp
        val plantId = userActLogList[position].plantId
        val whichPlantText = binding.whichPlantText
        val activityTypeText = binding.activityTypeText
        val lastTimeText = binding.lastTimeText
        val viewLogBtn = binding.viewLogBtn
        val editLogBtn = binding.editLogBtn
        val deleteLogBtn = binding.deleteLogBtn

        if (plantId != null) {
            whichPlantText.text = plantIdToNameDict[plantId]
        } else {
            whichPlantText.text = ""
        }
        activityTypeText.text = activityType
        lastTimeText.text = timestamp



        viewLogBtn.setOnClickListener{
            //val intent = Intent(getContext(), ViewPlantDetailsActivity::class.java)
            //intent.putExtra("plantId", positionId)
            //getContext().startActivity(intent)
        }

        editLogBtn.setOnClickListener{
            val thisId = positionId
            val intent = Intent(getContext(), LogActivitiesActivity::class.java)
            intent.putExtra("userId",userId)
            intent.putExtra("plantIdToNameDict",plantIdToNameDict)
            intent.putExtra("sessionCookie",sessionCookie)
            intent.putExtra("currentActivityLog",currentActivityLog)
            intent.putExtra("update", true)
            haveUpdateLauncher.launch(intent)
        }

        deleteLogBtn.setOnClickListener{
            Thread {
                val deleteStatus = PlantSpeciesLogService.deleteLog(currentActivityLog.id,null,sessionCookie)
                if (deleteStatus in 200..299) {
                    loggedInFragment.activity?.runOnUiThread{
                        loggedInFragment.makeToast("Deleted activity log successfully")
                        //whichPlantText.visibility = View.GONE
                        //activityTypeText.visibility = View.GONE
                        //lastTimeText.visibility = View.GONE
                        //viewLogBtn.visibility = View.GONE
                        //editLogBtn.visibility = View.GONE
                        //deleteLogBtn.visibility = View.GONE
                        //_view.visibility = View.GONE
                        loggedInFragment.tryPullAllUsersActivities()
                    }
                }
            }.start()
        }

        //showSpeciesImg.setOnClickListener {
        //    Log.d("ChoosePlantAdapter","*** Viewing Position: $position ***")
        //    val thisId = this.storedPlantId[position]
        //    Log.d("ChoosePlantAdapter","thisId: $thisId")
        //    val intent = Intent(getContext(), ViewPlantDetailsActivity::class.java)
        //    intent.putExtra("ediblePlantId", thisId)
        //    getContext().startActivity(intent)
        //}

        return _view
    }

    //protected fun requestImageDL(imgURL: String, position: Int) {
    //    val intent = Intent(getContext(), DownloadImageService::class.java)
    //    intent.setAction("download_file_id")
    //    intent.putExtra("url", imgURL)
    //    intent.putExtra("id", position)
    //    intent.putExtra("returnBitmap",false)
    //    Log.d("ChoosePlantAdapter","URL for position ${position}: ${imgURL}")
    //    getContext().startService(intent)
    //}

    //protected fun initReceiver() {
    //    val filter = IntentFilter()
    //    filter.addAction("download_completed_id")
    //    registerReceiver(getContext(),receiver, filter, ContextCompat.RECEIVER_EXPORTED)
    //}
}