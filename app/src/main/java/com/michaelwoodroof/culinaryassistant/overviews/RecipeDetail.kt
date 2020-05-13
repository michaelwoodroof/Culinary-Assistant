package com.michaelwoodroof.culinaryassistant.overviews

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.michaelwoodroof.culinaryassistant.adapters.IngredientAdapter
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.adapters.StepAdapter
import com.michaelwoodroof.culinaryassistant.structure.IngredientContent
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import com.michaelwoodroof.culinaryassistant.structure.StepContent
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import org.bson.Document


class RecipeDetail : AppCompatActivity() {

    // Used throughout
    lateinit var rc : Recipe
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
                    clReview.visibility = View.GONE // @TODO REMOVE
                    val uid = intent.getStringExtra("uid")
                    loadReviews(uid)
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

                val temperatureSuffix = "°C"

                txtvTemp.text = getString(R.string.tempFormat, "", rc.temperature, temperatureSuffix)

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

                txtvServings.text = getString(R.string.servingFormat, rc.numOfServings)
                txtvAuthor.text = rc.author

                // Configure Allergens
                if (rc.dietary.size == 0) {
                    txtvAllergens.visibility = View.GONE
                } else {
                    txtvAllergens.visibility = View.VISIBLE
                    for (diet in rc.dietary) {

                        // Add Allergens into Allergen List @TODO EXPAND
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

                loadStepContent(0)

            }

        }

    }

    fun moveStep (view : View) {

        val v : Chip = view as Chip
        Log.d("testData", currentStep.toString())
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

        Log.d("testData", v.tag as String)

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

        Log.d("searchTest", "IT loaded")

        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("reviews")

            val doc = Document().append("\$eq", uid)
            val docQuery = Document().append("link", doc)

            val query = coll.find(docQuery).limit(10)
            val result = mutableListOf<Document>()

            query.into(result).addOnSuccessListener {

                result.forEach {
                    Log.d("searchTest", it["contents"] as String)
                }

            }

        }

    }

}
