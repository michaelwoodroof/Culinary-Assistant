package com.michaelwoodroof.culinaryassistant.createRecipe

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.net.toUri
import androidx.core.view.children
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.ImageConversions
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import com.michaelwoodroof.culinaryassistant.structure.Section
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_create_recipe_s5.*
import org.bson.Document
import java.io.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class CreateRecipeS5 : AppCompatActivity() {

    var lastRef : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe_s5)
    }

    fun goBack(view: View) {
        var pr: Recipe? = intent.getParcelableExtra("partialrecipe")
        val intent = Intent(this, CreateRecipeS4::class.java)
        intent.putExtra("partialrecipe", pr as Parcelable)
        startActivity(intent)
    }

    override fun onBackPressed() {
        // Ensures that Data is Kept Between Screens
        val b : Button = Button(this) // Empty View
        goBack(b)
    }

    fun addStep(view : View) {

        // Set up Views

        val img = ImageView(this)
        val txtv = TextView(this)
        val txtd = TextView(this)

        // Set up Properties

        img.id = View.generateViewId()
        img.setImageResource(R.drawable.ic_circle_fill)
        img.scaleX = 1.5F
        img.scaleY = 1.5F

        txtv.id = View.generateViewId()
        txtv.text = txtStepNum.text


        txtd.id = View.generateViewId()
        txtd.maxLines = 10
        txtd.minLines = 1
        txtd.tag = "desc"
        txtd.text = txtStepDesc.text

        clSteps.addView(img)
        clSteps.addView(txtv)
        clSteps.addView(txtd)

        // Set Constraints

        val setDsc = ConstraintSet()
        setDsc.clone(clSteps)
        setDsc.constrainWidth(txtd.id, 0)

        // WRAP IN IF STATEMENT
        if (lastRef == 0) {
            setDsc.connect (
                txtd.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                36
            )
        } else {
            setDsc.connect (
                txtd.id,
                ConstraintSet.TOP,
                lastRef,
                ConstraintSet.BOTTOM,
                36
            )
        }

        setDsc.connect (
            txtd.id,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT
        )

        setDsc.connect (
            txtd.id,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            192
        )
        setDsc.applyTo(clSteps)

        val setImg = ConstraintSet()
        setImg.clone(clSteps)

        setImg.connect (
            img.id,
            ConstraintSet.TOP,
            txtd.id,
            ConstraintSet.TOP
        )

        setImg.connect (
            img.id,
            ConstraintSet.BOTTOM,
            txtd.id,
            ConstraintSet.BOTTOM
        )

        setImg.connect (
            img.id,
            ConstraintSet.RIGHT,
            txtd.id,
            ConstraintSet.LEFT
        )

        setImg.connect (
            img.id,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT
        )
        setImg.applyTo(clSteps)

        val setTxt = ConstraintSet()
        setTxt.clone(clSteps)

        setTxt.connect (
            txtv.id,
            ConstraintSet.TOP,
            img.id,
            ConstraintSet.TOP
        )

        setTxt.connect (
            txtv.id,
            ConstraintSet.BOTTOM,
            img.id,
            ConstraintSet.BOTTOM
        )

        setTxt.connect (
            txtv.id,
            ConstraintSet.RIGHT,
            img.id,
            ConstraintSet.RIGHT
        )

        setTxt.connect (
            txtv.id,
            ConstraintSet.LEFT,
            img.id,
            ConstraintSet.LEFT
        )
        setTxt.applyTo(clSteps)

        txtStepDesc.text = Editable.Factory.getInstance().newEditable("")
        txtStepNum.text = Editable.Factory.getInstance().newEditable("")

        lastRef = txtd.id

    }

    fun createRecipe(view : View) {
        val pr: Recipe? = intent.getParcelableExtra("partialrecipe")
        // Updates Recipe with Stage 5 Recipe
        // @TODO ADD FIELDS + ERROR CHECKING
        var stepNo = 0
        for (child in clSteps.children) {
            if (child is TextView) {
                if (child.tag == "desc") {
                    val section = Section(stepNo, child.text.toString())
                    val current = pr!!.steps
                    current.add(section)
                    pr.steps = current
                } else {
                    stepNo = child.text.toString().toInt()
                }
            }
        }

        val str = UUID.randomUUID().toString()
        val uid = "uid$str"
        val fileOutputStream : FileOutputStream
        if (pr != null) {
            uploadToDB(pr, uid)
        }

//        try {
//            // Set Up File Stream
//            fileOutputStream = openFileOutput(uid, Context.MODE_PRIVATE)
//            /*
//            Write Recipe Data to Local Storage
//            Write Title
//            */
//            fileOutputStream.write(pr!!.title.toByteArray())
//            /* Write Description */
//            fileOutputStream.write(pr.description.toByteArray())
//            /* Write Difficulty */
//            fileOutputStream.write(pr.difficulty)
//            /*
//            Write Image
//            @TODO UPDATE
//            Write Prep Time
//            Write Cook Time
//            Write Cuisine Type
//            Write Meal Type
//            Write Spice Rating
//            Write isFreezable
//            Write Keywords
//            Write Portion Size
//            Write Ingredients
//            Write Allergens
//            Write Steps
//            */
//
//
//        } catch (e : Exception) {
//            e.printStackTrace()
//        }

        Toast.makeText(applicationContext,"Recipe Saved", Toast.LENGTH_LONG).show()

        // Load Main Menu
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    private fun uploadToDB(pr : Recipe, uid : String) {
        val stitchAppClient = Stitch.getDefaultAppClient()
        val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
        val coll = client.getDatabase("appdata").getCollection("recipes")

        val recipeDoc = Document()

        // Stage 1
        recipeDoc["title"] = pr.title
        recipeDoc["description"] = pr.description
        recipeDoc["difficulty"] = pr.difficulty

        if (pr.imgReference != "") {
            recipeDoc["imagePath"] = ImageConversions.bitMapToString(ImageConversions.uriToBitMap(pr.uriRef.toUri(), this.contentResolver))
        } else {
            recipeDoc["imagePath"] = ""
        }

        // Stage 2
        recipeDoc["prepTime"] = pr.prepTime
        recipeDoc["cookTime"] = pr.cookTime
        recipeDoc["cuisine"] = pr.cuisine
        recipeDoc["courseType"] = pr.courseType
        recipeDoc["spice"] = pr.spice

        // Stage 3
        recipeDoc["isFreezable"] = pr.isFreezable
        recipeDoc["keyWords"] = pr.keywords

        // Stage 4
        recipeDoc["numberOfServings"] = pr.numOfServings

        val ingredientData = ArrayList<Document>()
        // Iterate through Ingredients
        for (ingredient in pr.ingredients) {
            val data : Document = Document("name", ingredient.name)
            data.append("amount", ingredient.amount)
            data.append("unit", ingredient.unit)
            data.append("notes", ingredient.notes)
            ingredientData.add(data)
        }

        val dietData = ArrayList<Document>()
        // Iterate through Dietary Information
        for (dietary in pr.dietary) {
            val data = Document("name", dietary.name)
            dietData.add(data)
        }

        recipeDoc["ingredients"] = ingredientData
        recipeDoc["dietary"] = dietData

        // Stage 5
        val stepsData = ArrayList<Document>()
        // Iterate through Steps
        for (step in pr.steps) {
            val data = Document("stepNumber", step.stepNumber)
            data.append("description", step.description)
            stepsData.add(data)
        }
        recipeDoc["steps"] = stepsData

        recipeDoc["numberOfServings"] = pr.numOfServings

        // Others that Need Adding
        recipeDoc["ingredientSection"] = "Add this"
        recipeDoc["stepsSection"] = "Add this"
        recipeDoc["temperature"] = "Add this"
        recipeDoc["author"] = "Add this"
        recipeDoc["source"] = "Add this"

        // Set when Uploading to Database
        recipeDoc["uid"] = uid
        recipeDoc["reviewScore"] = 0.0
        recipeDoc["totalReviews"] = 0
        coll.insertOne(recipeDoc).addOnSuccessListener {
            Toast.makeText(applicationContext,"Recipe uploaded", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(applicationContext,"Recipe couldn't be upload", Toast.LENGTH_LONG).show()
        }

    }

}
