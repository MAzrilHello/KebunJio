package iss.nus.edu.sg.sa4106.kebunjio.features.reminders;

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
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ViewPlantToChooseForReminderBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity
import iss.nus.edu.sg.sa4106.kebunjio.service.DownloadImageService
import iss.nus.edu.sg.sa4106.kebunjio.service.PlantSpeciesLogService
import java.io.File


class PlantToChooseForReminderAdapter(private val context: Context,
                                      protected var loggedInFragment: LoggedInFragment,
                                      protected var haveUpdateLauncher: ActivityResultLauncher<Intent>,
                                      protected var sessionCookie: String,
                                      protected var userId: String,
                                      protected var usersPlantList: ArrayList<Plant>
                                      //protected var speciesIdToNameDict: HashMap<String, String>,
                                      //protected var usersActivityLogList: ArrayList<ActivityLog>
        ): ArrayAdapter<Any?>(context, R.layout.view_plant_to_choose) {


    init {
        addAll(*arrayOfNulls<Any>(usersPlantList.size))
    }

    public fun resetData(userId: String,
                         usersPlantList: ArrayList<Plant>,
                         //speciesIdToNameDict: HashMap<String, String>,
                         //usersActivityLogList: ArrayList<ActivityLog>
                        ){
        this.userId = userId
        //this.usersPlantList = usersPlantList
        this.usersPlantList.clear()
        this.usersPlantList.addAll(usersPlantList)
        //this.speciesIdToNameDict = speciesIdToNameDict
        //this.speciesIdToNameDict.clear()
        //this.speciesIdToNameDict.putAll(speciesIdToNameDict)
        //this.usersActivityLogList = usersActivityLogList
        //this.usersActivityLogList.clear()
        //this.usersActivityLogList.addAll(usersActivityLogList)
        Log.d("ForReminderAdapter","notifyDataSetChanged size ${this.usersPlantList.size}")
        notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var _view = view
        var binding: ViewPlantToChooseForReminderBinding
        if (_view == null) {
            val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // if we are not responsible for adding the view to the parent,
            // then attachToRoot should be 'false' (which is in our case)
            //_view = inflater.inflate(R.layout.view_plant_to_choose, parent, false)
            binding = ViewPlantToChooseForReminderBinding.inflate(inflater,parent,false)
            _view = binding.root
        } else {
            binding = ViewPlantToChooseForReminderBinding.bind(_view)
        }
        val showPlantName = binding.plantNameChooseText
        val viewPlantBtn = binding.viewPlantBtn
        val editPlantBtn = binding.editPlantBtn
        val deletePlantBtn = binding.deletePlantBtn

        // hide view if it's data does not exist
        if (position >= usersPlantList.size) {
            _view.visibility = View.GONE
            return _view
        }

        var currentPlant = usersPlantList[position]
        Log.d("ForReminderAdapter","For Reminder Position ${position}'s TextView: ${currentPlant.name}")

        showPlantName.text = currentPlant.name



        viewPlantBtn.setOnClickListener{
            //val intent = Intent(getContext(), ViewPlantDetailsActivity::class.java)
            //intent.putExtra("haveData", true)
            //intent.putExtra("currentPlant", currentPlant)
            //intent.putExtra("speciesIdToNameDict",speciesIdToNameDict)
            //val thisActLog: ArrayList<ActivityLog> = arrayListOf()
            //for (i in 0..this.usersActivityLogList.size-1) {
            //    if (this.usersActivityLogList[i].plantId == currentPlant.id) {
            //        thisActLog.add(this.usersActivityLogList[i])
            //    }
            //}
            //intent.putExtra("thisActivityLog",thisActLog)
            //getContext().startActivity(intent)
        }

        editPlantBtn.setOnClickListener{
            //val intent = Intent(getContext(), AddPlantActivity::class.java)
            //intent.putExtra("userId",userId)
            //intent.putExtra("speciesIdToNameDict",speciesIdToNameDict)
            //intent.putExtra("sessionCookie",sessionCookie)
            //intent.putExtra("currentPlant",currentPlant)
            //intent.putExtra("update", true)
            //haveUpdateLauncher.launch(intent)
        }

        deletePlantBtn.setOnClickListener{
            //Thread {
            //    val deleteStatus = PlantSpeciesLogService.deletePlant(currentPlant.id,null,sessionCookie)
            //    if (deleteStatus in 200..299) {

            //        loggedInFragment.activity?.runOnUiThread{
            //            loggedInFragment.makeToast("Deleted plant successfully")
                        //showPlantName.visibility = View.GONE
                        //viewPlantBtn.visibility = View.GONE
                        //editPlantBtn.visibility = View.GONE
                        //deletePlantBtn.visibility = View.GONE
                        //_view.visibility = View.GONE
            //            loggedInFragment.tryPullAllUserPlants()
            //        }
            //    }
            //}.start()
        }

        return _view
    }

    public fun invalidateCookies() {
        this.sessionCookie = ""
    }

}