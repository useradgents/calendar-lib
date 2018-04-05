package com.useradgents.calendarlib.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.useradgents.calendarlib.view.MonthView
import java.util.*

class CalendarAdapter(val listener: ((Date, View) -> Unit)? = null, private val selectedColor: Int, private val disabledColor: Int) : RecyclerView.Adapter<CalendarAdapter.GenericViewHolder>() {
    var items = mutableListOf<Int>()

    var dateList: List<Date>? = null
        set(list) {
            field = list
            viewList.forEach {
                it.disabledDays = list
                it.refresh(firstSelectedDays, secondSelectedDays)
            }
        }

    var min: Date? = null
        set(date) {
            field = date
            notifyDataSetChanged()
        }

    var max: Date? = null
        set (date) {
            field = date
            notifyDataSetChanged()
        }

    var firstSelectedDays: Date? = null
    var secondSelectedDays: Date? = null

    private val viewList = ArrayList<MonthView>()

    override fun onBindViewHolder(holder: GenericViewHolder?, position: Int) {
        holder?.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenericViewHolder? {
        val view = MonthView(parent?.context)
        view.selectedColor = selectedColor
        view.disabledColor = disabledColor

        viewList.add(view)
        return CalendarViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    abstract inner class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun <T> bind(t: T)
    }

    inner class CalendarViewHolder(itemView: View) : GenericViewHolder(itemView) {
        override fun <T> bind(t: T) {
            val month = t as Int
            when (itemView) {
                is MonthView -> {
                    itemView.min = min
                    itemView.max = max
                    itemView.disabledDays = dateList
                    itemView.firstSelectedDays = firstSelectedDays
                    itemView.secondSelectedDays = secondSelectedDays
                    itemView.setDeltaMonth(month)
                    itemView.setOnClickListener { date, day -> listener?.invoke(date, day) }

                }
            }
        }
    }

    fun add(accountAddressModel: Int) {
        items.add(accountAddressModel)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun setSelectedDays(firstDay: Date?, secondDay: Date?) {
        firstSelectedDays = firstDay
        secondSelectedDays = secondDay
        viewList.forEach { it.refresh(firstSelectedDays, secondSelectedDays) }
    }
}

