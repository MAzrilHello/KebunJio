package iss.nus.edu.sg.sa4106.kebunjio

import android.app.Activity.RESULT_OK
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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import iss.nus.edu.sg.sa4106.kebunjio.data.ActivityLog
import iss.nus.edu.sg.sa4106.kebunjio.data.EdiblePlantSpecies
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.data.User
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentLoggedInBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.browseguides.ChooseGuideToViewFragment
import iss.nus.edu.sg.sa4106.kebunjio.features.logactivities.ChooseLogToViewFragment
import iss.nus.edu.sg.sa4106.kebunjio.features.reminders.ChoosePlantForReminderFragment
import iss.nus.edu.sg.sa4106.kebunjio.features.settings.SettingsFragment
import iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails.ChoosePlantToViewFragment
import iss.nus.edu.sg.sa4106.kebunjio.service.PlantSpeciesLogService


class LoggedInFragment : Fragment() {
    private var _binding: FragmentLoggedInBinding? = null
    private val binding get() = _binding!!

    private var logToViewFragment = ChooseLogToViewFragment()
    private var plantFragment = ChoosePlantToViewFragment()
    private var settingsFragment = SettingsFragment()
    private var chooseGuideToViewFragment = ChooseGuideToViewFragment()
    private var choosePlantForReminderFragment = ChoosePlantForReminderFragment()

    public var loggedUser: User? = null
    public var sessionCookie: String = ""
    public var speciesList: ArrayList<EdiblePlantSpecies> = ArrayList()
    public var usersPlantList: ArrayList<Plant> = ArrayList()
    public var usersActivityLogList: ArrayList<ActivityLog> = ArrayList()
    public var usersPlantListReminderList: ArrayList<Plant> = ArrayList()

    private var speciesReady: Boolean = false
    private var userPlantListReady: Boolean = false
    private var userActivityLogReady: Boolean = false
    private var userPlantListReminderReady: Boolean = false
    private var loadOnceYet: Boolean = false
    private lateinit var bottomNavigationView: BottomNavigationView

    // this receiver is for downloading data only
    protected var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val responseCode = intent.getIntExtra("responseCode", -2)

            if (responseCode in 200..299) {
                Log.d("LoggedInFragmentReceiver", "$action: response successful ($responseCode)")
            } else if (responseCode == -1) {
                Log.d("LoggedInFragmentReceiver", "$action: response error: ${intent.getStringExtra("exception")}")
            } else {
                Log.d("LoggedInFragmentReceiver", "$action: response received ($responseCode)")
            }

            if (action == "get_species_all") {
                speciesList = intent.getSerializableExtra("speciesList") as ArrayList<EdiblePlantSpecies>
                speciesReady = true
            } else if (action == "get_plants_byuser") {
                usersPlantList = intent.getSerializableExtra("plantList") as ArrayList<Plant>
                userPlantListReady = true
                Log.d("LoggedInFragmentReceiver", "User plant list size: ${usersPlantList.size}")

                // ✅ Ensure Reminder-Specific Plant List is Loaded Separately
                usersPlantListReminderList = ArrayList(usersPlantList) // Copy list instead of overwriting

                userPlantListReminderReady = true
                tryPullAllUsersActivities()
                //pullAllUserPlantsToAddReminder()
            } else if (action == "get_activity_log_byuser") {
                usersActivityLogList = intent.getSerializableExtra("logList") as ArrayList<ActivityLog>
                userActivityLogReady = true
            }

            if (speciesReady && userPlantListReady && userActivityLogReady && userPlantListReminderReady) {
                postAllArraysDownloaded()
            }
        }
    }

    public lateinit var haveUpdateLauncher: ActivityResultLauncher<Intent>

    private fun initReceiver() {
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

        filter.addAction("get_reminders_byuser")
        registerReceiver(requireContext(), receiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }

    private fun initHaveUpdateLauncher() {
        haveUpdateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode== RESULT_OK) {
                val haveUpdate = result.data?.getBooleanExtra("haveUpdate",false)
                if (haveUpdate==true) {
                    // update the cookie
                    val finalCookie = HandleNulls.ifNullString(result.data?.getStringExtra("sessionCookie"))
                    if (finalCookie != "") {
                        Log.d("LoggedInFragmentLauncher","Passing Cookie ${finalCookie}")
                        sessionCookie = finalCookie
                    }
                    Log.d("LoggedInFragment","Triggering re-download")
                    tryPullAllUserPlants()
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoggedInBinding.inflate(inflater, container,false)

        bottomNavigationView = binding.bottomNavigationView

        loggedUser = LoggedInFragmentArgs.fromBundle(requireArguments()).loggedUser
        sessionCookie = LoggedInFragmentArgs.fromBundle(requireArguments()).sessionCookie
        settingsFragment.loadNewData(this)
        Log.d("LoggdInFragment","User Id: ${loggedUser!!.id}")
        Log.d("LoggdInFragment","Username: ${loggedUser!!.username}")

        initReceiver()
        initHaveUpdateLauncher()
        tryPullAllSpecies()
        tryPullAllUserPlants()
        pullAllUserPlantsToAddReminder()

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
        Log.d("LoggedInFragment","Current Species List Size: ${speciesList.size}")
        Log.d("LoggedInFragment","Current Users Plant List Size: ${usersPlantList.size}")
        Log.d("LoggedInFragment","Current Users Activity Log Size: ${usersActivityLogList.size}")
        Log.d("LoggedInFragment","Passing Cookie: ${sessionCookie}")
        //plantFragment.loadNewData(sessionCookie,userId,speciesIdToNameDict,usersPlantList,usersActivityLogList)
        plantFragment.loadNewData(this)
        logToViewFragment.loadNewData(this)
        chooseGuideToViewFragment.loadNewData(this)
        choosePlantForReminderFragment.loadNewData(this)
        //choosePlantForReminderFragment.loadNewData(this)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                //R.id.tracker_item -> startActivity(Intent(requireContext(), TrackerActivity::class.java))
                R.id.my_plants_item -> setCurrentFragment(plantFragment)
                R.id.reminder_item -> setCurrentFragment(choosePlantForReminderFragment)
                R.id.activity_log_item -> setCurrentFragment(logToViewFragment)
                R.id.guide_item -> setCurrentFragment(chooseGuideToViewFragment)
                //R.id.guide_item -> startActivity(Intent(requireContext(), BrowseGuidesActivity::class.java))
                R.id.settings_item -> setCurrentFragment(settingsFragment)
            }
            true
        }
        if (!loadOnceYet) {

            setCurrentFragment(plantFragment)
            loadOnceYet = true
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public fun tryPullAllUsersActivities() {
        userActivityLogReady = false
        val intent = Intent(activity, PlantSpeciesLogService::class.java)
        intent.setAction("get_activity_logs")
        intent.putExtra("id",loggedUser!!.id)
        //intent.putExtra("id","679ecd82057c505d560bdbcb") // testing this one first
        intent.putExtra("singleUserOrPlant","user")
        activity?.startService(intent)
    }

    public fun tryPullAllUserPlants() {
        userActivityLogReady = false
        userPlantListReady = false
        val intent = Intent(activity, PlantSpeciesLogService::class.java)
        intent.setAction("get_plants")
        intent.putExtra("id",loggedUser!!.id)
        //intent.putExtra("id","679ecd82057c505d560bdbcb") // testing this one first
        intent.putExtra("byUser",true)
        activity?.startService(intent)
    }

    // @Azril:
    // this won't do anything
    // the service only responds to 'get_plants', not 'get_plants_byuser'
    // if you want all from the user you must set  "byUser" to 'true'
    // as it is functionally the same as tryPullAllUserPlants you should not run this
    public fun pullAllUserPlantsToAddReminder() {
        userPlantListReminderReady = false
        val intent = Intent(activity, PlantSpeciesLogService::class.java)
        intent.action = "get_plants_byuser"
        intent.putExtra("id", loggedUser!!.id)
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

    fun makeToast(text: String,length: Int = Toast.LENGTH_LONG) {
        val msg = Toast.makeText(
            getActivity(),
            text, length
        )
        msg.show()
    }


    public fun invalidateCookies() {
        this.sessionCookie = ""
        settingsFragment.invalidateCookies()
        logToViewFragment.invalidateCookies()
        plantFragment.invalidateCookies()
    }
}