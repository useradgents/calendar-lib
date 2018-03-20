package com.useradgents.calendarlib.view

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.useradgents.calendarlib.R
import com.useradgents.calendarlib.adapter.CalendarAdapter
import com.useradgents.calendarlib.controller.CalendarController
import com.useradgents.calendarlib.controller.CalendarViewInterface
import kotlinx.android.synthetic.main.calendar.view.*
import java.util.*
import kotlin.collections.ArrayList


class CalendarView : FrameLayout, CalendarViewInterface {

    companion object {
        const val NUMBER_OF_MONTHS = 12
    }

    private var onDateSelectedListener: ((Date) -> Unit)? = null
    private var onRangeSelectedListener: ((Date, Date) -> Unit)? = null
    private lateinit var controller: CalendarController
    private lateinit var adapter: CalendarAdapter

    var notAvailableDays: List<Date> ?= null
        set(list) {
            val listAtSameTime = list?.map {
                val cal = Calendar.getInstance()       // get calendar instance
                cal.time = it
                cal.set(Calendar.HOUR_OF_DAY, 10)            // set hour to midnight
                cal.set(Calendar.MINUTE, 0)                 // set minute in hour
                cal.set(Calendar.SECOND, 0)                 // set second in minute
                cal.set(Calendar.MILLISECOND, 0)
                cal.time
            }
            field = listAtSameTime
            controller.dateList = listAtSameTime
            adapter.dateList = listAtSameTime
        }

    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        controller = CalendarController(this)

        LayoutInflater.from(context).inflate(R.layout.calendar, this, true)
        //val a = context?.theme?.obtainStyledAttributes(attrs, R.styleable.DayView, 0, 0)
//        try {
//            val label = a?.getString(R.styleable.DayView_day_text)
//        } finally {
//            a?.recycle()
//        }
        val linearManager = LinearLayoutManager(context)
        adapter = CalendarAdapter({ date, day -> controller.onDayClicked(date, day) })

        val dividerItemDecoration = DividerItemDecoration(context,
                linearManager.orientation)

        adapter.items = (0 until NUMBER_OF_MONTHS).toMutableList()
        calendarRecyclerView.layoutManager = linearManager
        calendarRecyclerView.adapter = adapter
        calendarRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    fun setOnDateSelectedListener(listener: ((Date) -> Unit)?) {
        onDateSelectedListener = listener
    }

    fun setOnRangeSelectedListener(listener: ((Date, Date) -> Unit)?) {
        onRangeSelectedListener = listener
    }

    override fun onSecondDateSet(firstDate: Date, secondDate: Date) {
        val list = ArrayList<Date>()
        val cal = Calendar.getInstance()
        cal.time = firstDate
        cal.set(Calendar.HOUR_OF_DAY, 10)            // set hour to midnight
        cal.set(Calendar.MINUTE, 0)                 // set minute in hour
        cal.set(Calendar.SECOND, 0)                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0)

        while (cal.time.before(secondDate)) {
            list.add(cal.time)
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }

        list.add(secondDate)

        adapter.selectedDays = list
        onRangeSelectedListener?.invoke(firstDate, secondDate)
    }

    override fun onFirstDateSet(date: Date) {
        adapter.selectedDays = arrayListOf(date)
        onDateSelectedListener?.invoke(date)
    }
}