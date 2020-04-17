package com.michaelwoodroof.culinaryassistant.createRecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.view.forEach
import com.google.android.material.chip.Chip
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import kotlinx.android.synthetic.main.activity_create_recipe_s2.*
import kotlinx.android.synthetic.main.activity_create_recipe_s3.*

// @TODO Update GO FORWARD AND GO BACKWARD TO METHOD
// @TODO UPDATE KEEP TIME SO THAT SWITCH ISN'T ON WHEN FIELD IS EMPTY

class CreateRecipeS3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe_s3)

        val pr: Recipe? = intent.getParcelableExtra("partialrecipe")
        var keepTypes = arrayOf("3 Days", "5 Days", "1 Week", "2 Weeks", "3 Weeks", "1 Month")

        if (pr?.isFreezable != "NO" && pr?.isFreezable != null) {
            swFreezeable.isChecked = true
            txtvHowLong.visibility = View.VISIBLE
            txtiKeep.visibility = View.VISIBLE
            ddKeep.visibility = View.VISIBLE
            ddKeep.text = Editable.Factory.getInstance().newEditable(pr?.isFreezable)
        }

        // Set Drop-Down content

        val cAdapter = ArrayAdapter(baseContext, R.layout.dropdown_menu_popup_item, keepTypes)
        ddKeep.setAdapter(cAdapter)

        // Set Chips
        if (pr?.keywords?.size != 0 && pr?.keywords != null) {
            for (keyword in pr.keywords) {
                addChipData(keyword)
            }
            btnAdd.visibility = View.VISIBLE
            btnAddChar.visibility = View.GONE
            txtiChara.visibility = View.VISIBLE
        }

    }

    fun showKeepMenu(view: View) {
        if (swFreezeable.isChecked) {
            txtvHowLong.visibility = View.VISIBLE
            txtiKeep.visibility = View.VISIBLE
            ddKeep.visibility = View.VISIBLE
        } else {
            txtvHowLong.visibility = View.GONE
            txtiKeep.visibility = View.GONE
            ddKeep.visibility = View.GONE
        }
    }

    fun goBack(view: View) {
        var pr: Recipe? = intent.getParcelableExtra("partialrecipe")
        // Update S3 Properties
        if (ddKeep.visibility == View.VISIBLE) {
            pr?.isFreezable = ddKeep.text.toString()
        } else {
            pr?.isFreezable = "NO"
        }
        // Update Keywords
        val keywords = ArrayList<String>()
        cgKeywords.forEach {
            val data : Chip = it as Chip
            keywords.add(data.text as String)
        }
        pr?.keywords = keywords
        val intent = Intent(this, CreateRecipeS2::class.java)
        intent.putExtra("partialrecipe", pr as Parcelable)
        startActivity(intent)
    }

    override fun onBackPressed() {
        // Ensures that Data is Kept Between Screens
        val b : Button = Button(this) // Empty View
        goBack(b)
    }

    fun goForward(view: View) {
        var pr: Recipe? = intent.getParcelableExtra("partialrecipe")
        // Update S3 Properties
        if (ddKeep.visibility == View.VISIBLE) {
            pr?.isFreezable = ddKeep.text.toString()
        } else {
            pr?.isFreezable = "NO"
        }
        // Update Keywords
        val keywords = ArrayList<String>()
        cgKeywords.forEach {
            val data : Chip = it as Chip
            keywords.add(data.text as String)
        }
        pr?.keywords = keywords
        val intent = Intent(this, CreateRecipeS4::class.java)
        intent.putExtra("partialrecipe", pr as Parcelable)
        startActivity(intent)
    }

    fun openChipMenu(view : View) {
        btnAdd.visibility = View.VISIBLE
        btnAddChar.visibility = View.GONE
        txtiChara.visibility = View.VISIBLE
    }

    fun addChip(view : View) {
        addChipData(txtChara.text.toString())
        txtChara.text = Editable.Factory.getInstance().newEditable("") // Remove Text
    }

    private fun addChipData(data : String) {
        val chip = Chip(cgKeywords.context)
        // @TODO ADD PROPERTIES TO CHIP
        // @TODO ADD METHOD TO REMOVE CHIP
        // @TODO ADD CHECK TO ENSURE NOT TOO MANY CHIPS LIMIT AT 5 ?
        // @TODO ENSURE NO DUPLICATE CHIPS
        chip.id = View.generateViewId()
        chip.chipText = data
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener { cgKeywords.removeView(chip) }
        cgKeywords.addView(chip)
    }

    fun checkField(field : Int) : Boolean {
        return false
    }

}
