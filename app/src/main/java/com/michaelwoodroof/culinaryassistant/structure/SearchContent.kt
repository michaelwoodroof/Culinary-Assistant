package com.michaelwoodroof.culinaryassistant.structure

object SearchContent {

    val ITEMS: MutableList<SearchItem> = ArrayList()
    private val ITEM_MAP: MutableMap<String, SearchItem> = HashMap()

    private fun addItem(item: SearchItem) {
        ITEMS.add(item)
        ITEM_MAP[item.uid] = item
    }

    private fun createRecipeItem(position: Int): SearchItem {
        return SearchItem(position.toString(), makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    data class SearchItem(val title: String, val uid: String) {
        override fun toString(): String = uid
    }

}