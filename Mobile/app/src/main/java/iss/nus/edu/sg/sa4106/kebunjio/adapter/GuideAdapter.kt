package iss.nus.edu.sg.sa4106.kebunjio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.data.EdiblePlantSpecies

class GuideAdapter(private val guides: List<EdiblePlantSpecies>) :
    RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_guide, parent, false)
        return GuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val plant = guides[position]
        holder.plantNameTextView.text = plant.name
        holder.plantGroupTextView.text = plant.ediblePlantGroup
        holder.guideInfoTextView.text = plant.description
    }

    override fun getItemCount(): Int = guides.size

    class GuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantNameTextView: TextView = itemView.findViewById(R.id.plantNameTextView)
        val plantGroupTextView: TextView = itemView.findViewById(R.id.plantGroupTextView)
        val guideInfoTextView: TextView = itemView.findViewById(R.id.guideInfoTextView)
    }
}
