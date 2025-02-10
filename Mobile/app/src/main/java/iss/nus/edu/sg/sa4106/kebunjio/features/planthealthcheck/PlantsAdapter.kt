package iss.nus.edu.sg.sa4106.kebunjio.features.planthealthcheck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.data.Plant

class PlantsAdapter (private val plants: List<Plant>) : RecyclerView.Adapter<PlantsAdapter.PlantViewHolder>() {

    // ViewHolder to hold each plant's view components
    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantNameTextView: TextView = itemView.findViewById(R.id.plantNameTextView)
        val plantImageView: ImageView = itemView.findViewById(R.id.plantImageView)
        val plantDetailsTextView: TextView = itemView.findViewById(R.id.plantDetailsTextView) // If you want more info
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_plant, parent, false)
        return PlantViewHolder(itemView)
    }

    // Bind the plant data to the view components
    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        holder.plantNameTextView.text = plant.name

        // If you have more details, display them
        // holder.plantDetailsTextView.text = "Details: ${plant.ediblePlantSpeciesId}"

        // Set plant image (you can use Glide or Picasso if you want to load an image)
        // Glide.with(holder.itemView.context).load(plant.imageUri).into(holder.plantImageView)
    }

    // Return the size of the plant list
    override fun getItemCount() = plants.size
}