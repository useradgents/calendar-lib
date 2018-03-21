package com.useradgents.calendarlib.controller

import android.view.View
import java.util.*

class CalendarController constructor(private val calendarView: CalendarViewInterface) {
    private var firstDate: Date? = null
    private var secondDate: Date? = null

    var dateList: List<Date>? = null

    fun onDayClicked(date: Date, view: View) {
        if (firstDate == null || (firstDate != null && secondDate != null)) {
            firstDate = date
            calendarView.onFirstDateSet(date)
            secondDate = null
        } else {
            if (date.before(firstDate)) {
                firstDate = date
                calendarView.onFirstDateSet(date)
            } else if (date != firstDate) {
                 if (checkAllDaysBetweenAvailability(firstDate, date)) {
                     secondDate = date
                     firstDate?.let {
                         calendarView.onSecondDateSet(it, date)
                     }
                 } else {
                     firstDate = date
                     calendarView.onFirstDateSet(date)
                     secondDate = null
                 }
            }
        }
    }

    private fun checkAllDaysBetweenAvailability(firstDate: Date?, secondDate: Date?): Boolean {
        if (firstDate == null || secondDate == null) {
            return false
        }
        return dateList?.none { it == firstDate || it == secondDate || (it.after(firstDate) && it.before(secondDate)) } == true
    }
}

