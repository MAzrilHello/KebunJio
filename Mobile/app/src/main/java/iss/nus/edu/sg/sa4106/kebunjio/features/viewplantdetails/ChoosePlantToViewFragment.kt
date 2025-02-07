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
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityChoosePlantToViewBinding

// for testing
import iss.nus.edu.sg.sa4106.kebunjio.DummyData
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity

class ChoosePlantToViewFragment : Fragment() {

    private var _binding: ActivityChoosePlantToViewBinding? = null
    private val binding get() = _binding!!
    private val dummy = DummyData()
    private var userId = "a"

    lateinit var plantToViewText: TextView
    lateinit var plantList: ListView
    lateinit var addFAB: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityChoosePlantToViewBinding.inflate(layoutInflater)
        return binding.root
    }


    public fun loadForNewUserId(userId: String) {
        this.userId = userId
        TODO("Get list of user's plants")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

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
            val intent = Intent(requireContext(),AddPlantActivity::class.java)
            intent.putExtra("userId",userId)
            Log.d("ChoosePlantToViewActivity","putExtra userId: ${userId}")
            this.startActivity(intent)
        }

        plantList.adapter = PlantToChooseAdapter(requireContext(),userPlants)
    }
}