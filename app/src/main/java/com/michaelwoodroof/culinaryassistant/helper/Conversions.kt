package com.michaelwoodroof.culinaryassistant.helper

import android.content.Context
import android.util.Log
import com.michaelwoodroof.culinaryassistant.structure.*
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import org.bson.Document
import kotlin.collections.ArrayList

object Conversions {

    fun convertUIDtoRecipe(uid : String, gc : Context) {

        // Check if it Exists before DB Call to Save Calls @TODO
        val fh = FileHandler()
        if (!fh.checkIfExists(uid, true, gc)) {

            val stitchAppClient = Stitch.getDefaultAppClient()

            // Call Database for Categories
            stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

                // Get Categories
                val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
                val coll = client.getDatabase("appdata").getCollection("recipes")
                val query : Document = Document().append("uid", Document().append("\$eq", uid))
                val result = coll.findOne(query)

                result.addOnSuccessListener {
                    if (result.result == null) {
                        Log.d("testData", "No document found")
                    } else if (result.isSuccessful) {
                        val r : Recipe = convertDocumenttoRecipe(result.result)
                        val fh = FileHandler()
                        fh.addCacheRecipe(r, gc)
                    }
                }

            }

        }

    }

    fun convertDocumenttoRecipe(doc: Document) : Recipe {

        // UPDATE as Need to ensure Data
        val steps = doc["steps"] as ArrayList<*>
        val cSteps = ArrayList<Section>()
        var counter = 1
        for (step in steps) {
            cSteps.add(Section(counter, "$step"))
            counter++
        }

        val ingredients = doc["ingredients"] as ArrayList<*>
        val cIngredients = ArrayList<Ingredient>()
        ingredients.forEach {
            val data = it as Document
            cIngredients.add(Ingredient(data["name"] as String, data["amount"] as String,
                data["unit"] as String, data["notes"] as String))
        }

        return Recipe(doc["isFreezable"] as String, doc["cookTime"] as String,
            doc["prepTime"] as String, doc["difficulty"] as Int, doc["spice"] as Int,
            doc["uid"] as String, doc["author"] as String, doc["courseType"] as String,
            doc["cuisine"] as String, doc["description"] as String, "",
            doc["numberOfServings"] as String, doc["source"] as String,
            doc["temperature"] as String, doc["title"] as String, ArrayList(), cIngredients,
            ArrayList(), ArrayList(), ArrayList(), ArrayList(), cSteps, "")

    }

}