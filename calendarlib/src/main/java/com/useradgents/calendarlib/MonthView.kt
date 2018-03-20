package com.useradgents.calendarlib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.Toast
import kotlinx.android.synthetic.main.month.view.*
import java.text.SimpleDateFormat
import java.util.*

class MonthView : LinearLayout {
    private val TAG = "MonthView"

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
        LayoutInflater.from(context).inflate(R.layout.month, this, true)
        orientation = LinearLayout.VERTICAL
    }

    fun setDeltaMonth(month: Int) {
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.MONDAY

        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 10)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        cal.add(Calendar.MONTH, month)

        val baseMonth = cal.get(Calendar.MONTH)
        val nbDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        Log.e(TAG, "nbDayOfMonth=$nbDayOfMonth pour ${cal.time.fullFormat()}")
        monthName.text = cal.time.month()

        var dayOffset = 0
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            dayOffset++
            cal.add(Calendar.DAY_OF_MONTH, -1)
        }

        monthTable.removeAllViews()
        val nbLines = ((nbDayOfMonth + dayOffset) / 7) + if((nbDayOfMonth + dayOffset) % 7 == 0) 0 else 1
        (0 until nbLines).forEach { l ->
            val row = LayoutInflater.from(context).inflate(R.layout.row, monthTable, false) as TableRow
            (0 until 7).forEach { r ->
//                Log.i(TAG, "[$l,$r] test=${cal.time.fullFormat()}")
                val dayView = DayView(context, cal.time)
                if (cal.get(Calendar.MONTH) == baseMonth) {
                    dayView.setText(cal.time.dayOfMonth())
                } else {
                    dayView.isEnabled = false
                }
                dayView.onClickListener {
                    Toast.makeText(context, it.day(), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "clicked: ${it.day()}")
                }
                row.addView(dayView)
                cal.add(Calendar.DAY_OF_MONTH, 1)
            }
            monthTable.addView(row)
        }
    }
}

fun Date.fullFormat(): String =
        SimpleDateFormat("EEE yyyy-MM-dd 'at' HH:mm:ss z", Locale.getDefault()).format(this)

fun Date.day(): String = SimpleDateFormat("EEE yyyy-MM-dd", Locale.getDefault()).format(this)


fun Date.dayOfMonth(): String = SimpleDateFormat("d", Locale.getDefault()).format(this)

fun Date.month(): String = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(this)
