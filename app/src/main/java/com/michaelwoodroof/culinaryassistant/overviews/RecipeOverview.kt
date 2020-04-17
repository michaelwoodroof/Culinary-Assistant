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
import com.michaelwoodroof.culinaryassistant.helper.RenderCard
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
            Log.d("testData", "RO Load")
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

                    prevID = RenderCard.makeVerticalCard(this, clRecipes, it["uid"] as String,
                    it["imagePath"] as String, it["title"] as String, it["spice"] as Int,
                        it["description"] as String, it["keyWords"] as ArrayList<*>,
                        it["difficulty"] as Int, it["reviewScore"] as Double, prevID)

                }

            }

        }.addOnFailureListener {
            loadCategoryRecipes()
        }

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
