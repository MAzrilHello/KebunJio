package iss.nus.edu.sg.sa4106.kebunjio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.data.Reminder
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ItemTrackerBinding
import java.time.format.DateTimeFormatter

class ReminderGroupAdapter(private var groupedReminders: MutableMap<String, List<Reminder>>) :
    RecyclerView.Adapter<ReminderGroupAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTrackerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(section: String, reminders: List<Reminder>) {
            binding.sectionTitle.text = section
            val formatter = DateTimeFormatter.ofPattern("hh:mm a")
            binding.reminderDetails.text = reminders.joinToString("\n") { "${it.reminderDateTime.format(formatter)} - ${it.reminderType}" }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrackerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = groupedReminders.keys.toList()[position]
        holder.bind(section, groupedReminders[section] ?: emptyList())
    }

    override fun getItemCount(): Int = groupedReminders.size

    fun updateData(newData: MutableMap<String, List<Reminder>>) {
        groupedReminders = newData
        notifyDataSetChanged()
    }
}