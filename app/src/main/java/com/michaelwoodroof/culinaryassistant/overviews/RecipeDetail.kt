package com.michaelwoodroof.culinaryassistant.overviews

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.michaelwoodroof.culinaryassistant.adapters.IngredientAdapter
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.RecipeCallback
import com.michaelwoodroof.culinaryassistant.adapters.StepAdapter
import com.michaelwoodroof.culinaryassistant.structure.IngredientContent
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import com.michaelwoodroof.culinaryassistant.structure.StepContent
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import org.bson.Document
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class RecipeDetail : AppCompatActivity() {

    // Used throughout
    lateinit var rc : Recipe
    lateinit var nd : MutableList<Document>
    var currentStep : Int = 0
    var currentData : String = "YES"
    private lateinit var recyclerView : RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)
        clReview.visibility = View.GONE

        // Get Recipe
        val bundle : Bundle? = intent.extras
        if (bundle != null) {

            if (bundle.containsKey("isOnline")) {
                if (intent.getStringExtra("isOnline") == "Yes") {
                    // Show Reviews
                    clReview.visibility = View.VISIBLE
                    clReview.visibility = View.GONE
                    val uid = intent.getStringExtra("uid")
                    //loadReviews(uid)
                } else {
                    // Remove Review as Offline Recipe
                    clReview.visibility = View.GONE
                }
            }

            if (bundle.containsKey("r")) {
                rc = intent.getParcelableExtra("r")!!

                // Fill in Content as Needed
//        val bm : Bitmap = ImageConversions.stringToBitMap(imgPath)
//        imgRecipePhoto.setImageBitmap(bm)

                val fh = com.michaelwoodroof.culinaryassistant.helper.FileHandler()

                if (fh.getRecipe(this, rc.id, false) == null) {
                    // Set ImgButton Tint
                    btnSave.setColorFilter(Color.BLACK)
                }

                imgRecipePhoto.setImageResource(R.drawable.rplaceholder)

                txtvTitle.text = rc.title

                when (rc.difficulty) {

                    0 -> {
                        txtvDifficulty.text = "Novice"
                    }

                    1 -> {
                        txtvDifficulty.text = "Intermediate"
                    }

                    2 -> {
                        txtvDifficulty.text = "Expert"
                    }

                }

                // Configure Spice
                val res : Resources = this.resources
                when (rc.spice) {

                    0 -> {
                        imgS1.visibility = View.INVISIBLE
                        imgS2.visibility = View.INVISIBLE
                        imgS3.visibility = View.INVISIBLE
                        imgS4.visibility = View.INVISIBLE
                        imgS5.visibility = View.INVISIBLE
                    }

                    1 -> {
                        imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS1.setColorFilter(Color.RED)
                        imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                        imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                        imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                        imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                    }

                    2 -> {
                        imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS1.setColorFilter(Color.RED)
                        imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS2.setColorFilter(Color.RED)
                        imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                        imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                        imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                    }

                    3 -> {
                        imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS1.setColorFilter(Color.RED)
                        imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS2.setColorFilter(Color.RED)
                        imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS3.setColorFilter(Color.RED)
                        imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                        imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                    }

                    4 -> {
                        imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS1.setColorFilter(Color.RED)
                        imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS2.setColorFilter(Color.RED)
                        imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS3.setColorFilter(Color.RED)
                        imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS4.setColorFilter(Color.RED)
                        imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                    }

                    5 -> {
                        imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS1.setColorFilter(Color.RED)
                        imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS2.setColorFilter(Color.RED)
                        imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS3.setColorFilter(Color.RED)
                        imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS4.setColorFilter(Color.RED)
                        imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                        imgS5.setColorFilter(Color.RED)
                    }

                }

                val temp = rc.temperature.substring(0, rc.temperature.length - 2)
                val suffix = rc.temperature.takeLast(2)
                val celcius = rc.convertToCelsius(suffix, temp)
                txtvTemp.text = celcius

                // Format Prep and Cook Times
                val prepTime = rc.prepTime.split("-")
                var modPrep = rc.prepTime
                if (prepTime[0] == prepTime[1]) {
                    modPrep = prepTime[0]
                }

                val cookTime = rc.cookTime.split("-")
                var modCook = rc.cookTime
                if (cookTime[0] == cookTime[1]) {
                    modCook = cookTime[0]
                }

                txtvPrep.text = getString(R.string.timingFormat, modPrep)
                txtvCook.text = getString(R.string.timingFormat, modCook)

                txtvPortion.text = Editable.Factory.getInstance().newEditable(rc.numOfServings)
                txtvAuthor.text = rc.author

                // Configure Allergens
                if (rc.dietary.size == 0) {
                    txtvAllergens.visibility = View.GONE
                } else {
                    txtvAllergens.visibility = View.VISIBLE
                    for (diet in rc.dietary) {

                        // Add Allergens into Allergen List
                        when (diet.name) {

                            "Celery" -> chpCelery.visibility = View.VISIBLE
                            "Gluten" -> chpGluten.visibility = View.VISIBLE
                            "Eggs" -> chpEggs.visibility = View.VISIBLE
                            "Seafood" -> chpSeafood.visibility = View.VISIBLE
                            "Lupin" -> chpLupin.visibility = View.VISIBLE
                            "Milk" -> chpMilk.visibility = View.VISIBLE
                            "Mustard" -> chpMustard.visibility = View.VISIBLE
                            "Nuts" -> chpNuts.visibility = View.VISIBLE
                            "Peanuts" -> chpPeanuts.visibility = View.VISIBLE
                            "Sesame" -> chpSesame.visibility = View.VISIBLE
                            "Soya" -> chpSoya.visibility = View.VISIBLE
                            "Sulphites" -> chpSulphites.visibility = View.VISIBLE

                        }
                    }
                }

                txtvDescription.text = rc.description


                // Configure Steps / Ingredients Section
                val dataSet = ArrayList<StepContent.StepItem>()
                for (step in rc.steps) {
                    val si = StepContent.StepItem(step.stepNumber.toString(), step.description)
                    dataSet.add(si)
                }

                viewManager = LinearLayoutManager(this)
                viewAdapter =
                    StepAdapter(
                        dataSet
                    )

                recyclerView = findViewById<RecyclerView>(R.id.rvSteps).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }

                val dataSetIng = ArrayList<IngredientContent.IngredientItem>()
                for (ingredient in rc.ingredients) {
                    val ii = IngredientContent.IngredientItem(ingredient.amount, ingredient.unit,
                    ingredient.name, ingredient.notes)
                    dataSetIng.add(ii)
                }

                viewManager = LinearLayoutManager(this)
                viewAdapter =
                    IngredientAdapter(
                        dataSetIng
                    )

                recyclerView = findViewById<RecyclerView>(R.id.rvIngredients).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }

                // Get Nutrional Data

                Log.d("testData", "preNNLOAD")
                Log.d("testData", rc.ingredients[0].name)

                loadNNData()

                loadStepContent(0)

            }

        }

        txtvPortion.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                // Update Portion Size
                // Get Original Portion Size
                try {
                    val ogs = rc.numOfServings.toInt()
                    val newss = txtvPortion.text.toString().toInt()
                    // Go Through each Ingredient
                    val dataSetIng = ArrayList<IngredientContent.IngredientItem>()
                    for (ingredient in rc.ingredients) {
                        var adjustedAmount = ingredient.amount

                        // Attempt to Update Amount of Ingredient
                        try {

                            var single = adjustedAmount.toDouble()
                            single /= ogs

                            adjustedAmount = (single * newss).toString()

                        } catch (e : Exception) {}

                        val ii = IngredientContent.IngredientItem(adjustedAmount, ingredient.unit,
                            ingredient.name, ingredient.notes)
                        dataSetIng.add(ii)
                    }

                    viewManager = LinearLayoutManager(baseContext)
                    viewAdapter =
                        IngredientAdapter(
                            dataSetIng
                        )

                    recyclerView = findViewById<RecyclerView>(R.id.rvIngredients).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }

                } catch (e : Exception) {}

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

    fun loadNNData() {

        Log.d("testData", "NNLOAD")

        val dd = loadNData(
            object : RecipeCallback {
                @SuppressLint("SetTextI18n")
                override fun getRecipe(result: MutableList<Document>) {
                    // Use Data
                    Log.d("testData", "INNERLOAD")
                    // Load in Nutrional Data
                    var fat = 0.0
                    var sodium = 0.0
                    var carbohydrate = 0.0
                    var protein = 0.0
                    var calories = 0.0

                    Log.d("testData", "preIngredCheck")

                    // Sort Data
                    rc.ingredients.forEach {

                        Log.d("testData", it.name)

                        // Check each Ingredient against Nutritional Database
                        val checkname = it.name.toLowerCase()

                        result.forEach { iit ->
                            if ((iit["ingredientName"] as String).toLowerCase(Locale.ROOT) == checkname && it.unit == "g") {
                                val mult : Double = it.amount.toDouble() / 100

                                fat += ((iit["fat"] as String).split(":")[0]).toDouble() * mult
                                sodium += ((iit["sodium"] as String).split(":")[0]).toDouble() * mult
                                carbohydrate += ((iit["carbohydrate"] as String).split(":")[0]).toDouble() * mult
                                protein += ((iit["protein"] as String).split(":")[0]).toDouble() * mult
                                calories += (iit["calories"] as String).toDouble() * mult

                            }
                        }



                    }

                    // Set Text Up
                    tvFA.text = String.format("%.2f", fat) + "g"
                    tvSA.text = String.format("%.2f", sodium) + "mg"
                    tvCA.text = String.format("%.2f", carbohydrate) + "g"
                    tvPA.text = String.format("%.2f", protein) + "g"
                    tvCAL.text = String.format("%.2f", calories)

                    tvFA.visibility = View.VISIBLE
                    tvSA.visibility = View.VISIBLE
                    tvCA.visibility = View.VISIBLE
                    tvPA.visibility = View.VISIBLE
                    tvCAL.visibility = View.VISIBLE

                }

            }

        )


    }

    private fun loadNData(rc : RecipeCallback) {

        val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Suggested Recipes
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            // Read from File for Suggested Recipes
            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("nutrition")
            val query = coll.find().limit(100)
            val result = mutableListOf<Document>()

            query.into(result).addOnSuccessListener {
                Log.d("testData", "callBack Nutrional Success")
                rc.getRecipe(result)
            }.addOnFailureListener {}

        }

    }

    fun moveStep (view : View) {

        val v : Chip = view as Chip
        if (v.id == chpPrev.id) {
            if (currentStep != 0) {
                currentStep -= 1
            }
        } else {
            if (currentStep != rc.steps.size - 1) {
                currentStep += 1
            }
        }

        loadStepContent(currentStep)

    }

    fun changeToAlternative(view : View) {
        val temp = rc.temperature.substring(0, rc.temperature.length - 2)
        val suffix = rc.temperature.takeLast(2)
        var celsius = rc.convertToCelsius(suffix, temp)
        celsius = celsius.substring(0, celsius.length - 2)

        try {
            when(view.tag) {
                "CC" -> {
                    txtvTemp.text = rc.convertTemperature("FO", celsius)
                    view.tag = "FO"
                }

                "FO" -> {
                    txtvTemp.text = rc.convertTemperature("GM", celsius)
                    view.tag = "GM"
                }

                "GM" -> {
                    txtvTemp.text = rc.convertTemperature("CC", celsius)
                    view.tag = "CC"
                }
            }
        } catch (e : Exception) {

        }

    }

    fun loadStepContent(stepNumber : Int) {

        // Update Step Number and Description
        if (rc.steps.size > 0) {
            tvStepNumber.text = rc.steps[stepNumber].stepNumber.toString()
            tvSBSDesc.text = rc.steps[stepNumber].description

            // Update Chips
            if (currentStep == 0) {
                chpPrev.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#DADADA"))
            } else {
                chpPrev.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#64B5F6"))
            }

            if (currentStep == rc.steps.size - 1) {
                chpNext.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#DADADA"))
            } else {
                chpNext.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#64B5F6"))
            }
        }

    }

    fun changeStepMode(view : View) {

        val v : Switch = view as Switch
        if (v.isChecked) {
            rvSteps.visibility = View.GONE
            clStepbyStep.visibility = View.VISIBLE
        } else {
            rvSteps.visibility = View.VISIBLE
            clStepbyStep.visibility = View.GONE
        }

    }

    fun changeStepIng(view : View) {

        val v : Chip = view as Chip
        var type : Int = 0


        if (v.tag == "not") {
            if (v.id == chpSteps.id) {
                chpIngredients.tag == "not"
                type = 0 // Steps
            } else {
                chpSteps.tag == "not"
                type = 1 // Ingredients
            }

            v.tag == "is"

        }

        updateContent(type)

    }

    fun updateContent(type : Int) {
        clNutrional.visibility = View.GONE
        // Update Chips and View
        if (type == 0) {

            swSBS.visibility = View.VISIBLE
            rvIngredients.visibility = View.GONE
            if (swSBS.isChecked) {
                clStepbyStep.visibility = View.VISIBLE
            } else {
                rvSteps.visibility = View.VISIBLE
            }

            chpSteps.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#64B5F6"))
            chpSteps.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")))

            chpIngredients.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#DADADA"))
            chpIngredients.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")))

        } else {

            clStepbyStep.visibility = View.GONE
            rvSteps.visibility = View.GONE
            rvIngredients.visibility = View.VISIBLE
            swSBS.visibility = View.GONE

            chpSteps.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#DADADA"))
            chpSteps.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")))

            chpIngredients.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#64B5F6"))
            chpIngredients.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")))

        }

    }

    fun loadReviews(uid : String) {

        // Load in Reviews
        val stitchAppClient = Stitch.getDefaultAppClient()

        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("reviews")

            val doc = Document().append("\$eq", uid)
            val docQuery = Document().append("link", doc)

            val query = coll.find(docQuery).limit(10)
            val result = mutableListOf<Document>()

            query.into(result).addOnSuccessListener {

                result.forEach {

                }

            }

        }

    }

    fun saveLocal(view : View) {

        val fh = com.michaelwoodroof.culinaryassistant.helper.FileHandler()

        if (fh.getRecipe(this, rc.id, false) == null) {
            fh.addRecipe(rc, this)
            btnSave.setColorFilter(Color.argb(204, 204, 204, 255))
        }

    }

    @SuppressLint("SetTextI18n")
    fun loadNutrionalData(view : View) {

        if (view.tag == "noclick") {
            view.tag = "click"
            clNutrional.visibility = View.VISIBLE
            rvIngredients.visibility = View.GONE
            rvSteps.visibility = View.GONE
            clStepbyStep.visibility = View.GONE
        } else {
            view.tag = "noclick"
            clNutrional.visibility = View.GONE
            rvIngredients.visibility = View.GONE
            rvSteps.visibility = View.GONE
            clStepbyStep.visibility = View.GONE
        }

    }

}
