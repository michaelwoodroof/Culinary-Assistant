package com.michaelwoodroof.culinaryassistant.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.overviews.Categories
import com.michaelwoodroof.culinaryassistant.overviews.CategoryOverview
import com.michaelwoodroof.culinaryassistant.overviews.RecipeDetail
import com.michaelwoodroof.culinaryassistant.overviews.RecipeOverview

object RenderCard {

    fun makeHorizontalCard(givenContext : Context, givenParent : ConstraintLayout, lowerText : String,
    imgPath : String, hasRating : Boolean, uid : String, cuisine : String,
                 reviewScore : Double, prevID : Int) : Int {

        // Make Card View
        val cv = CardView(givenContext)


        // Assign Values for Cardview
        cv.id = View.generateViewId()
        cv.tag = uid
        cv.radius = 20F
        cv.setCardBackgroundColor(Color.parseColor(CategoryColor.getColor(cuisine)))

        // Set Onclick
        if (hasRating) {
            cv.setOnClickListener {
                val intent = Intent(givenContext, RecipeDetail::class.java)
                intent.putExtra("uid", cv.tag as String)
                givenContext.startActivity(intent)
            }
        } else {
            cv.setOnClickListener {
                val intent = Intent(givenContext, RecipeOverview::class.java)
                intent.putExtra("type", "category")
                intent.putExtra("category", cv.tag as String)
                givenContext.startActivity(intent)
            }
        }

        // Add to the Parent
        givenParent.addView(cv)

        // Set up Constraints
        val setCV = ConstraintSet()
        setCV.clone(givenParent)
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
        if (prevID == givenParent.id) {
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
        // Apply Constraints
        setCV.applyTo(givenParent)

        // Give Constraint Layout
        val cl = ConstraintLayout(givenContext)
        cl.id = View.generateViewId()

        cv.addView(cl)

        // Set-up Controls
        val imageView = ImageView(givenContext)
        val textView = TextView(givenContext)

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

        val rating = TextView(givenContext)

        if (hasRating) {
            rating.id = View.generateViewId()
            rating.tag = uid
            val dr : Double = reviewScore
            val sr = dr.toBigDecimal().toPlainString()
            rating.text = sr
            rating.setBackgroundResource(R.drawable.roundedtextview)
            rating.textSize = 10F
            val myDraw : Drawable = ContextCompat.getDrawable(givenContext, R.drawable.ic_star_black_8dp)!!
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

        return cv.id // New ID

    }

    fun makeVerticalCard() {

    }

    fun renderFiller(givenContext: Context, givenParent: ConstraintLayout, prevID: Int) : Int {

        val filler = TextView(givenContext)
        filler.id = View.generateViewId()

        givenParent.addView(filler)

        // Set Constraint
        val setFiller = ConstraintSet()
        setFiller.clone(givenParent)

        setFiller.constrainWidth(filler.id, 80)
        setFiller.constrainHeight(filler.id, 440)
        setFiller.connect(
            filler.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        setFiller.connect(
            filler.id,
            ConstraintSet.LEFT,
            prevID,
            ConstraintSet.RIGHT,
            16
        )
        setFiller.applyTo(givenParent)

        return filler.id

    }



}