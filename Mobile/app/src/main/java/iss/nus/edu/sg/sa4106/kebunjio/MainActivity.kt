package iss.nus.edu.sg.sa4106.kebunjio

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iss.nus.edu.sg.sa4106.kebunjio.data.Reminder
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityMainBinding
import iss.nus.edu.sg.sa4106.kebunjio.features.addplant.AddPlantActivity
<<<<<<< HEAD
=======
import iss.nus.edu.sg.sa4106.kebunjio.features.logactivities.LogActivitiesActivity
>>>>>>> 26241c7d3c4755d7ac50f4219e98749892b49dd7
import iss.nus.edu.sg.sa4106.kebunjio.features.planthealthcheck.PlantHealthCheckActivity
import iss.nus.edu.sg.sa4106.kebunjio.features.reminders.ReminderActivity
import iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails.ViewPlantDetailsActivity
import iss.nus.edu.sg.sa4106.kebunjio.features.viewplantdetails.ChoosePlantToViewActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startActivity()

    }
    private fun startActivity() {
        //val intent = Intent(this, ReminderActivity::class.java)
        val intent = Intent(this, ReminderActivity::class.java)
        startActivity(intent)

    }

//     private fun startActivity() {
//         val intent = Intent(this, ReminderActivity::class.java)
//         startActivity(intent)
//     }
}
