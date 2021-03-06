package com.michaelwoodroof.culinaryassistant.overviews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.RenderCard
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_categories.*
import org.bson.Document
import org.bson.types.Decimal128

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

                    prevID = RenderCard.makeHorizontalCard(this, clCategories,
                        it["categoryTitle"] as String, it["imagePath"] as String, false,
                        it["categoryTitle"] as String, it["categoryTitle"] as String, Decimal128(0), prevID)

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
