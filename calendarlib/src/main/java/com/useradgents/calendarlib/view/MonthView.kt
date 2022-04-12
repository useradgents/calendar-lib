package com.useradgents.calendarlib.view

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.useradgents.calendarlib.R
import java.text.SimpleDateFormat
import java.util.*

class MonthView : FrameLayout {
    private lateinit var uiHandler: Handler
    private lateinit var workerHandler: Handler
    var disabledDays: List<Date>? = null
    //var selectedDays: List<Date>? = null

    var firstSelectedDays: Date? = null
    var secondSelectedDays: Date? = null

    var min: Date? = null
    var max: Date? = null

    private lateinit var workerThread: HandlerThread
    private val viewList = ArrayList<DayView>()

    private var baseMonth: Int = 0
    private var cal: Calendar = Calendar.getInstance()

    private var listener: ((Date, View) -> Unit)? = null

    var selectedColor: Int = 0
    var disabledColor: Int = 0
    var selectedTextColor: Int = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }


    private fun init(context: Context?, @Suppress("UNUSED_PARAMETER") attrs: AttributeSet?) {
        workerThread = HandlerThread("MonthWorker")
        workerThread.start()
        workerHandler = Handler(workerThread.looper)
        uiHandler = Handler(Looper.getMainLooper())
        LayoutInflater.from(context).inflate(R.layout.month, this, true)
    }


    fun setDeltaMonth(min: Date?, month: Int) {
        cal = Calendar.getInstance()
        min?.let {
            cal.time = it
        }
        cal.firstDayOfWeek = Calendar.MONDAY

        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 10)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        cal.add(Calendar.MONTH, month)

        baseMonth = cal.get(Calendar.MONTH)
        val baseYear = cal.get(Calendar.YEAR)
        val nbDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        findViewById<TextView>(R.id.monthName).text = cal.time.month()

        var dayOffset = 0
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            dayOffset++
            cal.add(Calendar.DAY_OF_MONTH, -1)
        }

        val monthTable = findViewById<TableLayout>(R.id.monthTable)
        monthTable.removeAllViews()
        viewList.clear()
        val nbLines = ((nbDayOfMonth + dayOffset) / 7) + if ((nbDayOfMonth + dayOffset) % 7 == 0) 0 else 1
        workerHandler.removeCallbacksAndMessages(null)
        uiHandler.removeCallbacksAndMessages(null)
        val progressBar = findViewById<View>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        monthTable.visibility = View.INVISIBLE
        workerHandler.post {
            val rows = mutableListOf<TableRow>()
            (0 until nbLines).forEach {
                val row = LayoutInflater.from(context).inflate(R.layout.row, monthTable, false) as TableRow
                (0 until 7).forEach {
                    val dayView = DayView(context, cal.time)
                    dayView.selectedColor = selectedColor
                    dayView.disabledColor = disabledColor
                    dayView.selectedTextColor = selectedTextColor
                    dayView.displayedInMonth = baseMonth
                    dayView.displayedInYear = baseYear
                    viewList.add(dayView)

                    if (cal.get(Calendar.MONTH) == baseMonth) {
                        dayView.setText(cal.time.dayOfMonth())

                        if (max == null && min != null) {
                            uiHandler.post {
                                dayView.isEnabled = cal.time.after(min)
                                if (isDateSelected(dayView)) {
                                    dayView.setTextColor(Color.WHITE)
                                }
                            }
                        } else if (min == null && max != null) {
                            uiHandler.post {
                                dayView.isEnabled = cal.time.before(max)
                                if (isDateSelected(dayView)) {
                                    dayView.setTextColor(Color.WHITE)
                                }
                            }
                        } else if (min != null && max != null) {
                            uiHandler.post {
                                dayView.isEnabled = cal.time.before(max) && cal.time.after(min)
                                if (isDateSelected(dayView)) {
                                    dayView.setTextColor(Color.WHITE)
                                }
                            }
                        }

                        if (disabledDays?.firstOrNull { it.time == cal.time.time } != null
                            && dayView.text.isNotEmpty()) {
                            uiHandler.post {
                                dayView.isEnabled = false
                                if (isDateSelected(dayView)) {
                                    dayView.setTextColor(Color.WHITE)
                                }
                            }
                        }


                        dayView.onClickListener { date, view ->
                            listener?.invoke(date, view)
                        }
                    }
                    updateCellState(dayView.date, dayView)
                    row.addView(dayView)
                    cal.add(Calendar.DAY_OF_MONTH, 1)
                }
                rows.add(row)
            }

            uiHandler.post {
                rows.forEach {
                    monthTable.addView(it)
                }
                progressBar.visibility = View.GONE
                monthTable.visibility = View.VISIBLE
            }
        }
    }

    private fun updateCellState(time: Date?, dayView: DayView) {
        if (dayView.text.isNotEmpty()) {
            when (checkIfDateSelected(time)) {
                SelectionState.ONLY_ONE_SELECTED -> dayView.setOnlyOneSelected()
                SelectionState.FIRST_DAY -> dayView.setFirstDay()
                SelectionState.LAST_DAY -> dayView.setLastDay()
                SelectionState.DAY_BETWEEN -> dayView.setDayBetween()
                SelectionState.NOT_SELECTED -> dayView.setNotSelected()
            }
        } else if (firstSelectedDays != null && secondSelectedDays != null) {
            val cal = Calendar.getInstance()
            cal.time = firstSelectedDays
            val firstDayMonth = cal.get(Calendar.MONTH)
            val firstDayYear = cal.get(Calendar.YEAR)
            cal.time = secondSelectedDays
            val secondDayMonth = cal.get(Calendar.MONTH)
            cal.time = dayView.date
            if (firstDayMonth != secondDayMonth && ((dayView.date.after(firstSelectedDays) && dayView.displayedInMonth == firstDayMonth)
                            || (dayView.date.before(secondSelectedDays) && dayView.displayedInMonth == secondDayMonth)
                            || (dayView.displayedInMonth in (firstDayMonth + 1)..(secondDayMonth - 1))
                            || (dayView.date.before(secondSelectedDays) && dayView.displayedInYear > firstDayYear))) {
                dayView.setDayBetween()
            } else {
                dayView.setNotSelected()
            }
        } else {
            dayView.setNotSelected()
        }
    }

    private fun isDateSelected(dayView: DayView): Boolean {
        if (firstSelectedDays == null || secondSelectedDays == null) {
            return false
        }

        val isBetweenSelectedDates =
            dayView.date.after(firstSelectedDays) && dayView.date.before(secondSelectedDays)
        val isBetweenMinMax = dayView.date.after(min) && dayView.date.before(max)
        val isFirstOrLastSelected =
            dayView.date.day() == secondSelectedDays?.day() || dayView.date.day() == firstSelectedDays?.day()
        val isMinOrMax = dayView.date.day() == max?.day() || dayView.date.day() == min?.day()

        return (isBetweenSelectedDates && isBetweenMinMax) || isFirstOrLastSelected || isMinOrMax
    }


    private fun checkIfDateSelected(time: Date?): SelectionState {
        return when {
            firstSelectedDays == null -> SelectionState.NOT_SELECTED
            secondSelectedDays == null -> {
                if (firstSelectedDays == time)
                    SelectionState.ONLY_ONE_SELECTED
                else
                    SelectionState.NOT_SELECTED
            }
            time == firstSelectedDays && time != secondSelectedDays -> SelectionState.FIRST_DAY
            time != firstSelectedDays && time == secondSelectedDays -> SelectionState.LAST_DAY
            time == firstSelectedDays && time == secondSelectedDays -> SelectionState.ONLY_ONE_SELECTED
            else -> {
                if (time?.after(firstSelectedDays) == true && time.before(secondSelectedDays))
                    SelectionState.DAY_BETWEEN
                else
                    SelectionState.NOT_SELECTED
            }
        }
    }

    private fun updateView(dayView: DayView) {
        if (max == null && min != null) {
            dayView.isEnabled = dayView.date.after(min)
        } else if (min == null && max != null) {
            dayView.isEnabled = dayView.date.before(max)
        } else if (min != null && max != null) {
            dayView.isEnabled = dayView.date.before(max) && dayView.date.after(min)
        }
        if (disabledDays?.firstOrNull { it.time == dayView.date.time } != null
                && dayView.getText().isNotEmpty()) {
            dayView.isEnabled = false
        }

        updateCellState(dayView.date, dayView)
    }

    fun setOnClickListener(listener: (Date, View) -> Unit) {
        this.listener = listener
    }

    fun refresh(firstSelected: Date?, secondSelected: Date?) {
        firstSelectedDays = firstSelected
        secondSelectedDays = secondSelected
        viewList.forEach { updateView(it) }
    }
}

enum class SelectionState {
    FIRST_DAY, DAY_BETWEEN, LAST_DAY, ONLY_ONE_SELECTED, NOT_SELECTED
}

@Suppress("unused")
fun Date.fullFormat(): String =
        SimpleDateFormat("EEE yyyy-MM-dd 'at' HH:mm:ss z", Locale.getDefault()).format(this)

fun Date.day(): String = SimpleDateFormat("EEE yyyy-MM-dd", Locale.getDefault()).format(this)


fun Date.dayOfMonth(): String = SimpleDateFormat("d", Locale.getDefault()).format(this)

fun Date.month(): String = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(this)
