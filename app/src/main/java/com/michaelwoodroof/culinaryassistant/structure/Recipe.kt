package com.michaelwoodroof.culinaryassistant.structure

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.net.URI
import kotlin.collections.ArrayList

@Parcelize
class Recipe(var isFreezable: String, var cookTime: String, var prepTime: String,
             var difficulty: Int, var spice: Int, var id: String, var author: String,
             var courseType: String, var cuisine: String, var description: String,
             var imgReference: String, var numOfServings: String, var source: String,
             var temperature: String,
             var title: String, var dietary: ArrayList<Dietary>, var ingredients: ArrayList<Ingredient>,
             var nutrition: ArrayList<Nutrition>, var ingredientSection: ArrayList<ExtSection>,
             var stepsSection: ArrayList<ExtSection>, var keywords: ArrayList<String>,
             var steps: ArrayList<Section>, var uriRef : String) : Parcelable, Serializable {


    // Functions
    fun convertTemperature(givenTemperature: String): String {
        return ""
    }

    fun convertToJSON(givenRecipe: Recipe) {
        // Gives a JSON File version of a Recipe

    }

}
