package com.michaelwoodroof.culinaryassistant.overviews

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.Conversions
import com.michaelwoodroof.culinaryassistant.helper.RenderCard
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_recipe_overview.*
import org.bson.Document
import org.bson.types.Decimal128

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

        // Call Database for Categories
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            // Get Categories
            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("recipes")
            val query = coll.find(Document("cuisine", intent?.getStringExtra("category"))).sort(Document("reviewScore", 1)).limit(20)
            val result = mutableListOf<Document>()
            var prevID = -1
            var counter = 0
            var tempInt = 0

            query.into(result).addOnSuccessListener {
                result.forEach {

                    Conversions.convertUIDtoRecipe(it["uid"] as String, baseContext)

                    if (counter == result.size - 1) {
                        tempInt = RenderCard.makeVerticalCard(this, clRecipes, it["uid"] as String,
                            it["imagePath"] as String, it["title"] as String, it["spice"] as Int,
                            it["description"] as String, it["keyWords"] as ArrayList<*>,
                            it["difficulty"] as Int, it["reviewScore"] as Decimal128, prevID)
                        RenderCard.renderFiller(this, clRecipes, tempInt, 400, 10) // @TODO MIGHT CHANGE SEEMS HIGH
                    } else {
                        prevID = RenderCard.makeVerticalCard(this, clRecipes, it["uid"] as String,
                            it["imagePath"] as String, it["title"] as String, it["spice"] as Int,
                            it["description"] as String, it["keyWords"] as ArrayList<*>,
                            it["difficulty"] as Int, it["reviewScore"] as Decimal128, prevID)
                    }
                    counter++
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
