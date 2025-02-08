package iss.nus.edu.sg.sa4106.kebunjio

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentLoggedInBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.browseguides.BrowseGuidesActivity
import iss.nus.edu.sg.sa4106.kebunjio.features.logactivities.ChooseLogToViewFragment
import iss.nus.edu.sg.sa4106.kebunjio.features.settings.SettingsFragment
import iss.nus.edu.sg.sa4106.kebunjio.features.tracker.TrackerActivity
import iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails.ChoosePlantToViewFragment


class LoggedInFragment : Fragment() {
    private var _binding: FragmentLoggedInBinding? = null
    private val binding get() = _binding!!
    private var userId: String? = null
    private var sessionId: String? = null
    private var logToViewFragment = ChooseLogToViewFragment()
    private var plantFragment = ChoosePlantToViewFragment()
    private var settingsFragment = SettingsFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoggedInBinding.inflate(inflater, container,false)

        val bottomNavigationView = binding.bottomNavigationView

        setCurrentFragment(logToViewFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tracker_item -> startActivity(Intent(requireContext(), TrackerActivity::class.java))
//                R.id.tracker_item -> setCurrentFragment(logToViewFragment)
                R.id.my_plants_item -> setCurrentFragment(plantFragment)
                R.id.guide_item -> startActivity(Intent(requireContext(), BrowseGuidesActivity::class.java))
                R.id.settings_item -> setCurrentFragment(settingsFragment)
            }
            true
        }

        return binding.root
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
