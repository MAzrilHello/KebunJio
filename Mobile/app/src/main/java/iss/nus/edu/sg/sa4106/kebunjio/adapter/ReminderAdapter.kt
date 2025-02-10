package iss.nus.edu.sg.sa4106.kebunjio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import iss.nus.edu.sg.sa4106.kebunjio.R

class ReminderAdapter(
    private val context: Context,
    private val dataList: List<String> // List of data for the spinner
) : BaseAdapter() {

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            // Inflate the custom layout for each spinner item
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        }

        val textView = view?.findViewById<TextView>(R.id.spinnerText)
        textView?.text = dataList[position] // Set the text for the item in the spinner

        return view ?: convertView!! // Safely return either the inflated view or the recycled one
    }

    // For the dropdown view (the items shown when you click the spinner)
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent) // Reuse the same view for dropdown items
    }
}