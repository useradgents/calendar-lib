package com.useradgents.calendarlib.controller

import java.util.*

interface CalendarControllerInterface {
    var dateList: List<Date>?
    fun onDayClicked(date: Date)
    fun setSelectedDates(dates: Pair<Date?, Date?>)
}

