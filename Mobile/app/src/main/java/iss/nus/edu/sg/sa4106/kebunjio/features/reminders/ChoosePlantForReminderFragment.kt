package iss.nus.edu.sg.sa4106.kebunjio.features.reminders

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iss.nus.edu.sg.sa4106.kebunjio.LoggedInFragment
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentChoosePlantForReminderBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails.PlantToChooseAdapter

class ChoosePlantForReminderFragment : Fragment() {
    private var _binding: FragmentChoosePlantForReminderBinding? = null
    private val binding get() = _binding!!

    private var usersPlantList = arrayListOf<Plant>()
    private var loggedInFragment: LoggedInFragment? = null

    private lateinit var plantToChooseAdapter: PlantToChooseAdapter

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

        if (!::plantToChooseAdapter.isInitialized) {
            plantToChooseAdapter = PlantToChooseAdapter(
                requireContext(),
                loggedInFragment,
                loggedInFragment.haveUpdateLauncher,
                loggedInFragment.sessionCookie,
                loggedInFragment.loggedUser!!.id,
                usersPlantList,
                hashMapOf(), // Empty hashMap, species data not needed
                arrayListOf() // Empty list, activity log data not needed
            )
            binding.plantList.adapter = plantToChooseAdapter
        } else {
            plantToChooseAdapter.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (usersPlantList.isEmpty()) {
            binding.plantToChooseText.text = "No plants available for reminders"
        } else {
            binding.plantToChooseText.text = "Choose a Plant to Set a Reminder"
        }

        plantToChooseAdapter = PlantToChooseAdapter(
            requireContext(),
            loggedInFragment!!,
            loggedInFragment!!.haveUpdateLauncher,
            loggedInFragment!!.sessionCookie,
            loggedInFragment!!.loggedUser!!.id,
            usersPlantList,
            hashMapOf(),
            arrayListOf()
        )
        binding.plantList.adapter = plantToChooseAdapter
    }
}
