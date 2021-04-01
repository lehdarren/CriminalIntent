package com.example.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.sql.Time
import java.util.*

private const val ARG_DATE = "date"

class DatePickerFragment : DialogFragment(){

    interface Callbacks {
        fun onDateSelected(date: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var chosenYear: Int = 0
        var chosenMonth: Int = 0
        var chosenDay: Int = 0

        val dateListener = DatePickerDialog.OnDateSetListener{
                _:DatePicker,year:Int, month:Int, day:Int ->

            chosenYear = year - 1900
            chosenMonth = month
            chosenDay = day
        }

        val timeListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            val resultTime = Date(chosenYear, chosenMonth, chosenDay, hour, minute)

            targetFragment?.let {fragment ->
                (fragment as Callbacks).onDateSelected(resultTime)
            }
        }

        val calendar = Calendar.getInstance()

        val date = arguments?.getSerializable(ARG_DATE) as Date
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context, timeListener, hour, minute,
            DateFormat.is24HourFormat(context))
        timePickerDialog.show()

        return DatePickerDialog(
                requireContext(),
                dateListener,
                initialYear,
                initialMonth,
                initialDay
        )
    }

    companion object {
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }

            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}