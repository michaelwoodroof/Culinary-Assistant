package com.michaelwoodroof.culinaryassistant.mealPlanner

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import kotlinx.android.synthetic.main.activity_meal_planner.*
import java.text.SimpleDateFormat
import java.util.*


class MealPlanner : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_planner)

        val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

        val cAdapter = ArrayAdapter(baseContext,
            R.layout.dropdown_menu_popup_item, days)
        ddDayOfWeek.setAdapter(cAdapter)

    }

    fun loadPicker(view : View) {
        // Invoke TimePicker
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            // Set Time Here
            when (view.id) {

                btnTPBreakfast.id -> btnTPBreakfast.text = SimpleDateFormat("HH:mm").format(cal.time)

                btnTPLunch.id -> btnTPLunch.text = SimpleDateFormat("HH:mm").format(cal.time)

                btnTPDinner.id -> btnTPDinner.text = SimpleDateFormat("HH:mm").format(cal.time)

            }
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        updateMealPlannerFile()
    }

    fun toggleNotification(view : View) {

        view as ImageView

        if (view.tag == "tog") {
            view.tag = "nt"
            view.setImageResource(R.drawable.ic_notifications_off_black_24dp)
        } else {
            view.tag = "tog"
            view.setImageResource(R.drawable.ic_notifications_active_black_24dp)
        }

    }

    fun updateMealPlannerFile() {
        // Get Day

    }

    fun goBack(view : View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
