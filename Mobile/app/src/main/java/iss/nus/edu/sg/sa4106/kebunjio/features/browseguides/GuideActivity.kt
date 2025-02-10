package iss.nus.edu.sg.sa4106.kebunjio.features.browseguides

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import iss.nus.edu.sg.sa4106.kebunjio.data.EdiblePlantSpecies
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityGuideBinding
import java.net.HttpURLConnection
import java.net.URL

class GuideActivity : AppCompatActivity() {

    private var _binding: ActivityGuideBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val ediblePlantSpecies = intent.getSerializableExtra("ediblePlantSpecies") as EdiblePlantSpecies
        binding.nameText.text = ediblePlantSpecies.name
        binding.sciNameText.text = ediblePlantSpecies.scientificName
        binding.descriptionText.text = ediblePlantSpecies.description
        binding.waterTipsText.text = ediblePlantSpecies.wateringTips
        binding.sunlightText.text = ediblePlantSpecies.sunlight
        binding.soilTypeText.text = ediblePlantSpecies.soilType
        binding.harvestTimeText.text = ediblePlantSpecies.harvestTime
        binding.commonPestsText.text = ediblePlantSpecies.commonPests
        binding.growSpaceText.text = ediblePlantSpecies.growingSpace
        binding.fertilizerTipsText.text = ediblePlantSpecies.fertilizerTips
        binding.specialNeedsText.text = ediblePlantSpecies.specialNeeds
        downloadImage(ediblePlantSpecies.imageURL)
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun downloadImage(imgUrl: String) {

        Thread{
            var conn : HttpURLConnection? = null
            try {
                val url = URL(imgUrl)
                conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                if (conn.responseCode in 200..299) {
                    val inp = conn.inputStream
                    val bitmap: Bitmap = BitmapFactory.decodeStream(inp)
                    runOnUiThread{
                        binding.showPlantImg.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                Log.d("GuideActivity","Failed to download image ${e.toString()}")
            } finally {
                conn?.disconnect()
            }
        }.start()
    }

    fun makeToast(text: String,length: Int = Toast.LENGTH_LONG) {
        val msg = Toast.makeText(
            this,
            text, length
        )
        msg.show()
    }

}