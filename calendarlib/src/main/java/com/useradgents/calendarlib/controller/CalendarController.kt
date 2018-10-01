package com.useradgents.calendarlib.controller

import java.util.*

class CalendarController constructor(private val calendarView: CalendarViewInterface) : CalendarControllerInterface {
    private var firstDate: Date? = null
    private var secondDate: Date? = null

    override fun setSelectedDates(dates: Pair<Date?, Date?>) {
        let { onDaySet(dates) }
    }

    private fun onDaySet(dates: Pair<Date?, Date?>) {
        if ((dates.first != null && dates.second != null)) {
            calendarView.onSecondDateSet(dates.first!!, dates.second!!)
            secondDate = null
        } else if (dates.first != null && dates.second == null) {
            calendarView.onFirstDateSet(dates.first!!)
        }
    }

    override var dateList: List<Date>? = null

    override fun onDayClicked(date: Date) {
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

