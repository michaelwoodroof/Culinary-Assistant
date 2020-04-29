package com.michaelwoodroof.culinaryassistant.structure

object StepContent {

    val ITEMS: MutableList<StepItem> = ArrayList()
    private val ITEM_MAP: MutableMap<String, StepItem> = HashMap()

    private fun addItem(item: StepItem) {
        ITEMS.add(item)
        ITEM_MAP[item.stepNumber] = item
    }

    private fun createRecipeItem(position: Int): StepItem {
        return StepItem(position.toString(), makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    data class StepItem(val stepNumber: String, val description : String) {
        override fun toString(): String = description
    }

}
