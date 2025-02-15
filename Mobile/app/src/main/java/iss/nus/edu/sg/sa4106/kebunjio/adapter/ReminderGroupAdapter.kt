package iss.nus.edu.sg.sa4106.kebunjio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.data.Reminder
import java.time.format.DateTimeFormatter

class ReminderGroupAdapter(private var groupedReminders: MutableMap<String, List<Reminder>>) :
    RecyclerView.Adapter<ReminderGroupAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val sectionTitle: TextView = view.findViewById(R.id.sectionTitle)
        private val reminderContainer: LinearLayout = view.findViewById(R.id.reminderContainer)

        private val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        fun bind(section: String, reminders: List<Reminder>) {
            sectionTitle.text = section

            reminderContainer.removeAllViews()

            reminders.forEach { reminder ->
                val reminderTextView = TextView(itemView.context)
                reminderTextView.text = "${reminder.reminderDateTime.format(formatter)} - ${reminder.reminderType}"
                reminderTextView.textSize = 16f
                reminderTextView.setPadding(10, 5, 10, 5)

                reminderContainer.addView(reminderTextView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder_group, parent, false)
        return ViewHolder(view)
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