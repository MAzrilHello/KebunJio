package iss.nus.edu.sg.sa4106.kebunjio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import iss.nus.edu.sg.sa4106.kebunjio.data.DAO.RegisterDAO
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentRegisterAccountBinding
import iss.nus.edu.sg.sa4106.kebunjio.service.PlantSpeciesLogService
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class RegisterAccountFragment : Fragment() {
    private var _binding: FragmentRegisterAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterAccountBinding.inflate(inflater, container,false)


        binding.backBtn.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_registerFragment_to_loginOrRegisterFragment)
        }

        binding.registerBtn.setOnClickListener {
            registerAccount()
        }

        return binding.root
    }

    private fun registerAccount() {
        // get all input values
        val username = binding.usernameText.text.toString()
        val email = binding.emailText.text.toString()
        val phoneno = binding.phonenoText.text.toString()
        val password = binding.passwordText.text.toString()
        val confirmPassword = binding.confirmPasswordText.text.toString()
        // check that all are filled
        if (username.equals("") || email.equals("") || phoneno.equals("") || password.equals("") || confirmPassword.equals("")) {
            makeToast("Please ensure all parts are filled",Toast.LENGTH_SHORT)
            return
        }
        if (!password.equals(confirmPassword)) {
            makeToast("Please ensure both passwords are the same",Toast.LENGTH_SHORT)
            return
        }

        val registerDAO = RegisterDAO(email,username,password,confirmPassword,phoneno)
        doRegistration(registerDAO)
    }


    private fun doRegistration(registerDAO: RegisterDAO) {
        Thread{
            Log.d("RegisterAccountFragment","Making the connection")
            val fullUrl = "${PlantSpeciesLogService.startUrl}/users/signup"
            val url = URL(fullUrl)
            val connection = url.openConnection() as HttpURLConnection
            try {

                connection.requestMethod = "POST"
                connection.connectTimeout = 15000  // 15 seconds
                connection.readTimeout = 15000    // 15 seconds

                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false

                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")

                Log.d("RegisterAccountFragment","Registering json")
                val registerJson = JSONObject().apply {
                    put("email",registerDAO.email)
                    put("username",registerDAO.username)
                    put("password",registerDAO.password)
                    put("confirmPassword",registerDAO.confirmPassword)
                    put("contactPhone",registerDAO.contactPhone)
                }

                Log.d("RegisterAccountFragment","Data output stream")
                val outputStream = DataOutputStream(connection.outputStream)
                Log.d("RegisterAccountFragment","Write json bytes")
                outputStream.writeBytes(registerJson.toString())
                Log.d("RegisterAccountFragment","Flush data")
                outputStream.flush()
                Log.d("RegisterAccountFragment","Close output stream")
                outputStream.close()

                val responseCode = connection.responseCode
                Log.d("RegisterAccountFragment","Response Code: ${responseCode}")
                activity?.runOnUiThread {
                    if (responseCode in 200..299) {
                        Log.d("RegisterAccountFragment","Success!")
                        makeToast("Account successfully created",Toast.LENGTH_SHORT)
                        binding.root.findNavController().navigate(R.id.action_registerFragment_to_loginOrRegisterFragment)
                    } else {
                        Log.d("RegisterAccountFragment","Fail!")
                        makeToast("Error in account creation: ${responseCode}",Toast.LENGTH_SHORT)
                    }
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    makeToast("Error in account creation: ${e.toString()}")
                    Log.d("RegisterAccountFragment", "Error in account creation: ${e.toString()}")
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