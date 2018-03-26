package com.useradgents.calendarlib.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
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
    internal var selectedColor: Int = 0
        set(value) {
            field = value
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                backgroundTintList = ColorStateList(
                        arrayOf(EMPTY_STATE_SET),
                        intArrayOf(field))
            }
        }
    internal var selectedTextColor: Int = 0
    internal var disabledColor: Int = 0

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 5F
        }
        setOnClickListener { listener?.invoke(date, this) }
    }

    fun setText(s: String) {
        day.text = s
    }

    fun getText(): CharSequence = day.text

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) {
            day.setTextColor(Color.BLACK)
        } else {
            day.setTextColor(disabledColor)
        }
        invalidate()
    }

    fun onClickListener(listener: (Date, View) -> Unit) {
        this.listener = listener
    }

    fun setOnlyOneSelected() {
        isSelected = true
        if (isEnabled) {
            setBackgroundResource(R.drawable.single)
            day.setTextColor(Color.BLACK)
        }
        invalidate()
    }

    fun setFirstDay() {
        isSelected = true
        if (isEnabled) {
            setBackgroundResource(R.drawable.start)
            day.setTextColor(Color.BLACK)
        }
        invalidate()
    }

    fun setLastDay() {
        isSelected = true
        if (isEnabled) {
            setBackgroundResource(R.drawable.end)
            day.setTextColor(Color.BLACK)
        }
        invalidate()
    }

    fun setDayBetween() {
        isSelected = true
        if (isEnabled) {
            setBackgroundResource(R.drawable.between)
            day.setTextColor(Color.BLACK)
        }
        invalidate()
    }

    fun setNotSelected() {
        isSelected = false
        if (isEnabled) {
            setBackgroundColor(Color.TRANSPARENT)
            day.setTextColor(Color.BLACK)
        }
        invalidate()
    }

    var displayedInMonth: Int = 0
    var displayedInYear: Int = 0
}