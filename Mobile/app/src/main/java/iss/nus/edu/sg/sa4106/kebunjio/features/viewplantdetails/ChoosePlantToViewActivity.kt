package iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityChoosePlantToViewBinding

// for testing
import iss.nus.edu.sg.sa4106.kebunjio.DummyData
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity

class ChoosePlantToViewActivity : AppCompatActivity() {

    private var _binding: ActivityChoosePlantToViewBinding? = null
    private val binding get() = _binding!!
    private val dummy = DummyData()
    private val userId = "a"

    lateinit var plantToViewText: TextView
    lateinit var plantList: ListView
    lateinit var addFAB: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        _binding = ActivityChoosePlantToViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        plantToViewText = binding.plantToViewText
        // get the species list view
        plantList = binding.plantList
        addFAB = binding.addFab

        val userPlants = dummy.getUserPlants(userId)
        //val idList: MutableList<Int> = mutableListOf<Int>()
        //val nameList: MutableList<String> = mutableListOf<String>()

        //for (i in 0..userPlants.size-1) {
        //    idList.add(userPlants[i].plantId)
        //    nameList.add(userPlants[i].name)
        //}

        if (userPlants.size==0) {
            plantToViewText.text = "No plants to view"
        } else {
            plantToViewText.text = "Choose Plant to View"
        }

        addFAB.setOnClickListener {
            val intent = Intent(this,AddPlantActivity::class.java)
            intent.putExtra("userId",userId)
            Log.d("ChoosePlantToViewActivity","putExtra userId: ${userId}")
            this.startActivity(intent)
        }

        plantList.adapter = PlantToChooseAdapter(this,userPlants)
    }
}