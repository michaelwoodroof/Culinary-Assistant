package com.michaelwoodroof.culinaryassistant.repiceFormat

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object RecipeContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<RecipeItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    private val ITEM_MAP: MutableMap<String, RecipeItem> = HashMap()

    private fun addItem(item: RecipeItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    private fun createRecipeItem(position: Int): RecipeItem {
        return RecipeItem(position.toString(), "Item $position", makeDetails(position), "")
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    data class RecipeItem(val id: String, val content: String, val details: String, val imgString : String) {
        override fun toString(): String = content
    }

}
