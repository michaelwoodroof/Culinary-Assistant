package com.michaelwoodroof.culinaryassistant.overviews

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.CategoryColor
import com.michaelwoodroof.culinaryassistant.helper.ImageConversions
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_main_nav.*
import kotlinx.android.synthetic.main.activity_main_nav.clCategories
import org.bson.Document

class Categories : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        // Load all Categories
        // Needed Variables
        createCards()

    }

    private fun createCards() {

        var prevID = clCategories.id
        val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Categories
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            // Get Categories
            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("categories")
            val query = coll.find().sort(Document("categoryTitle", 1))
            val result = mutableListOf<Document>()
            var counter = 0

            query.into(result).addOnSuccessListener {
                result.forEach {

                    prevID = renderCard(it["categoryTitle"] as String, it["imagePath"] as String, false, clCategories, prevID,
                        it["categoryTitle"] as String, it["categoryTitle"] as String, 0.0, false)
                    Log.d("testData", "catAdded")

                    clFlowHelper.referencedIds = clFlowHelper.referencedIds.plus(prevID)

                }
            }

        }.addOnFailureListener {
            createCards()
        }

    }

    private fun openCategory(catName : String) {
        // Load Category Specific View
        val intent = Intent(this, RecipeOverview::class.java)
        intent.putExtra("type", "category")
        intent.putExtra("category", catName)
        startActivity(intent)
    }

    fun renderCard(lowerText : String, imgPath : String, hasRating : Boolean, container : ConstraintLayout, prevID : Int,
                   uid : String, cuisine : String, reviewScore : Double, hasFiller : Boolean) : Int {

        // Set Up CardView
        val cv = CardView(this)

        cv.id = View.generateViewId()
        cv.tag = uid
        cv.radius = 20f
        //
        cv.setCardBackgroundColor(Color.parseColor(CategoryColor.getColor(cuisine)))
        cv.setOnClickListener { openCategory(cv.tag as String) }

        // Add to ConstraintLayout
        container.addView(cv)

        // Set Constraints
        val setCV = ConstraintSet()
        setCV.clone(container)
        setCV.constrainWidth(cv.id, 320)
        setCV.constrainHeight(cv.id, 440)
        setCV.connect(
            cv.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        setCV.connect(
            cv.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        // Set Depending on ID
        if (prevID == container.id) {
            setCV.connect(
                cv.id,
                ConstraintSet.LEFT,
                prevID,
                ConstraintSet.LEFT
            )
        } else {
            setCV.connect(
                cv.id,
                ConstraintSet.LEFT,
                prevID,
                ConstraintSet.RIGHT,
                16
            )
        }
        setCV.applyTo(container)

        // Set Up Constraint for Views
        val cl = ConstraintLayout(this)
        cl.id = View.generateViewId()

        cv.addView(cl)

        // Set-up Controls
        val imageView = ImageView(this)
        val textView = TextView(this)

        // Set Properties
        imageView.id = View.generateViewId()
        imageView.tag = uid
        if (imgPath != "") {
            val bm : Bitmap = ImageConversions.stringToBitMap(imgPath)
            imageView.setImageBitmap(bm)
        } else {
            imageView.setImageResource(R.drawable.rplaceholder) // UPDATE
        }

        textView.id = View.generateViewId()
        textView.tag = uid
        textView.text = lowerText
        textView.textSize = 12F
        textView.maxLines = 2
        textView.minLines = 1
        textView.setTextColor(Color.parseColor("#FFFFFF"))
        textView.setPadding(0, 4, 0, 4)

        // Add Constraints
        cl.addView(imageView)
        cl.addView(textView)

        val rating = TextView(this)

        if (hasRating) {
            rating.id = View.generateViewId()
            rating.tag = uid
            val dr : Double = reviewScore
            val sr = dr.toBigDecimal().toPlainString()
            rating.text = sr
            rating.setBackgroundResource(R.drawable.roundedtextview)
            rating.textSize = 10F
            val myDraw : Drawable = ContextCompat.getDrawable(this, R.drawable.ic_star_black_8dp)!!
            myDraw.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
            rating.setCompoundDrawablesWithIntrinsicBounds(null, null, myDraw, null)
            rating.compoundDrawablePadding = 4
            cl.addView(rating)
        }

        // Set Constraints
        val setImg = ConstraintSet()
        setImg.clone(cl)
        setImg.constrainHeight(imageView.id, 320)
        setImg.constrainWidth(imageView.id, 320)
        setImg.connect(
            imageView.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        setImg.connect(
            imageView.id,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT
        )
        setImg.applyTo(cl)

        val setTxt = ConstraintSet()
        setTxt.clone(cl)
        setTxt.connect(
            textView.id,
            ConstraintSet.TOP,
            imageView.id,
            ConstraintSet.BOTTOM
        )
        setTxt.connect(
            textView.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        setTxt.connect(
            textView.id,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            16
        )
        setTxt.connect(
            textView.id,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            16
        )
        setTxt.constrainWidth(textView.id, 0)
        setTxt.applyTo(cl)

        if (hasRating) {
            val setRating = ConstraintSet()
            setRating.clone(cl)
            setRating.connect(
                rating.id,
                ConstraintSet.TOP,
                imageView.id,
                ConstraintSet.TOP
            )
            setRating.connect(
                rating.id,
                ConstraintSet.BOTTOM,
                imageView.id,
                ConstraintSet.BOTTOM
            )
            setRating.connect(
                rating.id,
                ConstraintSet.RIGHT,
                imageView.id,
                ConstraintSet.RIGHT
            )
            setRating.setVerticalBias(rating.id, 0.8F)
            setRating.applyTo(cl)
        }

        if (hasFiller) {
            Log.d("testData", "filler running")
            val bufferView = TextView(this)
            bufferView.id = View.generateViewId()

            container.addView(bufferView)

            // Set Buffer Layout
            val setBuffer = ConstraintSet()
            setBuffer.clone(container)
            setBuffer.constrainHeight(bufferView.id, 280)
            setBuffer.constrainWidth(bufferView.id, 66)
            setBuffer.connect(
                bufferView.id,
                ConstraintSet.TOP,
                prevID,
                ConstraintSet.TOP
            )
            setBuffer.connect(
                bufferView.id,
                ConstraintSet.LEFT,
                prevID,
                ConstraintSet.RIGHT
            )
            setBuffer.applyTo(container)
        }

        return cv.id // New Previous ID

    }

    fun goBack(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
