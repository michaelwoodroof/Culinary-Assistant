package com.michaelwoodroof.culinaryassistant.helper

import android.content.Context
import android.util.Log
import com.michaelwoodroof.culinaryassistant.structure.*
import java.io.*

class FileHandler {

    fun getLocalRecipes(givenContext: Context) : ArrayList<Recipe> {
        val localRecipes = ArrayList<Recipe>()

        val fileDir = givenContext.filesDir
        val files = fileDir.listFiles()
        files.forEach {
            val file = it
            val fis = givenContext.openFileInput(file.name)
            try {
                val ois = ObjectInputStream(fis)
                val r = ois.readObject() as Recipe
                localRecipes.add(r)
                ois.close()
                fis.close()
            } catch (e: Exception) {
                Log.d("testData", e.toString())
            }

        }

        localRecipes.sortBy {it.title}
        return localRecipes
    }

    fun getRecipe(givenContext: Context, givenUID : String, isCache: Boolean) : Recipe? {

        val cf : File
        if (isCache) {
            cf = File(givenContext.cacheDir, givenUID)
        } else {
            cf = File(givenContext.filesDir, givenUID)
        }

        if (cf.exists()) {
            val file : File
            val ro : Recipe
            val fis : FileInputStream
            // Ensures File is Closed
            if (isCache) {
                file = File(givenContext.cacheDir, givenUID)
                fis = FileInputStream(file)
                ro = ObjectInputStream(fis).readObject() as Recipe
                fis.close()
            } else {
                file = File(givenContext.filesDir, givenUID)
                fis = FileInputStream(file)
                ro = ObjectInputStream(fis).readObject() as Recipe
                fis.close()
            }
            return ro
        } else {
            return null
        }

    }

    fun addRecipe(givenRecipe : Recipe, givenContext : Context) {
        // Check to See if File Exists Already
        if (checkIfExists(givenRecipe.id, false, givenContext)) {
            // TELL USER @TODO
        } else {
            val fos = givenContext.openFileOutput(givenRecipe.id, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(givenRecipe)
            oos.close()
            fos.close()
        }

    }

    fun addCacheRecipe(givenRecipe: Recipe, gc: Context) {

        if (checkIfExists(givenRecipe.id, true, gc)) {

        } else {

            val cf = File(gc.cacheDir, givenRecipe.id)
            cf.createNewFile()
            val fos = FileOutputStream(cf)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(givenRecipe)
            oos.close()
            fos.close()
        }

    }

    fun checkIfExists(uid : String, isCache : Boolean, gc : Context) : Boolean {

        val file : File = if (isCache) {
            File(gc.cacheDir, uid)
        } else {
            File(uid)
        }

        return file.exists()

    }

}