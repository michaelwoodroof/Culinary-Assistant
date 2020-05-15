package com.michaelwoodroof.culinaryassistant.helper

import android.content.Context
import android.util.Log
import com.michaelwoodroof.culinaryassistant.structure.*
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import org.bson.Document
import java.lang.Exception
import kotlin.collections.ArrayList

object Conversions {

    fun convertUIDtoRecipe(uid : String, gc : Context) {

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
        steps.forEach {
            val step = it as Document
            var stepNo : Int = 0
            try {
                stepNo = step["stepNumber"] as Int
            } catch (e : Exception) {
                try {
                    val strNo = step["stepNumber"] as String
                    stepNo = strNo.toInt()
                } catch (e : Exception) {

                }
            }

            cSteps.add(Section(stepNo, step["description"] as String))
        }

        val ingredients = doc["ingredients"] as ArrayList<*>
        val cIngredients = ArrayList<Ingredient>()
        ingredients.forEach {
            val data = it as Document
            cIngredients.add(Ingredient(data["name"] as String, data["amount"] as String,
                data["unit"] as String, data["notes"] as String))
        }

        val cDietary = ArrayList<Dietary>()
        try {
            val dietary = doc["dietary"] as ArrayList<*>

            dietary.forEach {
                val data = it as Document
                cDietary.add(Dietary(data["name"] as String))
            }
        } catch (e : Exception) {
            cDietary.add(Dietary("no allergen"))
        }


        val keywords = doc["keyWords"] as ArrayList<*>
        val cWords = ArrayList<String>()
        keywords.forEach {
            cWords.add(it as String)
        }


        return Recipe(doc["isFreezable"] as String, doc["cookTime"] as String,
            doc["prepTime"] as String, doc["difficulty"] as Int, doc["spice"] as Int,
            doc["uid"] as String, doc["author"] as String, doc["courseType"] as String,
            doc["cuisine"] as String, doc["description"] as String, "",
            doc["numberOfServings"] as String, doc["source"] as String,
            doc["temperature"] as String, doc["title"] as String, cDietary, cIngredients,
            ArrayList(), ArrayList(), ArrayList(), cWords, cSteps, "")

    }

}