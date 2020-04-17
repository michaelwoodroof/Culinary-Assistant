package com.michaelwoodroof.culinaryassistant.helper

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.overviews.RecipeDetail
import com.michaelwoodroof.culinaryassistant.overviews.RecipeOverview
import java.lang.StringBuilder

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

    fun makeVerticalCard(givenContext: Context, givenParent: ConstraintLayout, uid : String,
                         imgPath : String, title : String, spice : Int, description : String,
                         keywords : ArrayList<*>, difficulty : Int, reviewScore: Double,
                         prevID : Int) : Int {
        // Set up Views
        val cv = CardView(givenContext)
        val cl = ConstraintLayout(givenContext)

        // Set Properties
        cv.id = View.generateViewId()
        cv.tag = uid
        cv.radius = 20f

        cv.setOnClickListener { // @TODO REPLACE
            val intent = Intent(givenContext, MainActivity::class.java)
            givenContext.startActivity(intent)
        }

        cl.id = View.generateViewId()

        givenParent.addView(cv)

        // Create Constraint Set for CardView
        val setCV = ConstraintSet()
        setCV.clone(givenParent)
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

        setCV.applyTo(givenParent)
        cv.addView(cl)

        // Set - Up Remaining Views

        val img = ImageView(givenContext)
        val txtT = TextView(givenContext)

        val imgS1 = ImageView(givenContext)
        val imgS2 = ImageView(givenContext)
        val imgS3 = ImageView(givenContext)
        val imgS4 = ImageView(givenContext)
        val imgS5 = ImageView(givenContext)

        val txtD = TextView(givenContext)
        val txtTags = TextView(givenContext)
        val txtDiff = TextView(givenContext)

        val txtRating = TextView(givenContext)
        val imgStar = ImageView(givenContext)

        // Set Properties
        val res : Resources = givenContext.resources
        img.id = View.generateViewId()
        if (imgPath != "") {
            val bm : Bitmap = ImageConversions.stringToBitMap(imgPath as String)
            img.setImageBitmap(bm)
        } else {
            img.setImageResource(R.drawable.rplaceholder)
        }

        txtT.id = View.generateViewId()
        txtT.text = title as String
        txtT.textSize = 12F

        imgS1.id = View.generateViewId()
        imgS2.id = View.generateViewId()
        imgS3.id = View.generateViewId()
        imgS4.id = View.generateViewId()
        imgS5.id = View.generateViewId()

        // Adjust Spice Accordingly
        if (spice as Int == 0) {
            imgS1.visibility = View.INVISIBLE
            imgS2.visibility = View.INVISIBLE
            imgS3.visibility = View.INVISIBLE
            imgS4.visibility = View.INVISIBLE
            imgS5.visibility = View.INVISIBLE
        } else {
            if (spice as Int == 1) {
                imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS1.setColorFilter(Color.RED)
                imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (spice as Int == 2) {
                imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS1.setColorFilter(Color.RED)
                imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS2.setColorFilter(Color.RED)
                imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (spice as Int == 3) {
                imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS1.setColorFilter(Color.RED)
                imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS2.setColorFilter(Color.RED)
                imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS3.setColorFilter(Color.RED)
                imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (spice as Int == 4) {
                imgS1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS1.setColorFilter(Color.RED)
                imgS2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS2.setColorFilter(Color.RED)
                imgS3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS3.setColorFilter(Color.RED)
                imgS4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                imgS4.setColorFilter(Color.RED)
                imgS5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (spice as Int == 5) {
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
        txtD.text = description as String
        txtD.maxLines = 5
        txtD.minLines = 1
        txtD.textSize = 10F

        txtTags.id = View.generateViewId()
        txtTags.textSize = 10F

        // Run for Loop for Tags

        val sb = StringBuilder()
        for (tag in keywords) {
            sb.append("#$tag ")
        }
        txtTags.text = sb.toString()

        txtDiff.id = View.generateViewId()
        txtDiff.textSize = 10F

        if (difficulty as Int == 0) {
            txtDiff.text = "Novice"
        } else if (difficulty as Int == 1) {
            txtDiff.text = "Intermediate"
        } else if (difficulty as Int == 2) {
            txtDiff.text = "Expert"
        }

        txtRating.id = View.generateViewId()
        val rs = reviewScore as Double
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

    fun renderFiller(givenContext: Context, givenParent: ConstraintLayout, prevID: Int,
    givenHeight : Int, givenWidth : Int) : Int {

        val filler = TextView(givenContext)
        filler.id = View.generateViewId()

        givenParent.addView(filler)

        // Set Constraint
        val setFiller = ConstraintSet()
        setFiller.clone(givenParent)

        setFiller.constrainWidth(filler.id, givenWidth)
        setFiller.constrainHeight(filler.id, givenHeight)
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