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
import com.michaelwoodroof.culinaryassistant.helper.RenderCard
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

//                    prevID = renderCard(it["categoryTitle"] as String, it["imagePath"] as String, false, clCategories, prevID,
//                        it["categoryTitle"] as String, it["categoryTitle"] as String, 0.0, false)

                    prevID = RenderCard.makeHorizontalCard(this, clCategories, it["categoryTitle"] as String,
                                                it["imagePath"] as String, false, it["categoryTitle"] as String, it["categoryTitle"] as String,
                                                0.0, prevID)

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

    fun goBack(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
