package iss.nus.edu.sg.sa4106.kebunjio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentLoginOrRegisterBinding


class LoginOrRegisterFragment : Fragment() {
    private var _binding: FragmentLoginOrRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginOrRegisterBinding.inflate(inflater, container,false)

        val view = binding.root

        binding.loginBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginOrRegisterFragment_to_loginFragment)
        }
        binding.registerBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginOrRegisterFragment_to_registerFragment)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
