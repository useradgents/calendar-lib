package com.useradgents.calendarlib

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.TableRow
import android.widget.Toast
import kotlinx.android.synthetic.main.month.view.*
import java.text.SimpleDateFormat
import java.util.*

class MonthView : ConstraintLayout {
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

        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.MONDAY

        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 10)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        cal.add(Calendar.MONTH, 5)//TODO remove

        val baseMonth = cal.get(Calendar.MONTH)

        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_MONTH, -1)
        }

        val nbWeeksOfMonth = cal.getActualMaximum(Calendar.WEEK_OF_MONTH)
        Log.e(TAG, "nbWeeksOfMonth=$nbWeeksOfMonth")

        (0 until nbWeeksOfMonth).forEach { l ->
            val row = LayoutInflater.from(context).inflate(R.layout.row, monthTable, false) as TableRow
            (0 until 7).forEach { r ->
                Log.i(TAG, "[$l,$r] test=${cal.time.fullFormat()}")
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
