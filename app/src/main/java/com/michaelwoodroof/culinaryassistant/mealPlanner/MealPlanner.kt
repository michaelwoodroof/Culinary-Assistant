package com.michaelwoodroof.culinaryassistant.mealPlanner

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.michaelwoodroof.culinaryassistant.LocalRecipes
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.FileHandler
import com.michaelwoodroof.culinaryassistant.structure.MealDocument
import kotlinx.android.synthetic.main.activity_meal_planner.*
import kotlinx.android.synthetic.main.recipe_layout.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MealPlanner : AppCompatActivity() {

    var mdd = ArrayList<MealDocument>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_planner)

        val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

        // Get Meal Document from File
        val f = File("mealplanner.txt")
        val fh = FileHandler()
        if (f.exists()) {
            // Load Monday Data
            Log.d("mdTest", "FILE")
            mdd = fh.getMealDocument(this)
            dayLoad(0)
        } else {
            Log.d("mdTest", "no FILE")
            fh.createBlankMealDocument(this)
            mdd = fh.getMealDocument(this)
            dayLoad(0)
        }

        val bundle : Bundle? = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("md")) {
                val md : MealDocument = intent.getParcelableExtra("md")
                Log.d("mdTest", md.uid)
                fh.updateMealDocument(md, this)
            }
        }


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

    fun updateMealPlannerFile(md : MealDocument) {


    }

    fun dayLoad(day : Int) {

        when (day) {

            0 -> {

            }

            1 -> {

            }

            2 -> {

            }

            3 -> {

            }

            4 -> {

            }

            5 -> {

            }

            6 -> {

            }

            7 -> {

            }

        }

    }

    fun loadLocalPicker(view : View) {

        val day = "Monday"
        val mealType = "Breakfast"
        val time = "09:00"
        val uid = ""
        val isNoti = true

//        when (view.id) {
//
//
//
//        }

        val intent = Intent(this, LocalRecipes::class.java)
        intent.putExtra("mpMode", MealDocument(day, mealType, time, uid, isNoti) as Parcelable)
        startActivity(intent)

    }

    fun goBack(view : View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
