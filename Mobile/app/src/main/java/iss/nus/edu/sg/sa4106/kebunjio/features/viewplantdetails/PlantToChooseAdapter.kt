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
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ViewPlantToChooseBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity
import iss.nus.edu.sg.sa4106.kebunjio.service.DownloadImageService
import java.io.File


class PlantToChooseAdapter(private val context: Context,
                           protected var plantList: MutableList<Plant>
        ): ArrayAdapter<Any?>(context, R.layout.view_plant_to_choose) {

    //private var _binding: ViewPlantToChooseBinding? = null

    //lateinit var showSpeciesImg: ImageButton
    //private var showSpeciesImgArray: MutableList<ImageButton> = mutableListOf<ImageButton>()
    //lateinit var showPlantName: TextView
    //lateinit var viewPlantBtn: Button
    //lateinit var deletePlantBtn: Button
    lateinit var storedPlants:  MutableList<Plant>

    //protected var receiver: BroadcastReceiver = object : BroadcastReceiver() {
    //    override fun onReceive(context: Context, intent: Intent) {
    //        val action = intent.action
    //        Log.d("ChoosePlantAdapter","Got feedback ${intent}, ${action}")
    //        if (action != null) {
    //            if (action == "download_completed_id") {
    //                Log.d("ChoosePlantAdapter","Getting filename")

                //val filename = intent.getStringExtra("filename")
    //                val position = intent.getIntExtra("id",-1)
    //                val filename = intent.getStringExtra("filename")
    //                Log.d("ChoosePlantAdapter", "received position ${position} filename: ${filename}")
    //                if (filename != null) {
    //                    val bitmap = BitmapFactory.decodeFile(filename)
    //                    Log.d("ChoosePlantAdapter","Set image bitmap for ${position}: ${showSpeciesImgArray[position]}")
    //                    showSpeciesImgArray[position].setImageBitmap(bitmap)
    //                    val file = File(filename)
    //                    if (file.exists()) {
    //                        val handler = android.os.Handler()
    //                        handler.postDelayed({
    //                            file.delete()
    //                            Log.d("ChoosePlantAdapter","Deleted file for position ${position}")
    //                        },5000)
    //                    }
    //                }
    //            }
    //        } else {
    //            Log.d("ChoosePlantAdapter","ERROR: Action is null")
    //        }
    //    }
    //}


    init {
        for (i in 0..plantList.size-1) {

        }
        addAll(*arrayOfNulls<Any>(plantList.size))
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
        this.storedPlants = plantList
        //showSpeciesImg = _view!!.findViewById<ImageButton>(R.id.show_species_choose_img)
        //Log.d("ChoosePlantAdapter","Position ${position}'s ImageButton: ${showSpeciesImg}")
        //showSpeciesImgArray.add(showSpeciesImg)
        //Log.d("ChoosePlantAdapter","ImgArray Size: ${showSpeciesImgArray.size}")
        val showPlantName = binding.plantNameChooseText
        val viewPlantBtn = binding.viewPlantBtn
        val editPlantBtn = binding.editPlantBtn
        val deletePlantBtn = binding.deletePlantBtn
        Log.d("ChoosePlantAdapter","Position ${position}'s TextView: ${showPlantName}")

        showPlantName.text = plantList[position].name

        // setup to receive broadcast from MyDownloadService
        //initReceiver()

        //if (imgUrls.size > position) {
        //    val url = imgUrls[position]
        //    requestImageDL(url,position)
        //}

        viewPlantBtn.setOnClickListener{
            val thisId = this.plantList[position].id
            val userId = this.plantList[position].userId
            val intent = Intent(getContext(), ViewPlantDetailsActivity::class.java)
            intent.putExtra("plantId", thisId)
            getContext().startActivity(intent)
        }

        editPlantBtn.setOnClickListener{
            val thisId = this.plantList[position].id
            val userId = this.plantList[position].userId
            val intent = Intent(getContext(), AddPlantActivity::class.java)
            intent.putExtra("update", true)
            Log.d("PlantToChooseAdapter","putExtra update: true")
            intent.putExtra("plantId", thisId)
            intent.putExtra("userId",userId)
            Log.d("PlantToChooseAdapter","putExtra userId: ${userId}")
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