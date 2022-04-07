package com.useradgents.calendarmod

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.useradgents.calendarlib.view.CalendarView
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        val calendar  = findViewById<CalendarView>(R.id.calendar)
        calendar.setOnRangeSelectedListener { first, second ->
            Timber.e("on range selected: $first, $second")
            Toast.makeText(this, "ewi $first, enon: $second", Toast.LENGTH_LONG).show()
        }
        calendar.setOnUnavailableDate {
            Timber.e("on setOnUnavailableDate")
            Toast.makeText(this, "Choisissez un intervalle sans jour indisponilbe, n00b", Toast.LENGTH_LONG).show()
        }
        val cal = Calendar.getInstance()
        val fromDate = cal.time
        cal.add(Calendar.MONTH, 3)
        val toDate = cal.time
        calendar.selectedDates = fromDate to toDate

          calendar.max = Calendar.getInstance().apply {
              add(Calendar.MONTH, calendar.nbMonthInFuture)
          }.time

    }
}
