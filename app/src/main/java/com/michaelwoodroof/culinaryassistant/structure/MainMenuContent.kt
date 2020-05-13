package com.michaelwoodroof.culinaryassistant.structure

import org.bson.types.Decimal128

object MainMenuContent {

    val ITEMS: MutableList<MainMenuItem> = ArrayList()
    private val ITEM_MAP: MutableMap<String, MainMenuItem> = HashMap()

    private fun addItem(item : MainMenuItem) {
        ITEMS.add(item)
        ITEM_MAP[item.uid] = item
    }

    private fun createMainMenuItem(position: Int) : MainMenuItem {
        return MainMenuItem(position.toString(), makeDetails(position), "", Decimal128(0), "", true)
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    data class MainMenuItem(val title: String, val imgStr: String, val uid: String,
                              val reviewScore: Decimal128, val cuisine: String,
                            val hasReview : Boolean) {
        override fun toString(): String = title
    }

}