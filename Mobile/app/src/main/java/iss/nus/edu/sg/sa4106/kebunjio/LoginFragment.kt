package iss.nus.edu.sg.sa4106.kebunjio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import iss.nus.edu.sg.sa4106.kebunjio.data.User
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentLoginBinding
import iss.nus.edu.sg.sa4106.kebunjio.service.CookieHandling
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container,false)

        val view = binding.root

        binding.backBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_loginOrRegisterFragment)
        }

        navigateToLoggedInFragment()

        binding.loginBtn.setOnClickListener{
            navigateToLoggedInFragment()

//            attemptLogin()
            // for now should just switch to Logged In Fragment
            // set username to 'username'
        }

        return binding.root
    }

    private fun navigateToLoggedInFragment() {
        // ✅ Creating a dummy user with default session
        val dummyUser = User(
            id = "123",
            username = "TestUser",
            email = "testuser@example.com",
            phoneNumber = "0000000000",
            isAdmin = false
        )

        val dummySessionCookie = "dummy_cookie"

        // ✅ Log navigation for debugging
        Log.d("LoginFragment", "Bypassing login and navigating to LoggedInFragment")

        // ✅ Navigate immediately
        val action = LoginFragmentDirections.actionLoginFragmentToLoggedInFragment(dummyUser, dummySessionCookie)
        view?.findNavController()?.navigate(action)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun attemptLogin() {
        val username = binding.usernameText.text.toString()
        val password = binding.passwordText.text.toString()
        if (username.equals("") || password.equals("")) {
            makeToast("Please key in a valid username/ email and password")
            return
        }
        attemptLogin(username,password)
        // attempt to login

        // do not start intent here, rely on the navigation map
        //val intent = Intent(requireContext(), LoggedInTestActivity::class.java)
        //startActivity(intent)
    }


    private fun attemptLogin(username: String, password: String) {
        Thread{
            val partUrl = "http://10.0.2.2:8080/api/Users/login"
            val encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString())
            val encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8.toString())
            val fullUrl = "$partUrl?emailOrUsername=$encodedUsername&password=$encodedPassword"
            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection

            try {

                connection.requestMethod = "POST"
                connection.connectTimeout = 15000  // 15 seconds
                connection.readTimeout = 15000    // 15 seconds

                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false

                Log.d("LoginFragment","Data output stream")
                val outputStream = DataOutputStream(connection.outputStream)
                Log.d("LoginFragment","Flush data")
                outputStream.flush()
                Log.d("LoginFragment","Close output stream")
                outputStream.close()
                val responseCode = connection.responseCode
                val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }
                Log.d("LoginFragment","Response Code: ${responseCode}")
                activity?.runOnUiThread {
                    if (responseCode in 200..299) {
                        makeToast("Successfully logged in!",Toast.LENGTH_SHORT)
                        val responseObject = JSONObject(responseMessage)
                        val theUser = User.getFromResponseObject(responseObject)
                        val sessionCookie = CookieHandling.extractSessionCookie(connection)
                        Log.d("LoginFragment","Cookie: ${sessionCookie}")
                        Log.d("LoginFragment","Navigating to LoggedInFragment")
                        val action = LoginFragmentDirections.actionLoginFragmentToLoggedInFragment(theUser,sessionCookie)
                        binding.root.findNavController().navigate(action)
                        //val action = LoginFragmentDirections.actionLoginFragmentToLoggedInFragment()
                        //binding.root.findNavController().navigate(R.id.action_loginFragment_to_loggedInFragment)
                    } else {
                        makeToast("Please input a valid username/ email and password!",Toast.LENGTH_SHORT)
                    }
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    makeToast("Error in login: ${e.toString()}")
                    Log.d("LoginFragment","Error in login: ${e.toString()}")
                }
            } finally {
                connection.disconnect()
            }


        }.start()
    }


    private fun makeToast(text: String,length: Int = Toast.LENGTH_LONG) {
        val msg = Toast.makeText(
            getActivity(),
            text, length
        )
        msg.show()
    }
}
