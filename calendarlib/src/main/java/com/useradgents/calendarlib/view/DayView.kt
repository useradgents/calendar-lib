package com.useradgents.calendarlib.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.useradgents.calendarlib.R
import kotlinx.android.synthetic.main.day.view.*
import java.util.*

class DayView : FrameLayout {

    lateinit var date: Date
    private var listener: ((Date, View) -> Unit)? = null

    constructor(context: Context?, date: Date) : super(context) {
        this.date = Date(date.time)
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
        setOnClickListener { listener?.invoke(date, this) }
    }

    fun setText(s: String) {
        day.text = s
    }

    fun getText() = day.text

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) {
            day.setTextColor(Color.BLACK)
        } else {
            day.setTextColor(Color.RED)
        }
        invalidate()
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            day.setBackgroundColor(Color.RED)
        } else {
            day.setBackgroundColor(Color.TRANSPARENT)
        }
        invalidate()
    }

    fun onClickListener(listener: (Date, View) -> Unit) {
        this.listener = listener
    }
}