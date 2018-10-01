package com.useradgents.calendarlib.controller

import java.util.*

class CalendarSingleController constructor(private val calendarView: CalendarViewInterface) : CalendarControllerInterface {

    override fun setSelectedDates(dates: Pair<Date?, Date?>) {}

    override var dateList: List<Date>? = null

    override fun onDayClicked(date: Date) {
        calendarView.onFirstDateSet(date)
    }
}

