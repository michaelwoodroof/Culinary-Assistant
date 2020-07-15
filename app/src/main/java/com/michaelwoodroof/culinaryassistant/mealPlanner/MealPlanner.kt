package com.michaelwoodroof.culinaryassistant.mealPlanner

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.michaelwoodroof.culinaryassistant.LocalRecipes
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.FileHandler
import com.michaelwoodroof.culinaryassistant.helper.NotificationHandler
import com.michaelwoodroof.culinaryassistant.helper.ScheduleNotification
import com.michaelwoodroof.culinaryassistant.overviews.RecipeDetail
import com.michaelwoodroof.culinaryassistant.structure.MealDocument
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import kotlinx.android.synthetic.main.activity_meal_planner.*
import kotlinx.android.synthetic.main.recipe_layout.view.*
import org.w3c.dom.Text
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class MealPlanner : AppCompatActivity() {

    var mdd = ArrayList<MealDocument>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_planner)

        val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

        // Get Meal Document from File
        val f = File(this.filesDir, "mealplanner")
        val fh = FileHandler()
        Log.d("testData", f.exists().toString())
        if (f.exists()) {
            mdd = fh.getMealDocument(this)
        } else {
            fh.createBlankMealDocument(this)
            mdd = fh.getMealDocument(this)
        }

        for (i in 0..20) {
            Log.d("testData", i.toString())
            Log.d("testData", mdd[i].uid)
        }

        val bundle : Bundle? = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("md")) {
                val md : MealDocument = intent.getParcelableExtra("md")
                fh.updateMealDocument(md, this)
            }
        }

        val date = Calendar.getInstance()
        val format = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)

        ddDayOfWeek.text = Editable.Factory.getInstance().newEditable(format)

        ddDayOfWeek.setOnItemClickListener { parent, view, position, id ->
            val v = view as TextView

            when (v.text.toString()) {

                "Monday" -> {
                    dayLoad(0)
                }

                "Tuesday" -> {
                    dayLoad(1)
                }

                "Wednesday" -> {
                    dayLoad(2)
                }

                "Thursday" -> {
                    dayLoad(3)
                }

                "Friday" -> {
                    dayLoad(4)
                }

                "Saturday" -> {
                    dayLoad(5)
                }

                "Sunday" -> {
                    dayLoad(6)
                }

            }

        }

        val cAdapter = ArrayAdapter(baseContext,
            R.layout.dropdown_menu_popup_item, days)
        ddDayOfWeek.setAdapter(cAdapter)

        dayLoad(date.get(Calendar.DAY_OF_WEEK) - 2)

        // Set on Clicks
        val ib : CardView = includeBF.findViewById(R.id.cvMain)
        ib.setOnClickListener {
            loadRecipe(includeBF)
        }

        val il : CardView = includeLU.findViewById(R.id.cvMain)
        il.setOnClickListener {
            loadRecipe(includeLU)
        }

        val idd : CardView = includeDI.findViewById(R.id.cvMain)
        idd.setOnClickListener {
            loadRecipe(includeDI)
        }

    }

    fun loadPicker(view : View) {
        // Invoke TimePicker
        val cal = Calendar.getInstance()
        val fh = FileHandler()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            // Ensure that App has Notification Channel
            NotificationHandler.createNotificationChannel(this)



            // Set Time Here
            when (view.id) {

                btnTPBreakfast.id -> {
                    val sn = ScheduleNotification()
                    sn.createAlarm(this, hour, minute, (24 * 60 * 60 * 1000 * 7), "Breakfast Meal")
                    btnTPBreakfast.text = SimpleDateFormat("HH:mm").format(cal.time)
                    fh.updateMealDocument(MealDocument(ddDayOfWeek.text.toString(),
                        "Breakfast", btnTPBreakfast.text.toString(),
                        includeBF.tag.toString(), true),this)
                }

                btnTPLunch.id -> {
                    val sn = ScheduleNotification()
                    sn.createAlarm(this, hour, minute, (24 * 60 * 60 * 1000 * 7), "Lunch Meal")
                    btnTPLunch.text = SimpleDateFormat("HH:mm").format(cal.time)
                    fh.updateMealDocument(MealDocument(ddDayOfWeek.text.toString(),
                        "Lunch", btnTPLunch.text.toString(),
                        includeLU.tag.toString(), true),this)
                }

                btnTPDinner.id -> {
                    val sn = ScheduleNotification()
                    sn.createAlarm(this, hour, minute, (24 * 60 * 60 * 1000 * 7), "Evening Meal")
                    btnTPDinner.text = SimpleDateFormat("HH:mm").format(cal.time)
                    fh.updateMealDocument(MealDocument(ddDayOfWeek.text.toString(),
                        "Dinner", btnTPDinner.text.toString(),
                        includeDI.tag.toString(), true),this)
                }

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

    fun dayLoad(day : Int) {

        Log.d("testData", day.toString())

        val fh = FileHandler()

        // Clear Includes
        var tv: TextView = includeBF.findViewById(R.id.tv_local_title)
        var tvD: TextView = includeBF.findViewById(R.id.tvDescR)

        tv.text = ""
        tvD.text = ""

        tv = includeLU.findViewById(R.id.tv_local_title)
        tvD = includeLU.findViewById(R.id.tvDescR)

        tv.text = ""
        tvD.text = ""

        tv = includeDI.findViewById(R.id.tv_local_title)
        tvD = includeDI.findViewById(R.id.tvDescR)

        tv.text = ""
        tvD.text = ""

        when (day) {

            0 -> {

                val tv: TextView = includeBF.findViewById(R.id.tv_local_title)
                val tvD: TextView = includeBF.findViewById(R.id.tvDescR)

                if (mdd[0].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[0].uid, false)!!
                    tv.text = r.title
                    tvD.text = r.description
                    includeBF.tag = mdd[0].uid
                    btnTPBreakfast.text = mdd[0].time
                }

                val tv2: TextView = includeLU.findViewById(R.id.tv_local_title)
                val tvD2: TextView = includeLU.findViewById(R.id.tvDescR)
                if (mdd[1].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[1].uid, false)!!
                    tv2.text = r.title
                    tvD2.text = r.description
                    includeLU.tag = mdd[1].uid
                    btnTPLunch.text = mdd[1].time
                }

                val tv3: TextView = includeDI.findViewById(R.id.tv_local_title)
                val tvD3: TextView = includeDI.findViewById(R.id.tvDescR)
                if (mdd[2].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[2].uid, false)!!
                    tv3.text = r.title
                    tvD3.text = r.description
                    includeDI.tag = mdd[2].uid
                    btnTPDinner.text = mdd[2].time
                }


            }

            1 -> {

                val tv: TextView = includeBF.findViewById(R.id.tv_local_title)
                val tvD: TextView = includeBF.findViewById(R.id.tvDescR)

                if (mdd[3].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[3].uid, false)!!
                    tv.text = r.title
                    tvD.text = r.description
                    includeBF.tag = mdd[3].uid
                    btnTPBreakfast.text = mdd[3].time
                }

                val tv2: TextView = includeLU.findViewById(R.id.tv_local_title)
                val tvD2: TextView = includeLU.findViewById(R.id.tvDescR)
                if (mdd[4].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[4].uid, false)!!
                    tv2.text = r.title
                    tvD2.text = r.description
                    includeLU.tag = mdd[4].uid
                    btnTPLunch.text = mdd[4].time
                }

                val tv3: TextView = includeDI.findViewById(R.id.tv_local_title)
                val tvD3: TextView = includeDI.findViewById(R.id.tvDescR)
                if (mdd[5].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[5].uid, false)!!
                    tv3.text = r.title
                    tvD3.text = r.description
                    includeDI.tag = mdd[5].uid
                    btnTPDinner.text = mdd[5].time
                }

            }

            2 -> {

                val tv: TextView = includeBF.findViewById(R.id.tv_local_title)
                val tvD: TextView = includeBF.findViewById(R.id.tvDescR)

                if (mdd[6].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[6].uid, false)!!
                    tv.text = r.title
                    tvD.text = r.description
                    includeBF.tag = mdd[6].uid
                    btnTPBreakfast.text = mdd[6].time
                }

                val tv2: TextView = includeLU.findViewById(R.id.tv_local_title)
                val tvD2: TextView = includeLU.findViewById(R.id.tvDescR)
                if (mdd[7].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[7].uid, false)!!
                    tv2.text = r.title
                    tvD2.text = r.description
                    includeLU.tag = mdd[7].uid
                    btnTPLunch.text = mdd[7].time
                }

                val tv3: TextView = includeDI.findViewById(R.id.tv_local_title)
                val tvD3: TextView = includeDI.findViewById(R.id.tvDescR)
                if (mdd[8].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[8].uid, false)!!
                    tv3.text = r.title
                    tvD3.text = r.description
                    includeDI.tag = mdd[8].uid
                    btnTPDinner.text = mdd[8].time
                }

            }

            3 -> {

                val tv: TextView = includeBF.findViewById(R.id.tv_local_title)
                val tvD: TextView = includeBF.findViewById(R.id.tvDescR)

                if (mdd[9].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[9].uid, false)!!
                    tv.text = r.title
                    tvD.text = r.description
                    includeBF.tag = mdd[9].uid
                }

                val tv2: TextView = includeLU.findViewById(R.id.tv_local_title)
                val tvD2: TextView = includeLU.findViewById(R.id.tvDescR)
                if (mdd[10].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[10].uid, false)!!
                    tv2.text = r.title
                    tvD2.text = r.description
                    includeLU.tag = mdd[10].uid
                }

                val tv3: TextView = includeDI.findViewById(R.id.tv_local_title)
                val tvD3: TextView = includeDI.findViewById(R.id.tvDescR)
                if (mdd[11].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[11].uid, false)!!
                    tv3.text = r.title
                    tvD3.text = r.description
                    includeDI.tag = mdd[11].uid
                }

            }

            4 -> {

                val tv: TextView = includeBF.findViewById(R.id.tv_local_title)
                val tvD: TextView = includeBF.findViewById(R.id.tvDescR)

                if (mdd[12].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[12].uid, false)!!
                    tv.text = r.title
                    tvD.text = r.description
                    includeBF.tag = mdd[12].uid
                }

                val tv2: TextView = includeLU.findViewById(R.id.tv_local_title)
                val tvD2: TextView = includeLU.findViewById(R.id.tvDescR)
                if (mdd[13].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[13].uid, false)!!
                    tv2.text = r.title
                    tvD2.text = r.description
                    includeLU.tag = mdd[13].uid
                }

                val tv3: TextView = includeDI.findViewById(R.id.tv_local_title)
                val tvD3: TextView = includeDI.findViewById(R.id.tvDescR)
                if (mdd[14].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[14].uid, false)!!
                    tv3.text = r.title
                    tvD3.text = r.description
                    includeDI.tag = mdd[14].uid
                }

            }

            5 -> {

                val tv: TextView = includeBF.findViewById(R.id.tv_local_title)
                val tvD: TextView = includeBF.findViewById(R.id.tvDescR)

                if (mdd[15].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[15].uid, false)!!
                    tv.text = r.title
                    tvD.text = r.description
                    includeBF.tag = mdd[15].uid
                }

                val tv2: TextView = includeLU.findViewById(R.id.tv_local_title)
                val tvD2: TextView = includeLU.findViewById(R.id.tvDescR)
                if (mdd[16].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[16].uid, false)!!
                    tv2.text = r.title
                    tvD2.text = r.description
                    includeLU.tag = mdd[16].uid
                }

                val tv3: TextView = includeDI.findViewById(R.id.tv_local_title)
                val tvD3: TextView = includeDI.findViewById(R.id.tvDescR)
                if (mdd[17].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[17].uid, false)!!
                    tv3.text = r.title
                    tvD3.text = r.description
                    includeDI.tag = mdd[17].uid
                }

            }

            6 -> {

                val tv: TextView = includeBF.findViewById(R.id.tv_local_title)
                val tvD: TextView = includeBF.findViewById(R.id.tvDescR)

                if (mdd[18].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[18].uid, false)!!
                    tv.text = r.title
                    tvD.text = r.description
                    includeBF.tag = mdd[18].uid
                }

                val tv2: TextView = includeLU.findViewById(R.id.tv_local_title)
                val tvD2: TextView = includeLU.findViewById(R.id.tvDescR)
                if (mdd[19].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[19].uid, false)!!
                    tv2.text = r.title
                    tvD2.text = r.description
                    includeLU.tag = mdd[19].uid
                }

                val tv3: TextView = includeDI.findViewById(R.id.tv_local_title)
                val tvD3: TextView = includeDI.findViewById(R.id.tvDescR)
                if (mdd[20].uid != "-1") {
                    val r: Recipe = fh.getRecipe(this, mdd[20].uid, false)!!
                    tv3.text = r.title
                    tvD3.text = r.description
                    includeDI.tag = mdd[20].uid
                }

            }
        }

    }

    fun loadLocalPicker(view : View) {

        val day = ddDayOfWeek.text.toString()
        var mealType = ""
        when (view.id) {

            btnEditBreak.id -> {
                mealType = "Breakfast"
            }

            btnEditLunch.id -> {
                mealType = "Lunch"
            }

            btnEditDinner.id -> {
                mealType = "Dinner"
            }

        }

        val time = "09:00"
        val uid = ""
        val isNoti = true


        val intent = Intent(this, LocalRecipes::class.java)
        intent.putExtra("mpMode", MealDocument(day, mealType, time, uid, isNoti) as Parcelable)
        startActivity(intent)

    }

    fun loadRecipe(view : View) {
        val fh = FileHandler()
        if (view.tag != "-1") {
            val intent = Intent(this, RecipeDetail::class.java)
            intent.putExtra("uid", view.tag.toString())
            intent.putExtra("r", fh.getRecipe(this,
                view.tag.toString(), false) as Parcelable)
            intent.putExtra("isOnline", "No")
            startActivity(intent)
        }
    }

    fun goBack(view : View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
