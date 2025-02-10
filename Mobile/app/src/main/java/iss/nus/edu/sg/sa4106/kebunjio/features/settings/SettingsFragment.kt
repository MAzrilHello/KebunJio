package iss.nus.edu.sg.sa4106.kebunjio.features.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import iss.nus.edu.sg.sa4106.kebunjio.LoggedInFragment
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.data.User
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    public lateinit var logoutButton: Button
    public lateinit var usernameTextView: TextView
    public lateinit var emailTextView: TextView
    public lateinit var phoneNoTextView: TextView
    private var sessionCookie: String = ""
    private var thisUser: User = User("","","","",false)
    private var loggedInFragment: LoggedInFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        logoutButton = binding.logoutBtn
        usernameTextView = binding.usernameText
        emailTextView = binding.emailText
        phoneNoTextView = binding.phonenoText
        // Set the click listener here
        logoutButton.setOnClickListener {
            //requireActivity().findNavController().navigate(R.id.action_loggedInFragment_to_loginFragment)
            loggedInFragment?.invalidateCookies()
            requireView().findNavController().navigate(R.id.action_loggedInFragment_to_loginFragment)
        }
        return binding.root
    }

    fun loadNewData(loggedInFragment: LoggedInFragment) {
        thisUser = loggedInFragment.loggedUser!!
        Log.d("SettingsFragment","loggedUser username: ${loggedInFragment.loggedUser!!.username}")

        sessionCookie = loggedInFragment.sessionCookie
        try {
            usernameTextView.text = thisUser.username
            Log.d("SettingsFragment","thisUser username: ${thisUser.username}")
            emailTextView.text = thisUser.email
            Log.d("SettingsFragment","thisUser email: ${thisUser.email}")
            phoneNoTextView.text = thisUser.phoneNumber
            Log.d("SettingsFragment","thisUser username: ${thisUser.phoneNumber}")
        } catch (e: Exception) {
            Log.d("SettingsFragment","Failed to assign name values")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        usernameTextView.text = thisUser.username
        emailTextView.text = thisUser.email
        phoneNoTextView.text = thisUser.phoneNumber


    }

    public fun invalidateCookies() {
        this.sessionCookie = ""

    }

    companion object {
    }
}