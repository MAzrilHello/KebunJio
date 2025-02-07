package iss.nus.edu.sg.sa4106.kebunjio

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentLoginBinding


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

        binding.loginBtn.setOnClickListener{
            attemptLogin()
            // for now should just switch to Logged In Fragment
            // set username to 'username'
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun attemptLogin() {
        val username = binding.usernameText.text.toString()
        val password = binding.passwordText.text.toString()
        if (username.equals("") || password.equals("")) {
            makeToast("Please key in a valid username and password")
            return
        }
        // attempt to login
        binding.root.findNavController().navigate(R.id.action_loginFragment_to_loggedInFragment)
        // do not start intent here, rely on the navigation map
        //val intent = Intent(requireContext(), LoggedInTestActivity::class.java)
        //startActivity(intent)
    }

    private fun makeToast(text: String) {
        val msg = Toast.makeText(
            getActivity(),
            text, Toast.LENGTH_LONG
        )
        msg.show()
    }
}
