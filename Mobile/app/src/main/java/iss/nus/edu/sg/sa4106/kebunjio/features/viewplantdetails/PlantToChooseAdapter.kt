package iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails;

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
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ViewPlantToChooseBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity
import iss.nus.edu.sg.sa4106.kebunjio.service.DownloadImageService
import java.io.File


class PlantToChooseAdapter(private val context: Context,
                           protected var userId: String,
                           protected var usersPlantList: ArrayList<Plant>,
                           protected var speciesIdToNameDict: HashMap<String, String>,
                           protected var usersActivityLogList: ArrayList<ActivityLog>
        ): ArrayAdapter<Any?>(context, R.layout.view_plant_to_choose) {


    init {
        addAll(*arrayOfNulls<Any>(usersPlantList.size))
    }

    interface OnPlantUpdateActionListener {
        fun onPlantUpdated()
    }

    public fun resetData(userId: String,
                         usersPlantList: ArrayList<Plant>,
                         speciesIdToNameDict: HashMap<String, String>,
                         usersActivityLogList: ArrayList<ActivityLog>){
        this.userId = userId
        //this.usersPlantList = usersPlantList
        this.usersPlantList.clear()
        this.usersPlantList.addAll(usersPlantList)
        //this.speciesIdToNameDict = speciesIdToNameDict
        this.speciesIdToNameDict.clear()
        this.speciesIdToNameDict.putAll(speciesIdToNameDict)
        //this.usersActivityLogList = usersActivityLogList
        this.usersActivityLogList.clear()
        this.usersActivityLogList.addAll(usersActivityLogList)
        Log.d("PlantToChooseAdapter","notifyDataSetChanged size ${this.usersPlantList.size}")
        notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var _view = view
        var binding: ViewPlantToChooseBinding
        if (_view == null) {
            val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // if we are not responsible for adding the view to the parent,
            // then attachToRoot should be 'false' (which is in our case)
            //_view = inflater.inflate(R.layout.view_plant_to_choose, parent, false)
            binding = ViewPlantToChooseBinding.inflate(inflater,parent,false)
            _view = binding.root
        } else {
            binding = ViewPlantToChooseBinding.bind(_view)
        }
        val showPlantName = binding.plantNameChooseText
        val viewPlantBtn = binding.viewPlantBtn
        val editPlantBtn = binding.editPlantBtn
        val deletePlantBtn = binding.deletePlantBtn
        Log.d("ChoosePlantAdapter","Position ${position}'s TextView: ${showPlantName}")

        showPlantName.text = usersPlantList[position].name

        // setup to receive broadcast from MyDownloadService
        //initReceiver()

        //if (imgUrls.size > position) {
        //    val url = imgUrls[position]
        //    requestImageDL(url,position)
        //}

        viewPlantBtn.setOnClickListener{
            //val intent = Intent(getContext(), ViewPlantDetailsActivity::class.java)
            //intent.putExtra("plantId", usersPlantList[position].id)
            //getContext().startActivity(intent)
        }

        editPlantBtn.setOnClickListener{
            //val intent = Intent(getContext(), AddPlantActivity::class.java)
            //intent.putExtra("userId",userId)
            //intent.putExtra("speciesIdToNameDict",speciesIdToNameDict)
            //intent.putExtra("update", true)
            //intent.putExtra("plantId", usersPlantList[position].id)
            //getContext().startActivity(intent)
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
}