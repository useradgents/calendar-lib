package com.useradgents.calendarlib.view

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.useradgents.calendarlib.R
import com.useradgents.calendarlib.adapter.CalendarAdapter
import com.useradgents.calendarlib.controller.CalendarController
import com.useradgents.calendarlib.controller.CalendarControllerInterface
import com.useradgents.calendarlib.controller.CalendarSingleController
import com.useradgents.calendarlib.controller.CalendarViewInterface
import kotlinx.android.synthetic.main.calendar.view.*
import java.util.*


class CalendarView : FrameLayout, CalendarViewInterface {

    companion object {
        const val NUMBER_OF_MONTHS = 12
    }

    private var onDateSelectedListener: ((Date) -> Unit)? = null
    private var onRangeSelectedListener: ((Date, Date) -> Unit)? = null
    private var onUnavailableDate: (() -> Unit)? = null
    private lateinit var controller: CalendarControllerInterface
    private lateinit var adapter: CalendarAdapter

    private var nbMonth: Int = -1
    private var disabledColor: Int = 0
    private var selectedColor: Int = 0
    private var selectedTextColor: Int = 0


    var notAvailableDays: List<Date>? = null
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

    var min: Date? = null
        set (date) {
            field = date
            adapter.min = date
        }
    var max: Date? = null
        set (date) {
            field = date
            adapter.max = date
        }

    var selectedDates: Pair<Date?, Date?>? = null
        set (date) {
            val cal = Calendar.getInstance()
            cal.time = date?.first
            cal.set(Calendar.HOUR_OF_DAY, 10)            // set hour to midnight
            cal.set(Calendar.MINUTE, 0)                 // set minute in hour
            cal.set(Calendar.SECOND, 0)                 // set second in minute
            cal.set(Calendar.MILLISECOND, 0)
            val firstDate = cal.time

            cal.time = date?.second
            cal.set(Calendar.HOUR_OF_DAY, 10)            // set hour to midnight
            cal.set(Calendar.MINUTE, 0)                 // set minute in hour
            cal.set(Calendar.SECOND, 0)                 // set second in minute
            cal.set(Calendar.MILLISECOND, 0)
            val secondDate = cal.time

            controller.setSelectedDates(firstDate to secondDate)
            field = date
        }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.calendar, this, true)
        val a = context?.theme?.obtainStyledAttributes(attrs, R.styleable.CalendarView, 0, 0)
        try {
            nbMonth = a?.getInteger(R.styleable.CalendarView_nb_month, NUMBER_OF_MONTHS) ?: NUMBER_OF_MONTHS
            disabledColor = a?.getColor(R.styleable.CalendarView_disabled_color, Color.parseColor("#55555555")) ?: Color.parseColor("#55555555")
            selectedTextColor = a?.getColor(R.styleable.CalendarView_selected_text_color, Color.parseColor("#FF000000")) ?: Color.parseColor("#FF000000")
            val mode = a?.getInt(R.styleable.CalendarView_cv_pick_mode, 0)
            controller = if (mode == 0) {
                CalendarController(this)
            } else {
                CalendarSingleController(this)
            }
        } finally {
            a?.recycle()
        }
        selectedColor = fetchAccentColor(attrs)

        val linearManager = LinearLayoutManager(context)
        adapter = CalendarAdapter({ date, _ -> controller.onDayClicked(date) }, selectedColor, disabledColor, selectedTextColor)

        val dividerItemDecoration = DividerItemDecoration(context,
                linearManager.orientation)

        adapter.items = (0 until nbMonth).toMutableList()
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

    fun setOnUnavailableDate(listener: (() -> Unit)?) {
        onUnavailableDate = listener
    }

    override fun onSecondDateSet(firstDate: Date, secondDate: Date) {
        adapter.setSelectedDays(firstDate, secondDate)
        onRangeSelectedListener?.invoke(firstDate, secondDate)
    }

    override fun onFirstDateSet(date: Date) {
        adapter.setSelectedDays(date, null)
        onDateSelectedListener?.invoke(date)
    }

    override fun onUnavailableDate() {
        onUnavailableDate?.invoke()
    }

    private fun fetchAccentColor(attrs: AttributeSet?): Int {
        val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CalendarView)

        var color = a.getColor(R.styleable.CalendarView_selected_color, Color.BLACK)
        a.recycle()

        if (color == Color.BLACK) {
            val typedValue = TypedValue()
            val b = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
            color = b.getColor(0, 0)
            b.recycle()
        }

        return color
    }
}
