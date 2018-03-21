package com.useradgents.calendarlib.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.useradgents.calendarlib.view.MonthView
import java.util.*

class CalendarAdapter(val listener: ((Date, View) -> Unit)? = null) : RecyclerView.Adapter<CalendarAdapter.GenericViewHolder>() {
    var items = mutableListOf<Int>()
    var i = 0
    var dateList: List<Date>?= null
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    var selectedDays: List<Date>?= null
        set(list) {
            field = list
            viewList.forEach { it.refresh(field) }
        }

    private val viewList = ArrayList<MonthView>()

    override fun onBindViewHolder(holder: GenericViewHolder?, position: Int) {
        holder?.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenericViewHolder? {
        i++
        Log.e("ewi", "new view holder $i)")
        val view = MonthView(parent?.context)
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
                    itemView.disabledDays = dateList
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

