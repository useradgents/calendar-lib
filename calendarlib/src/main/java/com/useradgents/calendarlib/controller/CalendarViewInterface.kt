package com.useradgents.calendarlib.controller

import java.util.*

interface CalendarViewInterface {
    fun onFirstDateSet(date: Date)
    fun onSecondDateSet(firstDate: Date, secondDate: Date)
}