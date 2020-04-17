package com.michaelwoodroof.culinaryassistant.helper

import android.content.Context
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class FileHandler {

    fun getLocalRecipes(givenContext: Context) : ArrayList<Recipe> {
        val localRecipes = ArrayList<Recipe>()

        val fileDir = givenContext.filesDir
        val files = fileDir.listFiles()
        files.forEach {
            val file = it
            val fis = givenContext.openFileInput(file.name)
            val ois = ObjectInputStream(fis)
            val r = ois.readObject() as Recipe
            localRecipes.add(r)
            ois.close()
            fis.close()
        }

        localRecipes.sortBy {it.title}
        return localRecipes
    }

    fun addRecipe(givenRecipe : Recipe, givenContext : Context) {
        // Check to See if File Exists Already
        if (checkIfExists(givenRecipe.id)) {
            // TELL USER @TODO
        } else {
            val fos = givenContext.openFileOutput(givenRecipe.id, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(givenRecipe)
            oos.close()
            fos.close()
        }

    }

    private fun checkIfExists(uid : String) : Boolean {

        val file = File(uid)
        return file.exists()

    }

}