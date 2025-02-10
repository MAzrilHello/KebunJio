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
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ViewActLogToChooseBinding
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ViewPlantToChooseBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity
import iss.nus.edu.sg.sa4106.kebunjio.service.DownloadImageService
import java.io.File


class LogToChooseAdapter(private val context: Context,
                         protected var userActLogList: MutableList<ActivityLog>,
                         protected var plantIdToName: MutableMap<String, String>
        ): ArrayAdapter<Any?>(context, R.layout.view_plant_to_choose) {

    //private var _binding: ViewPlantToChooseBinding? = null
    //private val binding get() = _binding!!

    //lateinit var showSpeciesImg: ImageButton
    //private var showSpeciesImgArray: MutableList<ImageButton> = mutableListOf<ImageButton>()
    //lateinit var showPlantName: TextView
    //lateinit var viewPlantBtn: Button
    //lateinit var deletePlantBtn: Button
    //lateinit var storedLogId:  MutableList<Int>


    init {
        addAll(*arrayOfNulls<Any>(userActLogList.size))
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
        val positionId = userActLogList[position].id
        Log.d("LogToChooseAdapter","do for positionId: ${positionId}")
        val userId = userActLogList[position].userId
        val activityType = userActLogList[position].activityType
        val timestamp = userActLogList[position].timestamp
        val plantId = userActLogList[position].plantId
        //showSpeciesImg = _view!!.findViewById<ImageButton>(R.id.show_species_choose_img)
        //Log.d("ChoosePlantAdapter","Position ${position}'s ImageButton: ${showSpeciesImg}")
        //showSpeciesImgArray.add(showSpeciesImg)
        //Log.d("ChoosePlantAdapter","ImgArray Size: ${showSpeciesImgArray.size}")
        val whichPlantText = binding.whichPlantText
        val activityTypeText = binding.activityTypeText
        val lastTimeText = binding.lastTimeText
        val viewLogBtn = binding.viewLogBtn
        val editLogBtn = binding.editLogBtn
        val deleteLogBtn = binding.deleteLogBtn

        if (plantId != null) {
            whichPlantText.text = plantIdToName[plantId]
        } else {
            whichPlantText.text = ""
        }
        activityTypeText.text = activityType
        lastTimeText.text = timestamp

        // setup to receive broadcast from MyDownloadService
        //initReceiver()

        //if (imgUrls.size > position) {
        //    val url = imgUrls[position]
        //    requestImageDL(url,position)
        //}

        viewLogBtn.setOnClickListener{
            //val intent = Intent(getContext(), ViewPlantDetailsActivity::class.java)
            //intent.putExtra("plantId", positionId)
            //getContext().startActivity(intent)
        }

        editLogBtn.setOnClickListener{
            val thisId = positionId
            val intent = Intent(getContext(), LogActivitiesActivity::class.java)
            intent.putExtra("update", true)
            Log.d("LogToChooseAdapter","putExtra update: true")
            intent.putExtra("logId", thisId)
            Log.d("LogToChooseAdapter","putExtra logId: ${thisId}")
            intent.putExtra("userId",userId)
            Log.d("LogToChooseAdapter","putExtra userId: ${userId}")
            getContext().startActivity(intent)
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