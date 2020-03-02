package com.example.projectsetup.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.FragmentManager
import com.example.projectsetup.R
import com.example.projectsetup.util.DateTimeUtils
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.dialog_job_date_time.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoField
import java.util.*

class JobDateTimeDialog(
    context: Context,
    private var zonedDateTime: ZonedDateTime,
    private val fragmentManager: FragmentManager
) : Dialog(context),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    companion object {
        private const val DATE_PICKER_DIALOG = "DATE_PICKER_DIALOG"
        private const val TIME_PICKER_DIALOG = "TIME_PICKER_DIALOG "
    }

    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var datePickerDialog: DatePickerDialog
    private val now = Calendar.getInstance()
    private lateinit var onSubmitClickListener: OnSubmitClickListener

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    private var hours: Int = 0
    private var minutes: Int = 0
    private var seconds: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_job_date_time)
        window?.let {
            it.setDimAmount(0.55f)
            it.setGravity(Gravity.CENTER)
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setBackgroundDrawableResource(android.R.color.white)
        }
        setCancelable(true)
        setCanceledOnTouchOutside(true)

        setInitialValues()
        initializeLayout()
        setOnClickListener()
    }

    private fun setInitialValues() {
        this.year = zonedDateTime.get(ChronoField.YEAR)
        this.month = zonedDateTime.get(ChronoField.MONTH_OF_YEAR)
        this.day = zonedDateTime.get(ChronoField.DAY_OF_MONTH)

        tvCalendarDate.text =
            "${zonedDateTime.get(ChronoField.DAY_OF_MONTH)}/${zonedDateTime.get(ChronoField.MONTH_OF_YEAR)}/${zonedDateTime.get(
                ChronoField.YEAR
            )}"

        this.hours = zonedDateTime.get(ChronoField.HOUR_OF_DAY)
        this.minutes = zonedDateTime.get(ChronoField.MINUTE_OF_HOUR)
        this.seconds = zonedDateTime.get(ChronoField.SECOND_OF_MINUTE)

        var hour = zonedDateTime.get(ChronoField.HOUR_OF_DAY)
        var timeIndicator = "AM"
        if (hour > 12) {
            hour -= 12
            timeIndicator = "PM"
        }
        val minute = zonedDateTime.get(ChronoField.MINUTE_OF_HOUR)
        var displayMinute = "$minute"
        if (minute < 10)
            displayMinute = "0$minute"

        tvCalendarTime.text =
            "$hour:$displayMinute $timeIndicator"

    }

    private fun initializeLayout() {
        datePickerDialog = DatePickerDialog.newInstance(
            this@JobDateTimeDialog,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.isThemeDark = true
        datePickerDialog.version = DatePickerDialog.Version.VERSION_2
        datePickerDialog.minDate = now

        timePickerDialog = TimePickerDialog.newInstance(
            this@JobDateTimeDialog,
            false
        )

        if (
            zonedDateTime.get(ChronoField.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) &&
            zonedDateTime.get(ChronoField.MONTH_OF_YEAR) == now.get(Calendar.MONTH) &&
            zonedDateTime.get(ChronoField.YEAR) == now.get(Calendar.YEAR)
        ) {
            timePickerDialog.setMinTime(
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND)
            )
        } else {
            timePickerDialog.setMinTime(0, 0, 0)
        }

        timePickerDialog.isThemeDark = true

    }

    private fun setOnClickListener() {
        tvCalendarDate.setOnClickListener {
            if (::datePickerDialog.isInitialized)
                datePickerDialog.show(fragmentManager, DATE_PICKER_DIALOG)
        }

        tvCalendarTime.setOnClickListener {
            if (::timePickerDialog.isInitialized) {
                timePickerDialog.show(fragmentManager, TIME_PICKER_DIALOG)
            }
        }

        ivClose.setOnClickListener {
            dismiss()
        }

        btnSubmit.setOnClickListener {

            if (::onSubmitClickListener.isInitialized)
                onSubmitClickListener.onSubmitClick(year, month, day, hours, minutes)
        }
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        this.hours = hourOfDay
        this.minutes = minute
        this.seconds = second

        val selectedMinutes = if (minute < 10) "0$minute" else "$minute"

        var currentHour = hourOfDay
        var timeIndicicator = "AM"
        if (currentHour > 12) {
            currentHour -= 12
            timeIndicicator = "PM"
        }

        tvCalendarTime.text = "$currentHour:$selectedMinutes $timeIndicicator"
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val monthOfTheYear = monthOfYear + 1 //monthOfYear is starting from 0

        this.year = year
        this.month = monthOfTheYear
        this.day = dayOfMonth

        val selectedMonth = if (monthOfTheYear < 10) "0$monthOfTheYear" else "$monthOfTheYear"

        val selectedDay = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"

        val selectedYear = "$year"

        tvCalendarDate.text = "$selectedDay/$selectedMonth/$selectedYear"

        if (
            dayOfMonth == now.get(Calendar.DAY_OF_MONTH) &&
            monthOfYear == now.get(Calendar.MONTH) &&
            year == now.get(Calendar.YEAR)
        ) {
            timePickerDialog.setMinTime(
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND)
            )
        } else {
            timePickerDialog.setMinTime(0, 0, 0)
        }
    }

    interface OnSubmitClickListener {
        fun onSubmitClick(year:Int, month:Int, day:Int, hours:Int, minutes:Int)
    }

    fun setOnSubmitClickListener(onSubmitClickListener: OnSubmitClickListener) {
        this.onSubmitClickListener = onSubmitClickListener
    }
}