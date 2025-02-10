package iss.nus.edu.sg.sa4106.kebunjio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import iss.nus.edu.sg.sa4106.kebunjio.R

class TimeSpinnerAdapter (
    context: Context,
    private val timeSlots: List<String> = (0..23).map { hour -> String.format("%02d:00", hour) }
) : ArrayAdapter<String>(context, 0, timeSlots) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent ,false)
        val textView = view.findViewById<TextView>(R.id.spinnerItemText)
        textView.text = timeSlots[position]
        return view
    }
}