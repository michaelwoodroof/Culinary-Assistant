package com.michaelwoodroof.culinaryassistant.createRecipe

// @TODO Update GO FORWARD AND GO BACKWARD TO METHOD

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.content.ContextCompat
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import kotlinx.android.synthetic.main.activity_create_recipe_s2.*

class CreateRecipeS2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe_s2)
        var pr: Recipe? = intent.getParcelableExtra("partialrecipe")

        // Set Attributes
        if (pr?.prepTime != null && pr?.prepTime != "" && pr?.prepTime.contains("-")) {
            val parts = pr?.prepTime?.split("-")
            txtvPrep.text = Editable.Factory.getInstance().newEditable(parts!![0])
            txtPrepMax.text = Editable.Factory.getInstance().newEditable(parts!![1])
        }
        if (pr?.cookTime != null && pr?.cookTime != "" && pr?.prepTime.contains("-")) {
            val parts = pr?.cookTime?.split("-")
            txtvCook.text = Editable.Factory.getInstance().newEditable(parts!![0])
            txtCookMax.text = Editable.Factory.getInstance().newEditable(parts!![1])
        }
        if (pr?.cuisine != null && pr?.cuisine != "") ddCuisine.text = Editable.Factory.getInstance().newEditable(pr?.cuisine)
        if (pr?.courseType != null && pr?.courseType != "") ddMeal.text = Editable.Factory.getInstance().newEditable(pr?.courseType)

        // Set Spice
        // Call Other Spice Levels
        if (pr?.spice != 0) {
            when (pr?.spice) {
                5 -> {
                    btnSpice1.tag = "full"
                    btnSpice2.tag = "full"
                    btnSpice3.tag = "full"
                    btnSpice4.tag = "full"
                    btnSpice5.tag = "full"
                } 4 -> {
                    btnSpice1.tag = "full"
                    btnSpice2.tag = "full"
                    btnSpice3.tag = "full"
                    btnSpice4.tag = "full"
                    btnSpice5.tag = "empty"
                } 3 -> {
                    btnSpice1.tag = "full"
                    btnSpice2.tag = "full"
                    btnSpice3.tag = "full"
                    btnSpice4.tag = "empty"
                    btnSpice5.tag = "empty"
                } 2 -> {
                    btnSpice1.tag = "full"
                    btnSpice2.tag = "full"
                    btnSpice3.tag = "empty"
                    btnSpice4.tag = "empty"
                    btnSpice5.tag = "empty"
                } 1 -> {
                    btnSpice1.tag = "full"
                    btnSpice2.tag = "empty"
                    btnSpice3.tag = "empty"
                    btnSpice4.tag = "empty"
                    btnSpice5.tag = "empty"
                }
            }
            applySpice()
        }

        // Set Drop-Down Content
        val cuisineTypes = arrayOf("American", "British", "Caribbean", "Chinese", "French", "Greek", "Indian", "Italian", "Japanese", "Mediterranean", "Mexican", "Moroccan", "Spanish"
        , "Thai", "Turkish", "Vietnamese", "Other")

        val mealTypes = arrayOf("Breakfast", "Brunch", "Main Meal", "Other")

        val cAdapter = ArrayAdapter(baseContext, R.layout.dropdown_menu_popup_item, cuisineTypes)
        ddCuisine.setAdapter(cAdapter)

        val mAdapter = ArrayAdapter(baseContext, R.layout.dropdown_menu_popup_item, mealTypes)
        ddMeal.setAdapter(mAdapter)

    }

    override fun onBackPressed() {
        // Ensures that Data is Kept Between Screens
        val b : Button = Button(this)
        goBack(b)
    }

    fun goBack(view: View) {
        var pr: Recipe? = intent.getParcelableExtra("partialrecipe")
        pr?.prepTime = txtvPrep.text.toString() + "-" + txtPrepMax.text.toString()
        pr?.cookTime = txtvCook.text.toString() + "-" + txtCookMax.text.toString()
        pr?.cuisine = ddCuisine.text.toString()
        pr?.courseType = ddMeal.text.toString()
        // Get Spice
        if (btnSpice5.tag == "full") {
            pr?.spice = 5
        } else if (btnSpice4.tag == "full") {
            pr?.spice = 4
        } else if (btnSpice3.tag == "full") {
            pr?.spice = 3
        } else if (btnSpice2.tag == "full") {
            pr?.spice = 2
        } else if (btnSpice1.tag == "full") {
            pr?.spice = 1
        } else {
            pr?.spice = 0
        }
        val intent = Intent(this, CreateRecipeS1::class.java)
        intent.putExtra("partialrecipe", pr)
        startActivity(intent)
    }

    fun goForward(view: View) {
        var boolList  = ArrayList<Boolean>()
        boolList.add(checkField(0))
        boolList.add(checkField(1))
        boolList.add(checkField(2))
        boolList.add(checkField(3))
        if (boolList[0] && boolList[1] && boolList[2] && boolList[3]) {
            var pr: Recipe? = intent.getParcelableExtra("partialrecipe")
            pr?.prepTime = txtvPrep.text.toString() + "-" + txtPrepMax.text.toString()
            pr?.cookTime = txtvCook.text.toString() + "-" + txtCookMax.text.toString()
            pr?.cuisine = ddCuisine.text.toString()
            pr?.courseType = ddMeal.text.toString()
            // Get Spice
            if (btnSpice5.tag == "full") {
                pr?.spice = 5
            } else if (btnSpice4.tag == "full") {
                pr?.spice = 4
            } else if (btnSpice3.tag == "full") {
                pr?.spice = 3
            } else if (btnSpice2.tag == "full") {
                pr?.spice = 2
            } else if (btnSpice1.tag == "full") {
                pr?.spice = 1
            } else {
                pr?.spice = 0
            }
            val intent = Intent(this, CreateRecipeS3::class.java)
            intent.putExtra("partialrecipe", pr)
            startActivity(intent)
        }
    }

    fun spiceChange(view: View) {

        // Get which Chili was clicked
        when (view.id) {
            btnSpice1.id -> {
                btnSpice1.tag = "full"
                btnSpice2.tag = "empty"
                btnSpice3.tag = "empty"
                btnSpice4.tag = "empty"
                btnSpice5.tag = "empty"
            } btnSpice2.id -> {
                btnSpice1.tag = "full"
                btnSpice2.tag = "full"
                btnSpice3.tag = "empty"
                btnSpice4.tag = "empty"
                btnSpice5.tag = "empty"
            } btnSpice3.id -> {
                btnSpice1.tag = "full"
                btnSpice2.tag = "full"
                btnSpice3.tag = "full"
                btnSpice4.tag = "empty"
                btnSpice5.tag = "empty"
            } btnSpice4.id -> {
                btnSpice1.tag = "full"
                btnSpice2.tag = "full"
                btnSpice3.tag = "full"
                btnSpice4.tag = "full"
                btnSpice5.tag = "empty"
            } btnSpice5.id -> {
                btnSpice1.tag = "full"
                btnSpice2.tag = "full"
                btnSpice3.tag = "full"
                btnSpice4.tag = "full"
                btnSpice5.tag = "full"
            }
        }
        applySpice()
    }

    fun applySpice() {
        // Apply Style
        if (btnSpice5.tag == "full") {
            // Set Style of All Chili
            btnSpice1.setImageResource(R.drawable.ic_spicyon)
            btnSpice2.setImageResource(R.drawable.ic_spicyon)
            btnSpice3.setImageResource(R.drawable.ic_spicyon)
            btnSpice4.setImageResource(R.drawable.ic_spicyon)
            btnSpice5.setImageResource(R.drawable.ic_spicyon)
            btnSpice1.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice2.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice3.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice4.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice5.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            txtvSpicyRating.text = "Fiery Hot"
            return
        } else if (btnSpice4.tag == "full") {
            btnSpice1.setImageResource(R.drawable.ic_spicyon)
            btnSpice2.setImageResource(R.drawable.ic_spicyon)
            btnSpice3.setImageResource(R.drawable.ic_spicyon)
            btnSpice4.setImageResource(R.drawable.ic_spicyon)
            btnSpice5.setImageResource(R.drawable.ic_spicyoff)
            btnSpice1.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice2.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice3.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice4.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice5.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
            txtvSpicyRating.text = "Very Hot"
            return
        } else if (btnSpice3.tag == "full") {
            btnSpice1.setImageResource(R.drawable.ic_spicyon)
            btnSpice2.setImageResource(R.drawable.ic_spicyon)
            btnSpice3.setImageResource(R.drawable.ic_spicyon)
            btnSpice4.setImageResource(R.drawable.ic_spicyoff)
            btnSpice5.setImageResource(R.drawable.ic_spicyoff)
            btnSpice1.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice2.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice3.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice4.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
            btnSpice5.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
            txtvSpicyRating.text = "Hot"
            return
        } else if (btnSpice2.tag == "full") {
            btnSpice1.setImageResource(R.drawable.ic_spicyon)
            btnSpice2.setImageResource(R.drawable.ic_spicyon)
            btnSpice3.setImageResource(R.drawable.ic_spicyoff)
            btnSpice4.setImageResource(R.drawable.ic_spicyoff)
            btnSpice5.setImageResource(R.drawable.ic_spicyoff)
            btnSpice1.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice2.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice3.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
            btnSpice4.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
            btnSpice5.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
            txtvSpicyRating.text = "Medium"
            return
        } else if (btnSpice1.tag == "full") {
            btnSpice1.setImageResource(R.drawable.ic_spicyon)
            btnSpice2.setImageResource(R.drawable.ic_spicyoff)
            btnSpice3.setImageResource(R.drawable.ic_spicyoff)
            btnSpice4.setImageResource(R.drawable.ic_spicyoff)
            btnSpice5.setImageResource(R.drawable.ic_spicyoff)
            btnSpice1.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchili))
            btnSpice2.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
            btnSpice3.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
            btnSpice4.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
            btnSpice5.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
            txtvSpicyRating.text = "Mild"
            return
        }
    }

    fun clearSpice(view: View) {
        btnSpice1.setImageResource(R.drawable.ic_spicyoff)
        btnSpice2.setImageResource(R.drawable.ic_spicyoff)
        btnSpice3.setImageResource(R.drawable.ic_spicyoff)
        btnSpice4.setImageResource(R.drawable.ic_spicyoff)
        btnSpice5.setImageResource(R.drawable.ic_spicyoff)
        btnSpice1.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
        btnSpice2.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
        btnSpice3.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
        btnSpice4.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
        btnSpice5.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.cslchiliem))
        txtvSpicyRating.text = "No Heat"
    }

    fun checkField(field : Int) : Boolean {

        // @TODO SHOW ERRORS IN MEANINGFUL WAY

        when (field) {

            // PREP FIELDS
            0 -> {
                // @TODO Check if Empty First then Try to Convert to Int
                if (txtvPrep.text.toString().toInt() <= txtPrepMax.text.toString().toInt() &&
                        txtvPrep.text.toString() != "" && txtPrepMax.text.toString() != ""
                ) {
                    txtiPrep.setError("")
                    return true
                } else {
                    txtiPrep.setError("EEE")
                    return false
                }
            }

            // COOK FIELDS
            1 -> {
                // @TODO Check if Empty First then Try to Convert to Int
                if (txtvCook.text.toString().toInt() <= txtCookMax.text.toString().toInt() &&
                    txtvCook.text.toString() != "" && txtCookMax.text.toString() != ""
                ) {
                    txtiCook.setError("")
                    return true
                } else {
                    txtiCook.setError("EEE")
                    return false
                }
            }

            // CUISINE @TODO ADD WARNING TEXTFIELDS
            2 -> {
                if (ddCuisine.text.toString() != "") {
                    return true
                } else {
                    return false
                }
            }

            // MEAL TYPE @TODO ADD WARNING TEXTFIELDS
            3 -> {
                if (ddMeal.text.toString() != "") {
                    return true
                } else {
                    return false
                }
            }

        }

        return false

    }


}
