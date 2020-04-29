package com.michaelwoodroof.culinaryassistant.structure

object IngredientContent {

    val ITEMS: MutableList<IngredientItem> = ArrayList()
    private val ITEM_MAP: MutableMap<String, IngredientItem> = HashMap()

    private fun addItem(item: IngredientItem) {
        ITEMS.add(item)
        ITEM_MAP[item.ingredientName] = item
    }

    private fun createRecipeItem(position: Int): IngredientItem {
        return IngredientItem(position.toString(), makeDetails(position), "", "")
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    data class IngredientItem(val amount: String, val unit: String, val ingredientName: String,
    val notes: String) {
        override fun toString(): String = ingredientName
    }

}