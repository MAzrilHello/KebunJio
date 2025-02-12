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
import iss.nus.edu.sg.sa4106.kebunjio.service.CookieHandling
import iss.nus.edu.sg.sa4106.kebunjio.service.PlantSpeciesLogService
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL


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

            Thread {
                val responseCode = callLogoutApi()
                activity?.runOnUiThread {
                    if (responseCode in 200..299) {
                        loggedInFragment?.makeToast("Logged out successfully")
                        loggedInFragment?.invalidateCookies()
                        requireView().findNavController().navigate(R.id.action_loggedInFragment_to_loginFragment)
                    } else {
                        loggedInFragment?.makeToast("Failed to logout")
                    }
                }
            }.start()

        }
        return binding.root
    }

    fun loadNewData(loggedInFragment: LoggedInFragment) {
        thisUser = loggedInFragment.loggedUser!!
        this.loggedInFragment = loggedInFragment
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


    private fun callLogoutApi(): Int {
        val partUrl = "${PlantSpeciesLogService.startUrl}/users/logout"
        val fullUrl = partUrl
        val url = URL(fullUrl)
        val connection = url.openConnection() as HttpURLConnection
        var responseCode = -1

        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = 15000  // 15 seconds
            connection.readTimeout = 15000    // 15 seconds

            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false
            CookieHandling.setSessionCookie(connection,sessionCookie)

            val outputStream = DataOutputStream(connection.outputStream)

            outputStream.flush()
            outputStream.close()

            responseCode = connection.responseCode

        } catch (e: Exception) {

        } finally {
            connection.disconnect()
        }
        return responseCode
    }


    companion object {
    }
}