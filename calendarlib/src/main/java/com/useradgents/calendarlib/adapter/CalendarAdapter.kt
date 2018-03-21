package com.useradgents.calendarlib.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.useradgents.calendarlib.view.MonthView
import java.util.*

class CalendarAdapter(val listener: ((Date, View) -> Unit)? = null) : RecyclerView.Adapter<CalendarAdapter.GenericViewHolder>() {
    var items = mutableListOf<Int>()
    var dateList: List<Date>?= null
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    var selectedDays: List<Date>?= null
        set(list) {
            field = list
            notifyDataSetChanged()
        }


    override fun onBindViewHolder(holder: GenericViewHolder?, position: Int) {
        holder?.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenericViewHolder? {
        val view = MonthView(parent?.context)
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
                    itemView.availableDays = dateList
                    itemView.selectedDays = selectedDays
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
}

