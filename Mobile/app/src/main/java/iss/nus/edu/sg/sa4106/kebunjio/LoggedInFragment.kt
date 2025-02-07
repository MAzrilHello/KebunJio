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
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import iss.nus.edu.sg.sa4106.kebunjio.data.EdiblePlantSpecies
import iss.nus.edu.sg.sa4106.kebunjio.data.User
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentLoggedInBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.logactivities.ChooseLogToViewFragment
import iss.nus.edu.sg.sa4106.kebunjio.features.settings.SettingsFragment
import iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails.ChoosePlantToViewFragment
import iss.nus.edu.sg.sa4106.kebunjio.service.PlantSpeciesLogService


class LoggedInFragment : Fragment() {
    private var _binding: FragmentLoggedInBinding? = null
    private val binding get() = _binding!!
    private var loggedUser: User? = null
    private var logToViewFragment = ChooseLogToViewFragment()
    private var plantFragment = ChoosePlantToViewFragment()
    private var settingsFragment = SettingsFragment()
    private var speciesList: ArrayList<EdiblePlantSpecies> = ArrayList()

    protected var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == "get_species_all") {
                speciesList = intent.getSerializableExtra("speciesList") as ArrayList<EdiblePlantSpecies>
                for (i in 0..speciesList.size-1) {
                    Log.d("LoggedInFragmentReceiver",speciesList[i].id)
                    Log.d("LoggedInFragmentReceiver",speciesList[i].name)
                }
            }
            else if (action == "get_plants_byuser") {

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
        //filter.addAction("get_activity_log_byuser")
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

        val bottomNavigationView = binding.bottomNavigationView

        loggedUser = LoggedInFragmentArgs.fromBundle(requireArguments()).loggedUser

        //plantFragment.loadForNewUserId(loggedUser!!.id)

        //setCurrentFragment(logToViewFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tracker_item -> setCurrentFragment(logToViewFragment)
                R.id.my_plants_item -> setCurrentFragment(plantFragment)
                R.id.guide_item -> null
                R.id.settings_item -> setCurrentFragment(settingsFragment)
            }
            true
        }
        initReceiver()
        tryPullAllSpecies()
        Log.d("LoggedInFragment","LoggedInFragment onCreateView")
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private fun tryPullAllUserPlants() {
        val intent = Intent(activity, PlantSpeciesLogService::class.java)
        intent.setAction("get_plants")
        intent.putExtra("id",loggedUser!!.id)
        intent.putExtra("byUser",true)
        activity?.startService(intent)
    }


    private fun tryPullAllSpecies() {
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


}
