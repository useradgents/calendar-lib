package com.useradgents.calendarlib

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.useradgents.calendarlib.adapter.CalendarAdapter
import kotlinx.android.synthetic.main.calendar.view.*
import java.util.*

class CalendarView : FrameLayout {

    private lateinit var date: Date
    private var listener: ((Date) -> Unit)? = null

    constructor(context: Context?, date: Date) : super(context) {
        this.date = date
        init(context, null)
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
        LayoutInflater.from(context).inflate(R.layout.calendar, this, true)
        //val a = context?.theme?.obtainStyledAttributes(attrs, R.styleable.DayView, 0, 0)
//        try {
//            val label = a?.getString(R.styleable.DayView_day_text)
//        } finally {
//            a?.recycle()
//        }
        setOnClickListener { listener?.invoke(date) }
        calendarRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CalendarAdapter()
        adapter.items = (0 until 24).toMutableList()
        calendarRecyclerView.adapter = adapter
    }
}