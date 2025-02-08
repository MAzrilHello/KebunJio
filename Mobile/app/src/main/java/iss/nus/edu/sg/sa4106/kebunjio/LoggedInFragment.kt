package iss.nus.edu.sg.sa4106.kebunjio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.data.EdiblePlantSpecies
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.data.User
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentLoggedInBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.logactivities.ChooseLogToViewFragment
import iss.nus.edu.sg.sa4106.kebunjio.features.settings.SettingsFragment
import iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails.ChoosePlantToViewFragment
import iss.nus.edu.sg.sa4106.kebunjio.service.PlantSpeciesLogService


class LoggedInFragment : Fragment() {
    private var _binding: FragmentLoggedInBinding? = null
    private val binding get() = _binding!!

    private var logToViewFragment = ChooseLogToViewFragment()
    private var plantFragment = ChoosePlantToViewFragment()
    private var settingsFragment = SettingsFragment()

    public var loggedUser: User? = null
    public var sessionCookie: String = ""
    public var speciesList: ArrayList<EdiblePlantSpecies> = ArrayList()
    public var usersPlantList: ArrayList<Plant> = ArrayList()
    public var usersActivityLogList: ArrayList<ActivityLog> = ArrayList()

    private var speciesReady: Boolean = false
    private var userPlantListReady: Boolean = false
    private var userActivityLogReady: Boolean = false
    private lateinit var bottomNavigationView: BottomNavigationView

    protected var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val responseCode = intent.getIntExtra("responseCode",-2)
            if (responseCode in 200..299) {

            } else if (responseCode == -1) {
                Log.d("LoggedInFragmentReceiver","${action}: response error: ${intent.getStringExtra("exception")}")
            } else {
                Log.d("LoggedInFragmentReceiver","${action}: response with no error: ${responseCode}")
            }
            if (action == "get_species_all") {
                speciesList = intent.getSerializableExtra("speciesList") as ArrayList<EdiblePlantSpecies>
                speciesReady = true
                Log.d("LoggedInFragmentReceiver","species list size: ${speciesList.size}")
                //for (i in 0..speciesList.size-1) {
                //    Log.d("LoggedInFragmentReceiver",speciesList[i].id)
                //    Log.d("LoggedInFragmentReceiver",speciesList[i].name)
                //}
            } else if (action == "get_plants_byuser") {
                usersPlantList = intent.getSerializableExtra("plantList") as ArrayList<Plant>
                userPlantListReady = true
                Log.d("LoggedInFragmentReceiver","user plant list size: ${usersPlantList.size}")
                tryPullAllUsersActivities()
            } else if (action == "get_activity_log_byuser") {
                usersActivityLogList = intent.getSerializableExtra("logList") as ArrayList<ActivityLog>
                userActivityLogReady = true
                Log.d("LoggedInFragmentReceiver","user activity log list size: ${usersActivityLogList.size}")
            }
            if (speciesReady && userPlantListReady && userActivityLogReady) {
                postAllArraysDownloaded()
            }
        }
    }

    protected fun initReceiver() {
        val filter = IntentFilter()
        //filter.addAction("create_plant")
        //filter.addAction("update_plant")
        //filter.addAction("get_plants")
        filter.addAction("get_plants_byuser")
        //filter.addAction("delete_plant")

        //filter.addAction("create_activity_log")
        //filter.addAction("get_activity_log")
        filter.addAction("get_activity_log_byuser")
        //filter.addAction("get_activity_log_byplant")
        //filter.addAction("update_activity_log")
        //filter.addAction("delete_activity_log")

        filter.addAction("get_species_all")
        //filter.addAction("get_species_byname")
        //filter.addAction("get_species_byid")
        registerReceiver(requireContext(), receiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoggedInBinding.inflate(inflater, container,false)

        bottomNavigationView = binding.bottomNavigationView

        loggedUser = LoggedInFragmentArgs.fromBundle(requireArguments()).loggedUser
        sessionCookie = LoggedInFragmentArgs.fromBundle(requireArguments()).sessionCookie
        Log.d("LoggdInFragment","loggedUser: ${loggedUser!!.id}")

        initReceiver()
        tryPullAllSpecies()
        tryPullAllUserPlants()
        // do not need to pull all user activities, plants will pull immediately after
        //tryPullAllUsersActivities()
        Log.d("LoggedInFragment","LoggedInFragment onCreateView")
        return binding.root
    }


    private fun postAllArraysDownloaded() {
        Log.d("LoggedInFragment","postAllArraysDownloaded")
        val speciesIdToNameDict: HashMap<String, String> = hashMapOf()
        for (i in 0..speciesList.size-1) {
            speciesIdToNameDict[speciesList[i].id] = speciesList[i].name
        }
        val plantIdToNameDict: HashMap<String, String> = hashMapOf()
        for (i in 0..usersPlantList.size-1) {
            plantIdToNameDict[usersPlantList[i].id] = usersPlantList[i].name
        }
        val userId = loggedUser!!.id
        Log.d("LoggedInFragment","Passing Cookie: ${sessionCookie}")
        //plantFragment.loadNewData(sessionCookie,userId,speciesIdToNameDict,usersPlantList,usersActivityLogList)
        plantFragment.loadNewData(this)
        logToViewFragment.loadNewData(userId,plantIdToNameDict,usersActivityLogList)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tracker_item -> null//setCurrentFragment(logToViewFragment)
                R.id.my_plants_item -> setCurrentFragment(plantFragment)
                R.id.guide_item -> null
                R.id.settings_item -> setCurrentFragment(settingsFragment)
            }
            true
        }
        setCurrentFragment(plantFragment)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun tryPullAllUsersActivities() {
        userActivityLogReady = false
        val intent = Intent(activity, PlantSpeciesLogService::class.java)
        intent.setAction("get_activity_logs")
        intent.putExtra("id",loggedUser!!.id)
        //intent.putExtra("id","679ecd82057c505d560bdbcb") // testing this one first
        intent.putExtra("singleUserOrPlant","user")
        activity?.startService(intent)
    }

    public fun tryPullAllUserPlants() {
        userPlantListReady = false
        userActivityLogReady = false
        val intent = Intent(activity, PlantSpeciesLogService::class.java)
        intent.setAction("get_plants")
        intent.putExtra("id",loggedUser!!.id)
        //intent.putExtra("id","679ecd82057c505d560bdbcb") // testing this one first
        intent.putExtra("byUser",true)
        activity?.startService(intent)
    }


    private fun tryPullAllSpecies() {
        speciesReady = false
        val intent = Intent(activity, PlantSpeciesLogService::class.java)
        intent.setAction("get_species")
        activity?.startService(intent)
    }


    private fun setCurrentFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //settingsFragment.logoutButton.setOnClickListener {
        //    binding.root.findNavController().navigate(R.id.action_loggedInFragment_to_loginFragment)
        //}
    }

    private fun makeToast(text: String,length: Int = Toast.LENGTH_LONG) {
        val msg = Toast.makeText(
            getActivity(),
            text, length
        )
        msg.show()
    }
}
