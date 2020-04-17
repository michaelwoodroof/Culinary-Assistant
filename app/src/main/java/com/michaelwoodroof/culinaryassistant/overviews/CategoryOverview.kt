package com.michaelwoodroof.culinaryassistant.overviews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_category_overview.*
import org.bson.Document

class CategoryOverview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_overview)

        var prevID = clRecipes.id
        val fText = intent?.getStringExtra("category") + " Recipes"
        txtvView.text = fText

        // Call Recipes Collection
        val stitchAppClient = Stitch.getDefaultAppClient()
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("recipes")
            // Cuisine = Given Category
            val query = coll.find(Document("cuisine", intent?.getStringExtra("category"))).sort(Document("title", 1)).limit(30)
            val result = mutableListOf<Document>()
            var counter = 0


            query.into(result).addOnSuccessListener {
                result.forEach {

                    // Add Controls
                    val title = it["title"] as String
                    val uid = it["uid"] as String
                    val desc = it["description"] as String
                    val keywords = "DUMMY"

                    // Create Controls
                    val imageView = ImageView(this)
                    val txtTitle = TextView(this)
                    val txtDesc = TextView(this)
                    val txtKeywords = TextView(this)

                    // Set Properties
                    imageView.id = View.generateViewId()
                    imageView.tag = uid
                    imageView.setImageResource(R.drawable.rplaceholder) // UPDATE

                    // Set Properties
                    txtTitle.id = View.generateViewId()
                    txtTitle.tag = uid
                    txtTitle.text = title

                    // Set Properties
                    txtDesc.id = View.generateViewId()
                    txtDesc.tag = uid
                    txtDesc.text = desc
                    txtDesc.maxLines = 100
                    txtDesc.textSize = 10.0f
                    //txtDesc.isSingleLine = false

                    // Set Properties
                    txtKeywords.id = View.generateViewId()
                    txtKeywords.tag = uid
                    // Run for Loop for Text
//                    val sb = StringBuilder()
//                    for (tag in keywords) {
//                        sb.append("#$tag ")
//                    }
                    txtKeywords.text = keywords

                    // Add to Constraint Layout
                    clRecipes.addView(imageView)
                    clRecipes.addView(txtTitle)
                    clRecipes.addView(txtDesc)
                    clRecipes.addView(txtKeywords)

                    // Set Layouts
                    val setImg = ConstraintSet()
                    setImg.clone(clRecipes)
                    setImg.constrainHeight(imageView.id, 280)
                    setImg.constrainWidth(imageView.id, 280)
                    setImg.connect(imageView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 32)
                    if (prevID == clRecipes.id) {
                        setImg.connect(imageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 32)
                    } else {
                        setImg.connect(imageView.id, ConstraintSet.TOP, prevID, ConstraintSet.BOTTOM, 32)
                    }
                    setImg.applyTo(clRecipes)

                    val setTitle = ConstraintSet()
                    setTitle.clone(clRecipes)
                    setTitle.connect(txtTitle.id, ConstraintSet.LEFT, imageView.id, ConstraintSet.RIGHT, 16)
                    setTitle.connect(txtTitle.id, ConstraintSet.TOP, imageView.id, ConstraintSet.TOP)
                    setTitle.applyTo(clRecipes)

                    val setDesc = ConstraintSet()
                    setDesc.clone(clRecipes)
                    setDesc.connect(txtDesc.id, ConstraintSet.LEFT, txtTitle.id, ConstraintSet.LEFT)
                    setDesc.connect(txtDesc.id, ConstraintSet.TOP, txtTitle.id, ConstraintSet.BOTTOM, 8)
                    setImg.constrainWidth(imageView.id, 100)
                    setDesc.applyTo(clRecipes)

                    val setKeywords = ConstraintSet()
                    setKeywords.clone(clRecipes)
                    setKeywords.connect(txtKeywords.id, ConstraintSet.LEFT, txtTitle.id, ConstraintSet.LEFT)
                    setKeywords.connect(txtKeywords.id, ConstraintSet.BOTTOM, imageView.id, ConstraintSet.BOTTOM)
                    setKeywords.constrainWidth(txtKeywords.id, 0)
                    setKeywords.applyTo(clRecipes)

                    // Add Buffer
                    if (counter == 29) {

                    }

                    counter += 1
                    prevID = imageView.id

                }
            }

        }
    }

    fun goBack(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
