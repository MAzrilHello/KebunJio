package iss.nus.edu.sg.sa4106.kebunjio.features.logactivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentChooseLogToViewBinding

// for testing
import iss.nus.edu.sg.sa4106.kebunjio.DummyData
import iss.nus.edu.sg.sa4106.kebunjio.LoggedInFragment
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity

class ChooseLogToViewFragment : Fragment() {

    private var _binding: FragmentChooseLogToViewBinding? = null
    private val binding get() = _binding!!
    //private val dummy = DummyData()

    private var sessionCookie: String = ""
    private var userId = ""
    private var usersActivityLogList: ArrayList<ActivityLog> = ArrayList()
    private var plantIdToNameDict: HashMap<String, String> = hashMapOf()
    private lateinit var haveUpdateLauncher: ActivityResultLauncher<Intent>
    private var loggedInFragment: LoggedInFragment? = null

    lateinit var logToViewText: TextView
    lateinit var actLogList: ListView
    private var actLogListAdapter: LogToChooseAdapter? = null
    lateinit var addFAB: FloatingActionButton

    public fun loadNewData(loggedInFragment: LoggedInFragment) {
        this.loggedInFragment = loggedInFragment
        this.sessionCookie = loggedInFragment.sessionCookie
        this.userId = loggedInFragment.loggedUser!!.id

        this.usersActivityLogList = loggedInFragment.usersActivityLogList
        this.haveUpdateLauncher = loggedInFragment.haveUpdateLauncher

        this.plantIdToNameDict.clear()
        for (i in 0..loggedInFragment.usersPlantList.size-1) {
            this.plantIdToNameDict[loggedInFragment.usersPlantList[i].id] = loggedInFragment.usersPlantList[i].name
        }
        Log.d("ChooseLogToViewFragment","plantIdToNameDict Size: ${this.plantIdToNameDict.size}")
        reloadActivityLogList()
    }


    private fun reloadActivityLogList() {
        if (actLogListAdapter != null) {
            Log.d("ChooseLogToViewFragment","Resizing begins ${userId}, ${usersActivityLogList.size}, ${this.plantIdToNameDict.size}")
            actLogListAdapter!!.resetData(this.userId,this.usersActivityLogList,this.plantIdToNameDict)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLogToViewBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        logToViewText = binding.logToViewText
        // get the species list view
        actLogList = binding.actLogList
        addFAB = binding.addFab

        //val userPlants = dummy.getUserPlants(0)
        //val idList: MutableList<Int> = mutableListOf<Int>()
        //val nameList: MutableList<String> = mutableListOf<String>()

        //for (i in 0..userPlants.size-1) {
        //    idList.add(userPlants[i].plantId)
        //    nameList.add(userPlants[i].name)
        //}

        //val userActLogList = dummy.getUserLogs(userId)
        //val plantList = dummy.PlantDummy

        if (usersActivityLogList.size==0) {
            logToViewText.text = "No activity log"
        } else {
            logToViewText.text = "Choose activity log"
        }

        addFAB.setOnClickListener {
            val intent = Intent(requireContext(),LogActivitiesActivity::class.java)
            intent.putExtra("userId",userId)
            intent.putExtra("plantIdToNameDict",plantIdToNameDict)
            intent.putExtra("sessionCookie",sessionCookie)
            haveUpdateLauncher.launch(intent)
        }

        actLogListAdapter = LogToChooseAdapter(requireContext(),
                                    loggedInFragment!!,
                                    haveUpdateLauncher,
                                    sessionCookie,
                                    userId,
                                    usersActivityLogList,
                                    plantIdToNameDict)
        actLogList.adapter = actLogListAdapter
    }

    public fun invalidateCookies() {
        this.sessionCookie = ""
        actLogListAdapter?.invalidateCookies()
    }
}