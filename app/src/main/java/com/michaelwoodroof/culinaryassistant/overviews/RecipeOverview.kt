package com.michaelwoodroof.culinaryassistant.overviews

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.ImageConversions
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_recipe_overview.*
import kotlinx.android.synthetic.main.activity_recipe_overview.imgBacker
import org.bson.Document
import java.lang.StringBuilder

class RecipeOverview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_overview)

        val typeis = intent.getStringExtra("type")

        if (typeis == "category") {
            txtvView.text = Editable.Factory.getInstance().newEditable(intent?.getStringExtra("category") + " Recipes")
            loadCategoryRecipes()
        } else if (typeis == "community") {
            txtvView.text = Editable.Factory.getInstance().newEditable("Community Recipes")
            // query = coll.find(gt("totalReviews", 9)).sort(Document("reviewScore",1)).limit(30)

        } else if (typeis == "suggested") {
            txtvView.text = Editable.Factory.getInstance().newEditable("Suggested Recipes")
        }

    }

    fun loadCategoryRecipes() {

        val stitchAppClient = Stitch.getDefaultAppClient()

        Log.d("testData", "attempt initial call")

        // Call Database for Categories
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            // Get Categories
            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("recipes")
            val query = coll.find(Document("cuisine", intent?.getStringExtra("category"))).sort(Document("reviewScore", 1)).limit(20)
            val result = mutableListOf<Document>()
            var prevID = -1

            Log.d("testData", "attempt call")

            query.into(result).addOnSuccessListener {
                result.forEach {

                    prevID = renderCard(it, prevID)

                }

            }

        }.addOnFailureListener {
            loadCategoryRecipes()
        }

    }

    private fun renderCard(it: Document, prevID : Int): Int {
        // Set up Views
        val cv = CardView(this)
        val cl = ConstraintLayout(this)

        // Set Properties
        cv.id = View.generateViewId()
        cv.tag = it["uid"] as String
        cv.radius = 20f

        cv.setOnClickListener { loadDetailRecipe(cv.tag) }

        cl.id = View.generateViewId()

        clRecipes.addView(cv)

        // Create Constraint Set for CardView
        val setCV = ConstraintSet()
        setCV.clone(clRecipes)
        setCV.constrainHeight(cv.id, 320) // Adjust if Needed
        setCV.constrainWidth(cv.id, 0)
        // Check if First
        if (prevID != -1) {
            setCV.connect(
                cv.id,
                ConstraintSet.TOP,
                prevID,
                ConstraintSet.BOTTOM,
                50
            )
        } else {
            setCV.connect(
                cv.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                50
            )
        }

        setCV.connect(
            cv.id,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            50
        )

        setCV.connect(
            cv.id,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            50
        )

        setCV.applyTo(clRecipes)
        cv.addView(cl)

        // Set - Up Remaining Views

        val img = ImageView(this)
        val txtT = TextView(this)

        val imgS1 = ImageView(this)
        val imgS2 = ImageView(this)
        val imgS3 = ImageView(this)
        val imgS4 = ImageView(this)
        val imgS5 = ImageView(this)

        val txtD = TextView(this)
        val txtTags = TextView(this)
        val txtDiff = TextView(this)

        val txtRating = TextView(this)
        val imgStar = ImageView(this)

        // Set Properties
        val res : Resources = resources
        img.id = View.generateViewId()
        if (it["imagePath"] as String != "") {
            val bm : Bitmap = ImageConversions.stringToBitMap(it["imagePath"] as String)
            img.setImageBitmap(bm)
        } else {
            img.setImageResource(R.drawable.rplaceholder)
        }

        txtT.id = View.generateViewId()
        txtT.text = it["title"] as String
        txtT.textSize = 12F

        imgS1.id = View.generateViewId()
        imgS2.id = View.generateViewId()
        imgS3.id = View.generateViewId()
        imgS4.id = View.generateViewId()
        imgS5.id = View.generateViewId()

        // Adjust Spice Accordingly
        if (it["spice"] as Int == 0) {
            imgS1.visibility = View.INVISIBLE
            imgS2.visibility = View.INVISIBLE
            imgS3.visibility = View.INVISIBLE
            imgS4.visibility = View.INVISIBLE
            imgS5.visibility = View.INVISIBLE
        } else {
            if (it["spice"] as Int == 1) {
                imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS1.setColorFilter(Color.RED)
                imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (it["spice"] as Int == 2) {
                imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS1.setColorFilter(Color.RED)
                imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS2.setColorFilter(Color.RED)
                imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (it["spice"] as Int == 3) {
                imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS1.setColorFilter(Color.RED)
                imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS2.setColorFilter(Color.RED)
                imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS3.setColorFilter(Color.RED)
                imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (it["spice"] as Int == 4) {
                imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS1.setColorFilter(Color.RED)
                imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS2.setColorFilter(Color.RED)
                imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS3.setColorFilter(Color.RED)
                imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS4.setColorFilter(Color.RED)
                imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (it["spice"] as Int == 5) {
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

        txtD.id = View.generateViewId()
        txtD.text = it["description"] as String
        txtD.maxLines = 5
        txtD.minLines = 1
        txtD.textSize = 10F

        txtTags.id = View.generateViewId()
        txtTags.textSize = 10F

        // Run for Loop for Tags
        val tags = it["keyWords"] as ArrayList<*>

        val sb = StringBuilder()
        for (tag in tags) {
            sb.append("#$tag ")
        }
        txtTags.text = sb.toString()

        txtDiff.id = View.generateViewId()
        txtDiff.textSize = 10F

        if (it["difficulty"] as Int == 0) {
            txtDiff.text = "Novice"
        } else if (it["difficulty"] as Int == 1) {
            txtDiff.text = "Intermediate"
        } else if (it["difficulty"] as Int == 2) {
            txtDiff.text = "Expert"
        }

        txtRating.id = View.generateViewId()
        val rs = it["reviewScore"] as Double
        txtRating.text = rs.toString()
        txtRating.textSize = 10F

        imgStar.id = View.generateViewId()
        imgStar.setImageDrawable(res.getDrawable(R.drawable.ic_star_black_24dp))
        imgStar.setColorFilter(Color.argb(0, 0, 0, 255))

        // Add to Constraint Layout
        cl.addView(img)
        cl.addView(txtT)
        cl.addView(imgS1)
        cl.addView(imgS2)
        cl.addView(imgS3)
        cl.addView(imgS4)
        cl.addView(imgS5)
        cl.addView(txtD)
        cl.addView(txtTags)
        cl.addView(txtDiff)
        cl.addView(txtRating)
        cl.addView(imgStar)

        // Apply Constraints
        val setImg = ConstraintSet()
        setImg.clone(cl)
        setImg.constrainHeight(img.id, 320) // Adjust if Needed
        setImg.constrainWidth(img.id, 320)

        setImg.connect(
            img.id,
            ConstraintSet.TOP,
            cl.id,
            ConstraintSet.TOP
        )
        setImg.connect(
            img.id,
            ConstraintSet.LEFT,
            cl.id,
            ConstraintSet.LEFT
        )
        setImg.applyTo(cl)

        val setTitle = ConstraintSet()
        setTitle.clone(cl)

        setTitle.connect(
            txtT.id,
            ConstraintSet.LEFT,
            img.id,
            ConstraintSet.RIGHT,
            16
        )
        setTitle.connect(
            txtT.id,
            ConstraintSet.TOP,
            cl.id,
            ConstraintSet.TOP,
            4
        )
        setTitle.applyTo(cl)

        val sDS = 48

        val setSpice1 = ConstraintSet()
        setSpice1.clone(cl)
        setSpice1.constrainHeight(imgS1.id, sDS)
        setSpice1.constrainWidth(imgS1.id, sDS)

        setSpice1.connect(
            imgS1.id,
            ConstraintSet.TOP,
            txtT.id,
            ConstraintSet.BOTTOM,
            2
        )

        setSpice1.connect(
            imgS1.id,
            ConstraintSet.LEFT,
            txtT.id,
            ConstraintSet.LEFT
        )

        setSpice1.applyTo(cl)

        val setSpice2 = ConstraintSet()
        setSpice2.clone(cl)
        setSpice2.constrainHeight(imgS2.id, sDS)
        setSpice2.constrainWidth(imgS2.id, sDS)

        setSpice2.connect(
            imgS2.id,
            ConstraintSet.TOP,
            txtT.id,
            ConstraintSet.BOTTOM,
            2
        )

        setSpice2.connect(
            imgS2.id,
            ConstraintSet.LEFT,
            imgS1.id,
            ConstraintSet.RIGHT
        )

        setSpice2.applyTo(cl)

        val setSpice3 = ConstraintSet()
        setSpice3.clone(cl)
        setSpice3.constrainHeight(imgS3.id, sDS)
        setSpice3.constrainWidth(imgS3.id, sDS)

        setSpice3.connect(
            imgS3.id,
            ConstraintSet.TOP,
            txtT.id,
            ConstraintSet.BOTTOM,
            2
        )

        setSpice3.connect(
            imgS3.id,
            ConstraintSet.LEFT,
            imgS2.id,
            ConstraintSet.RIGHT
        )

        setSpice3.applyTo(cl)

        val setSpice4 = ConstraintSet()
        setSpice4.clone(cl)
        setSpice4.constrainHeight(imgS4.id, sDS)
        setSpice4.constrainWidth(imgS4.id, sDS)

        setSpice4.connect(
            imgS4.id,
            ConstraintSet.TOP,
            txtT.id,
            ConstraintSet.BOTTOM,
            2
        )

        setSpice4.connect(
            imgS4.id,
            ConstraintSet.LEFT,
            imgS3.id,
            ConstraintSet.RIGHT
        )

        setSpice4.applyTo(cl)

        val setSpice5 = ConstraintSet()
        setSpice5.clone(cl)
        setSpice5.constrainHeight(imgS5.id, sDS)
        setSpice5.constrainWidth(imgS5.id, sDS)

        setSpice5.connect(
            imgS5.id,
            ConstraintSet.TOP,
            txtT.id,
            ConstraintSet.BOTTOM,
            2
        )

        setSpice5.connect(
            imgS5.id,
            ConstraintSet.LEFT,
            imgS4.id,
            ConstraintSet.RIGHT
        )

        setSpice5.applyTo(cl)

        val setDesc = ConstraintSet()
        setDesc.clone(cl)
        setDesc.constrainWidth(txtD.id, 0)

        setDesc.connect(
            txtD.id,
            ConstraintSet.LEFT,
            txtT.id,
            ConstraintSet.LEFT
        )

        setDesc.connect(
            txtD.id,
            ConstraintSet.RIGHT,
            cl.id,
            ConstraintSet.RIGHT,
            8
        )

        setDesc.connect(
            txtD.id,
            ConstraintSet.TOP,
            imgS1.id,
            ConstraintSet.BOTTOM,
            4
        )

        setDesc.applyTo(cl)

        val setDiff = ConstraintSet()
        setDiff.clone(cl)

        setDiff.connect(
            txtDiff.id,
            ConstraintSet.TOP,
            imgS1.id,
            ConstraintSet.TOP
        )

        setDiff.connect(
            txtDiff.id,
            ConstraintSet.BOTTOM,
            imgS1.id,
            ConstraintSet.BOTTOM
        )

        setDiff.connect(
            txtDiff.id,
            ConstraintSet.RIGHT,
            txtD.id,
            ConstraintSet.RIGHT,
            8
        )

        setDiff.applyTo(cl)

        val setStar = ConstraintSet()
        setStar.clone(cl)
        setStar.constrainWidth(imgStar.id, 30)
        setStar.constrainHeight(imgStar.id, 30)

        setStar.connect(
            imgStar.id,
            ConstraintSet.TOP,
            txtT.id,
            ConstraintSet.TOP
        )

        setStar.connect(
            imgStar.id,
            ConstraintSet.BOTTOM,
            txtT.id,
            ConstraintSet.BOTTOM
        )

        setStar.connect(
            imgStar.id,
            ConstraintSet.RIGHT,
            txtD.id,
            ConstraintSet.RIGHT,
            8
        )

        setStar.applyTo(cl)

        val setRating = ConstraintSet()
        setRating.clone(cl)

        setRating.connect(
            txtRating.id,
            ConstraintSet.TOP,
            imgStar.id,
            ConstraintSet.TOP
        )

        setRating.connect(
            txtRating.id,
            ConstraintSet.BOTTOM,
            imgStar.id,
            ConstraintSet.BOTTOM
        )

        setRating.connect(
            txtRating.id,
            ConstraintSet.RIGHT,
            imgStar.id,
            ConstraintSet.LEFT,
            1
        )

        setRating.applyTo(cl)

        val setTags = ConstraintSet()
        setTags.clone(cl)

        setTags.connect(
            txtTags.id,
            ConstraintSet.LEFT,
            txtD.id,
            ConstraintSet.LEFT
        )

        setTags.connect(
            txtTags.id,
            ConstraintSet.BOTTOM,
            cl.id,
            ConstraintSet.BOTTOM,
            4
        )

        setTags.applyTo(cl)

        // Apply Constraints to Views
       return cv.id
    }

    private fun openCategory(catName : String) {
        // Load Category Specific View
        val intent = Intent(this, RecipeOverview::class.java)
        intent.putExtra("type", "category")
        intent.putExtra("category", catName)
        startActivity(intent)
    }

    fun goBack(view : View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun loadDetailRecipe(tag : Any) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}
