package iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResult
import com.google.android.material.floatingactionbutton.FloatingActionButton

// for testing
import iss.nus.edu.sg.sa4106.kebunjio.LoggedInFragment
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentChoosePlantToViewBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity

class ChoosePlantToViewFragment : Fragment() {

    private var _binding: FragmentChoosePlantToViewBinding? = null
    private val binding get() = _binding!!
    //private val dummy = DummyData()

    private var sessionCookie: String = ""
    private var userId = ""
    private var speciesIdToNameDict: HashMap<String, String> = hashMapOf<String, String>()
    private var usersPlantList = arrayListOf<Plant>()
    private var usersActivityLogList = arrayListOf<ActivityLog>()
    private var loggedInFragment: LoggedInFragment? = null

    lateinit var plantToViewText: TextView
    lateinit var plantList: ListView
    private var plantListAdapter: PlantToChooseAdapter? = null
    lateinit var addFAB: FloatingActionButton

    //private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var haveUpdateLauncher: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoosePlantToViewBinding.inflate(layoutInflater)
        return binding.root
    }

    public fun loadNewData(loggedInFragment: LoggedInFragment) {
        this.loggedInFragment = loggedInFragment
        this.sessionCookie = loggedInFragment.sessionCookie
        this.userId = loggedInFragment.loggedUser!!.id

        this.usersPlantList = loggedInFragment.usersPlantList
        this.usersActivityLogList = loggedInFragment.usersActivityLogList
        this.haveUpdateLauncher = loggedInFragment.haveUpdateLauncher

        this.speciesIdToNameDict.clear()
        for (i in 0..loggedInFragment.speciesList.size-1) {
            this.speciesIdToNameDict[loggedInFragment.speciesList[i].id] = loggedInFragment.speciesList[i].name
        }
        Log.d("ChoosePlantToViewFragment","speciesIdToNameDict size: ${loggedInFragment.speciesList.size} vs ${speciesIdToNameDict.size}")
        reloadPlantList()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        plantToViewText = binding.plantToViewText
        // get the species list view
        plantList = binding.plantList
        addFAB = binding.addFab

        addFAB.setOnClickListener {
            val intent = Intent(requireContext(),AddPlantActivity::class.java)
            intent.putExtra("userId",userId)
            if (speciesIdToNameDict.size != loggedInFragment!!.speciesList.size) {
                speciesIdToNameDict.clear()
                for (i in 0..loggedInFragment!!.speciesList.size-1) {
                    this.speciesIdToNameDict[loggedInFragment!!.speciesList[i].id] = loggedInFragment!!.speciesList[i].name
                }
                Log.d("ChoosePlantToViewFragment","speciesIdToNameDict pre-add refresh size: ${speciesIdToNameDict.size}")
            } else {
                Log.d("ChoosePlantToViewFragment","speciesIdToNameDict pre-add size: ${speciesIdToNameDict.size}")
            }
            intent.putExtra("speciesIdToNameDict",speciesIdToNameDict)

            intent.putExtra("sessionCookie",sessionCookie)
            Log.d("ChoosePlantToViewActivity","putExtra userId: ${userId}")
            //launcher.launch(intent)
            haveUpdateLauncher.launch(intent)
            //this.startActivity(intent)
        }

        if (usersPlantList.size==0) {
            plantToViewText.text = "No plants to view"
        } else {
            plantToViewText.text = "Choose Plant to View"
        }

        //launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //    result: ActivityResult ->
        //        if (result.resultCode==RESULT_OK) {
        //            val haveUpdate = result.data?.getBooleanExtra("haveUpdate",false)
        //            if (haveUpdate==true) {
        //                //TODO("Trigger Logged In Fragment to update the plants")
        //                Log.d("ChoosePlantToViewFragment","Triggering re-download")
        //                loggedInFragment?.tryPullAllUserPlants()
        //            }
        //        }
        //}
        Log.d("ChoosePlantToViewFragment","speciesIdToNameDict size: ${speciesIdToNameDict.size}")
        plantListAdapter = PlantToChooseAdapter(
            requireContext(),
            loggedInFragment!!,
            haveUpdateLauncher,
            sessionCookie,
            userId,
            usersPlantList,
            speciesIdToNameDict,
            usersActivityLogList)
        plantList.adapter = plantListAdapter
        //reloadPlantList()
        //Log.d("ChoosePlantToViewFragment","onViewCreated has run")
    }

    private fun reloadPlantList() {
        if (plantListAdapter != null) {
            Log.d("ChoosePlantToViewFragment", "Resizing begins")
            plantListAdapter!!.resetData(userId,usersPlantList,speciesIdToNameDict,usersActivityLogList)
            plantList.invalidateViews()
        } else {
            Log.d("ChoosePlantToViewFragment","Adapter not initialised, skipping reload")
        }
    }
}