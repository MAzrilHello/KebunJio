package iss.nus.edu.sg.sa4106.kebunjio.features.reminders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iss.nus.edu.sg.sa4106.kebunjio.LoggedInFragment
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentChoosePlantForReminderBinding

class ChoosePlantForReminderFragment : Fragment() {
    private var _binding: FragmentChoosePlantForReminderBinding? = null
    private val binding get() = _binding!!

    private var usersPlantList = arrayListOf<Plant>()
    private var loggedInFragment: LoggedInFragment? = null

    private lateinit var plantToChooseAdapter: PlantToChooseForReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoosePlantForReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun loadNewData(loggedInFragment: LoggedInFragment) {
        this.loggedInFragment = loggedInFragment
        usersPlantList = loggedInFragment.usersPlantList
        Log.d("ChoosePlantForReminderFragment","usersPlantList Size: ${usersPlantList}")

        if (!::plantToChooseAdapter.isInitialized) {
            Log.d("ChoosePlantForReminderFragment","Adapter not initialised so we are initalising")
            try {
                // @Azril:
                // this was where the app crashed
                // I placed it in try-except brackets so it won't crash in future
                // Some displays have no words
                plantToChooseAdapter = PlantToChooseForReminderAdapter(
                    requireContext(),
                    loggedInFragment,
                    loggedInFragment.haveUpdateLauncher,
                    loggedInFragment.sessionCookie,
                    loggedInFragment.loggedUser!!.id,
                    usersPlantList
                    //hashMapOf(), // Empty hashMap, species data not needed
                    //arrayListOf() // Empty list, activity log data not needed
                )
                binding.plantList.adapter = plantToChooseAdapter
                Log.d("ChoosePlantForReminderFragment","Initialised successfully")
            } catch (e: Exception) {
                Log.d("ChoosePlantForReminderFragment","On 2nd thought we won't initialise")
            }
        } else {
            Log.d("ChoosePlantForReminderFragment","Notifying data has changed")
            plantToChooseAdapter.resetData(loggedInFragment.loggedUser!!.id,
                                                usersPlantList
                                                //hashMapOf(),
                                                //arrayListOf()
                                                )
            plantToChooseAdapter.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (usersPlantList.isEmpty()) {
            binding.plantToChooseText.text = "No plants available to add for reminders"
        } else {
            binding.plantToChooseText.text = "Choose a Plant to Set a Reminder"
        }

        Log.d("ChoosePlantForReminderFragment","Setting up PlantToChooseForReminderAdapter in onViewCreated")
        plantToChooseAdapter = PlantToChooseForReminderAdapter(
            requireContext(),
            loggedInFragment!!,
            loggedInFragment!!.haveUpdateLauncher,
            loggedInFragment!!.sessionCookie,
            loggedInFragment!!.loggedUser!!.id,
            usersPlantList
            //hashMapOf(),
            //arrayListOf()
        )
        binding.plantList.adapter = plantToChooseAdapter
    }
}
