package com.useradgents.calendarlib

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
class MonthViewTest {

    private lateinit var context: Context
    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testMonthDraw() {
        val monthView = MonthView(context)
        monthView.setDeltaMonth(0)
        Assert.assertThat(monthView, Matchers.notNullValue())
    }

    @Test
    fun testMonthDraw10Times() {
        (0 until 10).forEach {
            testMonthDraw()
        }
    }
}