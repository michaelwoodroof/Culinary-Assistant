package com.michaelwoodroof.culinaryassistant.structure

import org.bson.types.Decimal128
import java.util.ArrayList
import java.util.HashMap

object RecipeContent {

    val ITEMS: MutableList<RecipeItem> = ArrayList()
    private val ITEM_MAP: MutableMap<String, RecipeItem> = HashMap()

    private fun addItem(item: RecipeItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    private fun createRecipeItem(position: Int): RecipeItem {
        return RecipeItem(position.toString(), "Item $position", makeDetails(position), "", 0,
            Decimal128(0), true, ArrayList<String>(), 0, MealDocument("", "", "", "", true), 0)
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    data class RecipeItem(val id: String, val title: String, val desc: String,
                          val imgString : String, val spiceLevel: Int, val reviewScore : Decimal128,
                          val hasRating : Boolean, val keyWords : ArrayList<String>, val mode : Int,
                          val mpMode : MealDocument, val diff : Int) {
        override fun toString(): String = id
    }

}
