package iss.nus.edu.sg.sa4106.kebunjio

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    //private fun startActivity() {
        //val intent = Intent(this, ReminderActivity::class.java)
        //val intent = Intent(this, ChooseLogToViewActivity::class.java)
        //val intent = Intent(this, ChoosePlantToViewActivity::class.java)
        //val intent = Intent(this, ReminderActivity::class.java)
        //startActivity(intent)

    //}
}
