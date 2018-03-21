package com.useradgents.calendarlib.view

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TableRow
import com.useradgents.calendarlib.R
import kotlinx.android.synthetic.main.month.view.*
import java.text.SimpleDateFormat
import java.util.*

class MonthView : FrameLayout {
    private val TAG = "MonthView"

    private lateinit var uiHandler: Handler
    private lateinit var workerHandler: Handler
    var disabledDays: List<Date>? = null
    var selectedDays: List<Date>? = null
    private lateinit var workerThread: HandlerThread
    private val viewList = ArrayList<DayView>()

    private var baseMonth: Int = 0
    private var cal: Calendar = Calendar.getInstance()

    private var listener: ((Date, View) -> Unit)? = null


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
        workerThread = HandlerThread("MonthWorker")
        workerThread.start()
        workerHandler = Handler(workerThread.looper)
        uiHandler = Handler(Looper.getMainLooper())
        LayoutInflater.from(context).inflate(R.layout.month, this, true)
    }


    fun setDeltaMonth(month: Int) {
        cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.MONDAY

        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 10)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        cal.add(Calendar.MONTH, month)

        baseMonth = cal.get(Calendar.MONTH)
        val nbDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        monthName.text = cal.time.month()

        var dayOffset = 0
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            dayOffset++
            cal.add(Calendar.DAY_OF_MONTH, -1)
        }

        monthTable.removeAllViews()
        viewList.clear()
        val nbLines = ((nbDayOfMonth + dayOffset) / 7) + if ((nbDayOfMonth + dayOffset) % 7 == 0) 0 else 1
        workerHandler.removeCallbacksAndMessages(null)
        uiHandler.removeCallbacksAndMessages(null)
        progressBar.visibility = View.VISIBLE
        monthTable.visibility = View.INVISIBLE
        workerHandler.post {
            val rows = mutableListOf<TableRow>()
            (0 until nbLines).forEach { l ->
                val row = LayoutInflater.from(context).inflate(R.layout.row, monthTable, false) as TableRow
                (0 until 7).forEach { r ->
                    //                Log.i(TAG, "[$l,$r] test=${cal.time.fullFormat()}")
                    val dayView = DayView(context, cal.time)
                    viewList.add(dayView)

                    if (cal.get(Calendar.MONTH) == baseMonth) {
                        dayView.setText(cal.time.dayOfMonth())
                        if (disabledDays?.firstOrNull { it.time == cal.time.time } != null && dayView.getText().isNotEmpty()) {
                            dayView.isEnabled = false
                        } else if (selectedDays?.firstOrNull { it.time == cal.time.time } != null && dayView.getText().isNotEmpty()) {
                            dayView.isSelected = true
                        }
                    } else {
                        dayView.isEnabled = false
                    }
                    dayView.onClickListener { date, view ->
                        listener?.invoke(date, view)
                    }

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

    private fun updateView(dayView: DayView) {
        if (dayView.getText().isNotEmpty()) {
            dayView.isEnabled = disabledDays?.firstOrNull { it.time == dayView.date.time } == null
            dayView.isSelected = selectedDays?.firstOrNull { it.time == dayView.date.time } != null
        }
    }

    fun setOnClickListener(listener: (Date, View) -> Unit) {
        this.listener = listener
    }

    fun refresh(field: List<Date>?) {
        selectedDays = field
        viewList.forEach { updateView(it) }
    }
}

fun Date.fullFormat(): String =
        SimpleDateFormat("EEE yyyy-MM-dd 'at' HH:mm:ss z", Locale.getDefault()).format(this)

fun Date.day(): String = SimpleDateFormat("EEE yyyy-MM-dd", Locale.getDefault()).format(this)


fun Date.dayOfMonth(): String = SimpleDateFormat("d", Locale.getDefault()).format(this)

fun Date.month(): String = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(this)
