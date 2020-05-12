package com.michaelwoodroof.culinaryassistant

import org.bson.Document

interface RecipeCallback {

    fun getRecipe(result: MutableList<Document>)

}