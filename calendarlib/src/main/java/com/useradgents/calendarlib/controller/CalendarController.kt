package com.useradgents.calendarlib.controller

import java.util.*

class CalendarController constructor(private val calendarView: CalendarViewInterface) {
    var firstDate: Date? = null
        set (date) {
            field = date
            date?.let { onDaySet(it) }
        }
    var secondDate: Date? = null
        set (date) {
            field = date
            date?.let { onDaySet (it) }
        }

    private fun onDaySet(date: Date) {
        if ((firstDate != null && secondDate != null)) {
            calendarView.onSecondDateSet(firstDate!!, secondDate!!)
            secondDate = null
        } else if (firstDate != null && secondDate == null) {
            calendarView.onFirstDateSet(date)
        }
    }

    var dateList: List<Date>? = null

    fun onDayClicked(date: Date) {
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
                    calendarView.onUnavailableDate()
                    secondDate = null
                }
            }
        }
    }

    private fun checkAllDaysBetweenAvailability(firstDate: Date?, secondDate: Date?): Boolean {
        if (firstDate == null || secondDate == null) {
            return false
        } else if (dateList == null || dateList?.isEmpty() == true) {
            return true
        }
        return dateList?.none { it == firstDate || it == secondDate || (it.after(firstDate) && it.before(secondDate)) } == true
    }
}

