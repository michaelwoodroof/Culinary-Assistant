package com.michaelwoodroof.culinaryassistant.helper

import android.content.Context
import android.util.Log
import com.michaelwoodroof.culinaryassistant.structure.*
import java.io.File
import java.io.FileOutputStream
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

    fun getRecipe(givenContext: Context, givenUID : String, isCache: Boolean) : Recipe {

        val cf : File
        if (isCache) {
            cf = File(givenContext.cacheDir, givenUID)
        } else {
            cf = File(givenContext.filesDir, givenUID)
        }

        val ingredients = ArrayList<Ingredient>()
        ingredients.add(Ingredient("Carrot","200","100","ML"))
        val keywords = ArrayList<String>()
        keywords.add("Vegan")
        val steps = ArrayList<Section>()
        steps.add(Section(1, "A Step"))

        if (cf.exists()) {
            val file : File
            if (isCache) {
                file = File(givenContext.cacheDir, givenUID)
            } else {
                file = File(givenContext.filesDir, givenUID)
            } // FIX WITH FOR CACHE
            return ObjectInputStream(givenContext.openFileInput(file.name)).readObject() as Recipe
        } else {
            // UPDATE
            return Recipe("","10-20","5-10",0,0,"uid322","Michael","Other","Other",
                "A Local Recipes","","2","Local","200","Fail",ArrayList<Dietary>(),ingredients,
                ArrayList<Nutrition>(), ArrayList<ExtSection>(), ArrayList<ExtSection>(), keywords, steps,
                "")
        }

    }

    fun addRecipe(givenRecipe : Recipe, givenContext : Context) {
        // Check to See if File Exists Already
        if (checkIfExists(givenRecipe.id, false, givenContext)) {
            // TELL USER @TODO
            Log.d("testData", "File already Exists")
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
            Log.d("testData", "exists already")
        } else {
            Log.d("testData", "createdRecipe")
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