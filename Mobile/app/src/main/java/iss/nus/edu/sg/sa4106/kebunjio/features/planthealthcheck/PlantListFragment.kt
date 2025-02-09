package iss.nus.edu.sg.sa4106.kebunjio.features.planthealthcheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentPlantListBinding


class PlantListFragment : Fragment() {

    private lateinit var binding: FragmentPlantListBinding
    private val plantList = mutableListOf<Plant>()
    private lateinit var adapter: PlantsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout and initialize binding
        binding = FragmentPlantListBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("PlantListFragment", "onViewCreated called")

        binding.closeButton.visibility = View.VISIBLE

        // Set up RecyclerView and Adapter
        adapter = PlantsAdapter(plantList)
        binding.plantsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.plantsRecyclerView.adapter = adapter

        // Hardcoded data for testing purposes
        val hardcodedPlants = listOf(
            Plant(1, 1, 1, "Plant 1"),
            Plant(2, 2, 1, "Plant 2"),
            Plant(3, 3, 1, "Plant 3")
        )

        // Update RecyclerView with the hardcoded data
        plantList.clear()
        plantList.addAll(hardcodedPlants)
        adapter.notifyDataSetChanged()

        binding.closeButton.setOnClickListener {
            // Close the fragment when the X button is clicked
            Log.d("PlantListFragment", "Close button clicked")
            activity?.supportFragmentManager?.popBackStack()
        }
        // Fetch the plants asynchronously
//        getUserPlants()
    }

    // When a plant is selected from RecyclerView
    private fun onPlantSelected(plant: Plant) {

        val intent = Intent(requireContext(), PlantHealthCheckActivity::class.java)
        intent.putExtra("selectedPlant", plant)  // Send selected plant data back
        startActivity(intent)

        // Optionally, pop the fragment off the stack to go back
        activity?.supportFragmentManager?.popBackStack()
    }

    /* COMMENTED OUT FIRST BECAUSE BE NOT YET IMPLEMENTED
    // Fetch user's plants using API service
    private fun getUserPlants() {
        // This should be asynchronous to avoid blocking the main thread
        Thread {
            val plants = PlantApiService.getPlants() // Assuming this is synchronous

            activity?.runOnUiThread {
                if (plants.isNotEmpty()) {
                    plantList.clear()
                    plantList.addAll(plants)
                    adapter.notifyDataSetChanged()
                } else {
                    showToast("No plants found")
                }
            }
        }.start()
    }
    */

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
