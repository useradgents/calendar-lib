package com.useradgents.calendarlib.view
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import com.useradgents.calendarlib.R
import java.util.*




class DayView : AppCompatTextView {

    lateinit var date: Date
    private var listener: ((Date, View) -> Unit)? = null
    private var tint: ColorStateList? = null

    internal var selectedColor: Int = 0
        set(value) {
            field = value
            tint = ColorStateList(
                    arrayOf(EMPTY_STATE_SET),
                    intArrayOf(field))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                backgroundTintList = tint
            } else {
                ViewCompat.setBackgroundTintList(this, tint)
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
        val a = context?.theme?.obtainStyledAttributes(attrs, R.styleable.DayView, 0, 0)
        try {
            val label = a?.getString(R.styleable.DayView_day_text)
            text = label
        } finally {
            a?.recycle()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 5F
        }
        gravity = Gravity.CENTER
        setOnClickListener { listener?.invoke(date, this) }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val r = resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42f, r.displayMetrics)
        setMeasuredDimension(getMeasurement(widthMeasureSpec, px.toInt()),
                getMeasurement(heightMeasureSpec, px.toInt()))
    }

    private fun getMeasurement(measureSpec: Int, preferred: Int): Int {
        val specSize = MeasureSpec.getSize (measureSpec)

        return when (MeasureSpec.getMode(measureSpec)) {
            MeasureSpec.EXACTLY ->
                // This means the width of this view has been given.
                specSize
            MeasureSpec.AT_MOST ->
                // Take the minimum of the preferred size and what
                // we were told to be.
                Math.min(preferred, specSize)
            MeasureSpec.UNSPECIFIED -> preferred
            else -> preferred
        }
    }

    fun setText(s: String) {
        text = s
    }

//    fun getText(): CharSequence = text

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) {
            (Color.BLACK)
        } else {
            (disabledColor)
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
            (Color.BLACK)
        }
        invalidate()
    }

    fun setFirstDay() {
        isSelected = true
        if (isEnabled) {
            setBackgroundResource(R.drawable.start)
            (Color.BLACK)
        }
        invalidate()
    }

    fun setLastDay() {
        isSelected = true
        if (isEnabled) {
            setBackgroundResource(R.drawable.end)
            (Color.BLACK)
        }
        invalidate()
    }

    fun setDayBetween() {
        isSelected = true
        if (isEnabled) {
            setBackgroundResource(R.drawable.between)
            (Color.BLACK)
        }
        invalidate()
    }

    fun setNotSelected() {
        isSelected = false
        if (isEnabled) {
            setBackgroundColor(Color.TRANSPARENT)
            (Color.BLACK)
        }
        invalidate()
    }

    var displayedInMonth: Int = 0
    var displayedInYear: Int = 0
}