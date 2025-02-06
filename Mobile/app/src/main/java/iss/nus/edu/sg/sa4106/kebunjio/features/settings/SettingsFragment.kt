package iss.nus.edu.sg.sa4106.kebunjio.features.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    public lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        logoutButton = binding.logoutBtn
        // Set the click listener here
        logoutButton.setOnClickListener {
            //requireActivity().findNavController().navigate(R.id.action_loggedInFragment_to_loginFragment)
            requireView().findNavController().navigate(R.id.action_loggedInFragment_to_loginFragment)
        }
        return binding.root
    }

    companion object {
    }
}