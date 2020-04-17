package com.michaelwoodroof.culinaryassistant.createRecipe

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.core.view.forEach
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.structure.Dietary
import com.michaelwoodroof.culinaryassistant.structure.Ingredient
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import com.mongodb.ConnectionString
import kotlinx.android.synthetic.main.activity_create_recipe_s4.*


// @TODO FIX ALLERGENS + INGREDIENTS

class CreateRecipeS4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe_s4)

        var pr: Recipe? = intent.getParcelableExtra("partialrecipe")
        cvCreate.visibility = View.GONE

        if (pr?.numOfServings != "") {
            txtPortion.text = Editable.Factory.getInstance().newEditable(pr?.numOfServings)
        }

        if (pr?.ingredients?.size!! > 0) {
            for (ingredient in pr.ingredients) {
                addIngredientData(ingredient.name, ingredient.amount, ingredient.unit, ingredient.notes)
            }
        }

        if (pr.dietary.size > 0) {
            for (allergen in pr.dietary) {
                toggleAllergen(allergen.name)
            }
        }

        // Set Up Recipe Drop-Down
        var unitTypes = arrayOf("g","ml","oz","tbsp","tsp","c","pt","qt","gal","lb","#","custom")
        val cAdapter = ArrayAdapter(baseContext, R.layout.dropdown_menu_popup_item, unitTypes)
        ddUnit.setAdapter(cAdapter)

    }

    fun goBack(view: View) {
        val intent = Intent(this, CreateRecipeS3::class.java)
        intent.putExtra("partialrecipe", loadDataIntoIntent() as Parcelable)
        startActivity(intent)
    }

    override fun onBackPressed() {
        // Ensures that Data is Kept Between Screens
        val b : Button = Button(this) // Empty View
        goBack(b)
    }

    fun goForward(view: View) {
        val intent = Intent(this, CreateRecipeS5::class.java)
        intent.putExtra("partialrecipe", loadDataIntoIntent() as Parcelable)
        startActivity(intent)
    }

    private fun loadDataIntoIntent() : Recipe {
        var pr: Recipe = intent.getParcelableExtra("partialrecipe")!!

        // Add Portion Amount
        pr.numOfServings = txtPortion.text.toString()

        // Check Allergens
        pr.dietary = ArrayList()

        for (child in cgDietary.children) {
            val chip : Chip = child as Chip
            if (chip.isChecked) {
                pr.dietary.add(Dietary(chip.tag.toString()))
            }
        }

        pr.ingredients = ArrayList<Ingredient>()
        var counter = 0
        var ingredient = Ingredient("","","","")

        for (child in clIngredients.children) {

            if (child is TextView) {

                val cView : TextView = child

                if (child.id != tvTitle.id) {
                    when (counter) {

                        0 -> ingredient.amount = cView.text.toString()

                        1 -> ingredient.unit = cView.text.toString()

                        2 -> ingredient.name = cView.text.toString()

                        3 -> {
                            // Add Ingredient Data
                            ingredient.notes = cView.tag.toString()
                            pr.ingredients.add(ingredient)
                            // Reset Value
                            ingredient = Ingredient("","","","")
                            counter = -1
                        }

                    }

                    counter ++
                }
            }

        }

        return pr

    }

    fun loadIngredientMenu(view : View) {

        cvCreate.visibility = View.VISIBLE

    }

    fun closeIngredientMenu(view : View) {

        cvCreate.visibility = View.GONE

    }

    fun addIngredient(view : View) {
        clIngredients.visibility = View.VISIBLE
        addIngredientData(txtIngredientName.text.toString(), txtAmount.text.toString(),ddUnit.text.toString(),txtNotes.text.toString())
        cvCreate.visibility = View.GONE
    }

    fun addIngredientData(data : String, amount : String, unit : String, notes : String) : Int {

        val lastRef = getLastRef()

        // Add Views
        val tvIngredientName = TextView(this)
        val tvAmount = TextView(this)
        val tvUnit = TextView(this)
        val tvNotes = TextView(this)

        val btnEdit = Button(this)
        val btnRemove = Button(this)

        tvIngredientName.id = View.generateViewId()
        tvIngredientName.text = data
        tvIngredientName.textSize = 12F

        tvAmount.id = View.generateViewId()
        tvAmount.text = amount
        tvAmount.textSize = 12F

        tvUnit.id = View.generateViewId()
        tvUnit.text = unit
        tvUnit.textSize = 12F

        tvNotes.id = View.generateViewId()
        if (notes.trim() == "") {
            tvNotes.text = ""
            tvNotes.tag = notes.trim()
        } else {
            tvNotes.text = getString(R.string.notesFormat, "(", notes.trim(), ")")
            tvNotes.tag = notes.trim()
        }

        tvNotes.textSize = 10F

        btnEdit.id = View.generateViewId()

        btnRemove.id = View.generateViewId()
        btnRemove.text = "Remove"
        btnRemove.setOnClickListener {
            clIngredients.removeView(tvIngredientName)
            clIngredients.removeView(tvAmount)
            clIngredients.removeView(tvUnit)
            clIngredients.removeView(tvNotes)
            clIngredients.removeView(btnRemove)
            clIngredients.removeView(btnEdit)
        }

        // Add to Constraint Layout
        clIngredients.addView(tvAmount)
        clIngredients.addView(tvUnit)
        clIngredients.addView(tvIngredientName)
        clIngredients.addView(tvNotes)
//        clIngredients.addView(btnEdit)
//        clIngredients.addView(btnRemove)

        // Apply Constraints
        val setAmount = ConstraintSet()
        setAmount.clone(clIngredients)
        setAmount.connect(
            tvAmount.id,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT
        )
        // Statement to allow for better spacing

        if (lastRef == tvTitle.id) {
            setAmount.connect(
                tvAmount.id,
                ConstraintSet.TOP,
                lastRef,
                ConstraintSet.BOTTOM,
                24
            )
        } else {
            setAmount.connect(
                tvAmount.id,
                ConstraintSet.TOP,
                lastRef,
                ConstraintSet.BOTTOM,
                18
            )
        }
        setAmount.applyTo(clIngredients)

        val setUnit = ConstraintSet()
        setUnit.clone(clIngredients)
        setUnit.connect(
            tvUnit.id,
            ConstraintSet.LEFT,
            glUnit.id,
            ConstraintSet.RIGHT
        )
        setUnit.connect(
            tvUnit.id,
            ConstraintSet.TOP,
            tvAmount.id,
            ConstraintSet.TOP
        )
        setUnit.applyTo(clIngredients)

        val setIngredient = ConstraintSet()
        setIngredient.clone(clIngredients)
        setIngredient.connect(
            tvIngredientName.id,
            ConstraintSet.LEFT,
            glIngredient.id,
            ConstraintSet.RIGHT
        )
        setIngredient.connect(
            tvIngredientName.id,
            ConstraintSet.TOP,
            tvAmount.id,
            ConstraintSet.TOP
        )
        setIngredient.applyTo(clIngredients)

        val setNotes = ConstraintSet()
        setNotes.clone(clIngredients)
        setNotes.connect(
            tvNotes.id,
            ConstraintSet.LEFT,
            glNotes.id,
            ConstraintSet.RIGHT
        )
        setNotes.connect(
            tvNotes.id,
            ConstraintSet.TOP,
            tvAmount.id,
            ConstraintSet.TOP
        )
        setNotes.connect(
            tvNotes.id,
            ConstraintSet.BOTTOM,
            tvAmount.id,
            ConstraintSet.BOTTOM
        )
        setNotes.applyTo(clIngredients)

        return tvAmount.id

    }

    fun changeChips(view : View) {
        if (swShowNames.isChecked == true) {

            chpCelery.text = Editable.Factory.getInstance().newEditable("Celery")
            chpCelery.textStartPadding = 6F
            chpCelery.textEndPadding = 6F

            chpGluten.text = Editable.Factory.getInstance().newEditable("Gluten")
            chpGluten.textStartPadding = 6F
            chpGluten.textEndPadding = 6F

            chpEggs.text = Editable.Factory.getInstance().newEditable("Eggs")
            chpEggs.textStartPadding = 6F
            chpEggs.textEndPadding = 6F

            chpSeafood.text = Editable.Factory.getInstance().newEditable("Fish")
            chpSeafood.textStartPadding = 6F
            chpSeafood.textEndPadding = 6F

            chpLupin.text = Editable.Factory.getInstance().newEditable("Lupin")
            chpLupin.textStartPadding = 6F
            chpLupin.textEndPadding = 6F

            chpMilk.text = Editable.Factory.getInstance().newEditable("Milk")
            chpMilk.textStartPadding = 6F
            chpMilk.textEndPadding = 6F

            chpMustard.text = Editable.Factory.getInstance().newEditable("Mustard")
            chpMustard.textStartPadding = 6F
            chpMustard.textEndPadding = 6F

            chpNuts.text = Editable.Factory.getInstance().newEditable("Nuts")
            chpNuts.textStartPadding = 6F
            chpNuts.textEndPadding = 6F

            chpPeanuts.text = Editable.Factory.getInstance().newEditable("Peanuts")
            chpPeanuts.textStartPadding = 6F
            chpPeanuts.textEndPadding = 6F

            chpSesame.text = Editable.Factory.getInstance().newEditable("Sesame")
            chpSesame.textStartPadding = 6F
            chpSesame.textEndPadding = 6F

            chpSoya.text = Editable.Factory.getInstance().newEditable("Soya")
            chpSoya.textStartPadding = 6F
            chpSoya.textEndPadding = 6F

            chpSulphites.text = Editable.Factory.getInstance().newEditable("Sulphites")
            chpSulphites.textStartPadding = 6F
            chpSulphites.textEndPadding = 6F

        } else {
            chpCelery.text = Editable.Factory.getInstance().newEditable("")
            chpCelery.textStartPadding = 0F
            chpCelery.textEndPadding = 0F

            chpGluten.text = Editable.Factory.getInstance().newEditable("")
            chpGluten.textStartPadding = 0F
            chpGluten.textEndPadding = 0F

            chpEggs.text = Editable.Factory.getInstance().newEditable("")
            chpEggs.textStartPadding = 0F
            chpEggs.textEndPadding = 0F

            chpSeafood.text = Editable.Factory.getInstance().newEditable("")
            chpSeafood.textStartPadding = 0F
            chpSeafood.textEndPadding = 0F

            chpLupin.text = Editable.Factory.getInstance().newEditable("")
            chpLupin.textStartPadding = 0F
            chpLupin.textEndPadding = 0F

            chpMilk.text = Editable.Factory.getInstance().newEditable("")
            chpMilk.textStartPadding = 0F
            chpMilk.textEndPadding = 0F

            chpMustard.text = Editable.Factory.getInstance().newEditable("")
            chpMustard.textStartPadding = 0F
            chpMustard.textEndPadding = 0F

            chpNuts.text = Editable.Factory.getInstance().newEditable("")
            chpNuts.textStartPadding = 0F
            chpNuts.textEndPadding = 0F

            chpPeanuts.text = Editable.Factory.getInstance().newEditable("")
            chpPeanuts.textStartPadding = 0F
            chpPeanuts.textEndPadding = 0F

            chpSesame.text = Editable.Factory.getInstance().newEditable("")
            chpSesame.textStartPadding = 0F
            chpSesame.textEndPadding = 0F

            chpSoya.text = Editable.Factory.getInstance().newEditable("")
            chpSoya.textStartPadding = 0F
            chpSoya.textEndPadding = 0F

            chpSulphites.text = Editable.Factory.getInstance().newEditable("")
            chpSulphites.textStartPadding = 0F
            chpSulphites.textEndPadding = 0F

        }
    }

    fun checkedChips(view : View) {

        val chip = view as Chip

        if (chip.isChecked) {
            chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#838383"))
            chip.chipIconTint = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            chip.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#E1E1E1"))
            chip.chipIconTint = ColorStateList.valueOf(Color.parseColor("#838383"))
            chip.setTextColor(Color.parseColor("#424242"))
        }

    }

    fun toggleAllergen(allergen : String) {

        when(allergen) {

            "Celery" -> {
                chpCelery.isChecked = true
                checkedChips(chpCelery)
            }

            "Gluten" -> {
                chpGluten.isChecked = true
                checkedChips(chpGluten)
            }

            "Eggs" -> {
                chpEggs.isChecked = true
                checkedChips(chpEggs)
            }

            "Seafood" -> {
                chpSeafood.isChecked = true
                checkedChips(chpSeafood)
            }

            "Lupin" -> {
                chpLupin.isChecked = true
                checkedChips(chpLupin)
            }

            "Milk" -> {
                chpMilk.isChecked = true
                checkedChips(chpMilk)
            }

            "Mustard" -> {
                chpMustard.isChecked = true
                checkedChips(chpMustard)
            }

            "Nuts" -> {
                chpNuts.isChecked = true
                checkedChips(chpNuts)
            }

            "Peanuts" -> {
                chpPeanuts.isChecked = true
                checkedChips(chpPeanuts)
            }

            "Sesame" -> {
                chpSesame.isChecked = true
                checkedChips(chpSesame)
            }

            "Soya" -> {
                chpSoya.isChecked = true
                checkedChips(chpSoya)
            }

            "Sulphites" -> {
                chpSulphites.isChecked = true
                checkedChips(chpSulphites)
            }

        }

    }

    private fun getLastRef() : Int { // @TODO FIX

        var foundRef = 0

        for (child in clIngredients.children) {

            if (child is TextView) foundRef = child.id

        }

        Log.d("testData", foundRef.toString())
        return foundRef

    }

}
