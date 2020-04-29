package com.michaelwoodroof.culinaryassistant.helper

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.tasks.Task
import com.michaelwoodroof.culinaryassistant.overviews.RecipeDetail
import com.michaelwoodroof.culinaryassistant.structure.*
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.core.auth.StitchUser
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import org.bson.Document
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

object Conversions {

    fun convertUIDtoRecipe(uid : String, gc : Context) {

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

    fun convertDocumenttoRecipe(doc: Document) : Recipe {

        val r : Recipe = Recipe(doc["isFreezable"] as String, doc["cookTime"] as String,
            doc["prepTime"] as String, doc["difficulty"] as Int, doc["spice"] as Int,
            doc["uid"] as String, doc["author"] as String, doc["courseType"] as String,
            doc["cuisine"] as String, doc["description"] as String, "",
            doc["numberOfServings"] as String, doc["source"] as String,
            doc["temperature"] as String, doc["title"] as String, ArrayList<Dietary>(),
            ArrayList<Ingredient>(), ArrayList<Nutrition>(), ArrayList<ExtSection>(),
            ArrayList<ExtSection>(), ArrayList<String>(), ArrayList<Section>(), "")

        return r


    }

}