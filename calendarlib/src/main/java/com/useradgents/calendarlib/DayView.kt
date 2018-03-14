package com.useradgents.calendarlib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.day.view.*
import java.util.*

class DayView : FrameLayout {

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
        LayoutInflater.from(context).inflate(R.layout.day, this, true)
        val a = context?.theme?.obtainStyledAttributes(attrs, R.styleable.DayView, 0, 0)
        try {
            val label = a?.getString(R.styleable.DayView_day_text)
            day.text = label
        } finally {
            a?.recycle()
        }
        setOnClickListener { listener?.invoke(date) }
    }

    fun setText(s: String) {
        day.text = s
    }

    fun onClickListener(listener: (Date) -> Unit) {
        this.listener = listener
    }
}