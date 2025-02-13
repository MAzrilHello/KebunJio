package iss.nus.edu.sg.sa4106.kebunjio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
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
