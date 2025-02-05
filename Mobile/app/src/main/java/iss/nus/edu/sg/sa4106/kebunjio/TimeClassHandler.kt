package iss.nus.edu.sg.sa4106.kebunjio

import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context

class TimeClassHandler(timeTextView: TextView, changeDateBtn: Button, changeTimeBtn: Button, context: Context) {

    lateinit var timeTextView: TextView
    lateinit var changeDateBtn: Button
    lateinit var changeTimeBtn: Button
    lateinit var context: Context
    public var pattern = "yyyy-MM-dd'T'hh:mm:ss"

    init {
        this.timeTextView = timeTextView
        this.changeDateBtn = changeDateBtn
        this.changeTimeBtn = changeTimeBtn
        this.context = context
        this.timeTextView.text = pattern

        this.changeDateBtn.setOnClickListener {
            changeDateTimePopup(true)
        }
        this.changeTimeBtn.setOnClickListener {
            changeDateTimePopup(false)
        }
    }

    private fun makeFormatter(): SimpleDateFormat {
        return SimpleDateFormat(pattern, Locale.ENGLISH)
    }


    private fun changeDateTimePopup(dateNotTime: Boolean) {
        val c = getDate()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        if (dateNotTime) {
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                context,
                { view, year, monthOfYear, dayOfMonth ->
                    changeDate(year,monthOfYear,dayOfMonth)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        } else {
            val timePickerDialog = TimePickerDialog(
                context,
                { view, hourOfDay, minute ->
                    changeTime(hourOfDay,minute)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }
    }

    public fun changeDate(year: Int, month: Int, dayOfMonth: Int) {
        var currentDate = getDate()
        currentDate.set(Calendar.YEAR,year)
        currentDate.set(Calendar.MONTH,month)
        currentDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
        currentDate.set(Calendar.SECOND,0)
        setDate(currentDate)
    }

    public fun changeTime(hour: Int, minute: Int) {
        var currentDate = getDate()
        currentDate.set(Calendar.HOUR_OF_DAY,hour)
        currentDate.set(Calendar.MINUTE,minute)
        currentDate.set(Calendar.SECOND,0)
        setDate(currentDate)
    }

    public fun setDate(newDate: Calendar) {
        timeTextView.text = makeFormatter().format(newDate.time)
    }

    public fun gotValidDate(): Boolean {
        val dateText = timeTextView.text.toString()

        var thisCalendar = Calendar.getInstance()

        try {
            val sdf = makeFormatter()
            thisCalendar.time = sdf.parse(dateText)!!
            return true
        } catch (_: Exception) {
            return false
        }
    }

    public fun getDate(): Calendar {
        val dateText = timeTextView.text.toString()

        var thisCalendar = Calendar.getInstance()

        try {
            val sdf = makeFormatter()
            thisCalendar.time = sdf.parse(dateText)!!
        } catch (_: Exception) {
        }

        thisCalendar.set(Calendar.SECOND,0)

        Log.d("Got Date:",thisCalendar.toString())

        return thisCalendar
    }

}