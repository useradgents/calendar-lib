package com.useradgents.calendarmod

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendar.setOnRangeSelectedListener { first, second -> Toast.makeText (this, "ewi $first, enon: $second", Toast.LENGTH_LONG).show() }
        calendar.setOnUnavailableDate { Toast.makeText (this, "Choisissez un intervalle sans jour indisponilbe, n00b", Toast.LENGTH_LONG).show() }

        val cal = Calendar.getInstance()
        cal.set(2018, 2, 30)
        calendar.notAvailableDays = arrayListOf(cal.time)
    }
}
