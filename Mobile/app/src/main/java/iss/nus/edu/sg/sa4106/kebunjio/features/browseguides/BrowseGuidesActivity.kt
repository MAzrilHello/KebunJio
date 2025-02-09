package iss.nus.edu.sg.sa4106.kebunjio.features.browseguides

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import iss.nus.edu.sg.sa4106.kebunjio.DummyData
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.adapter.GuideAdapter
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ActivityBrowseGuidesBinding



class BrowseGuidesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBrowseGuidesBinding
    private lateinit var guideAdapter: GuideAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBrowseGuidesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initButtons()
        setupRecyclerView()
    }

    private fun initButtons() {
        binding.viewPlantsButton.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        val dummyData = DummyData()
        val speciesList = dummyData.SpeciesDummy
        guideAdapter = GuideAdapter(speciesList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = guideAdapter
    }
}