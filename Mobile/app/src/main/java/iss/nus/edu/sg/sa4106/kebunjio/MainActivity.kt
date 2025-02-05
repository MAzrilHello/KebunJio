package iss.nus.edu.sg.sa4106.kebunjio

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityMainBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.logactivities.ChooseLogToViewActivity
import iss.nus.edu.sg.sa4106.kebunjio.features.planthealthcheck.PlantHealthCheckActivity
import iss.nus.edu.sg.sa4106.kebunjio.features.reminders.ReminderActivity
import iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails.ChoosePlantToViewActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toPlantRelatedBtn.setOnClickListener {
            val intent = Intent(this, ChoosePlantToViewActivity::class.java)
            startActivity(intent)
        }
        binding.toLogRelatedButton.setOnClickListener {
            val intent = Intent(this, ChooseLogToViewActivity::class.java)
            startActivity(intent)
        }
        binding.toAddReminderButton.setOnClickListener {
            val intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)
        }
        binding.toHealthCheckButton.setOnClickListener {
            val intent = Intent(this, PlantHealthCheckActivity::class.java)
            startActivity(intent)
        }
        //startActivity()

    }
    //private fun startActivity() {
        //val intent = Intent(this, ReminderActivity::class.java)
        //val intent = Intent(this, ChooseLogToViewActivity::class.java)
        //val intent = Intent(this, ChoosePlantToViewActivity::class.java)
        //val intent = Intent(this, ReminderActivity::class.java)
        //startActivity(intent)

    //}
}
