package com.michaelwoodroof.culinaryassistant.helper

import android.content.Context
import android.util.Log
import com.michaelwoodroof.culinaryassistant.structure.*
import org.w3c.dom.Document
import java.io.*

class FileHandler {

    fun getLocalRecipes(givenContext: Context, isCache: Boolean) : ArrayList<Recipe> {
        val localRecipes = ArrayList<Recipe>()

        var fileDir = givenContext.filesDir

        if (isCache) {
            fileDir = givenContext.cacheDir
        }

        val files = fileDir.listFiles()
        files.forEach {
            val file = it
            if (file.name != "mealplanner" && file.name != "suggestion") {
                try {

                    val fis = FileInputStream(file)
                    val ro = ObjectInputStream(fis).readObject() as Recipe
                    localRecipes.add(ro)
                    fis.close()

                } catch (e: Exception) {

                }
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

        } else {
            val fos = givenContext.openFileOutput(givenRecipe.id, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(givenRecipe)
            oos.close()
            fos.close()
        }

    }

    fun createBlankSuggestionFile(gc : Context) {

        try {
            val fos : FileOutputStream = gc.openFileOutput("suggestion", Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)

            val armd : ArrayList<Suggestion> = ArrayList<Suggestion>()
            armd.add(Suggestion("notag", 0))

            oos.writeObject(armd)
            oos.close()
            fos.close()

        } catch (e : java.lang.Exception) {}

    }

    fun getSuggestionFile(gc : Context) : ArrayList<Suggestion> {

        val sf = ArrayList<Suggestion>()
        try {

            val file = File(gc.filesDir, "suggestion")
            val fis = FileInputStream(file)
            val ois = ObjectInputStream(fis)

            val armd = ois.readObject() as ArrayList<*>

            for (doc in armd) {
                sf.add(doc as Suggestion)
            }

            // Puts it to Ascending
            sf.sortBy {it.amount}
            sf.reverse()

        } catch (e : java.lang.Exception) {}

        return sf

    }

    fun updateSuggestionFile(sf : Suggestion, gc : Context) {

        val fh = FileHandler()
        val sff = fh.getSuggestionFile(gc)

        var counter = 0
        var isFound = false
        for (tag in sff) {
            if (tag.tag == sf.tag) {
                isFound = true
                break
            }
            counter++
        }

        if (!isFound) {
            sff.add(Suggestion(sf.tag, 1))
        } else {
            val mod = sff[counter]
            mod.amount += 1
            sff[counter] = mod
        }

        fh.addSuggestionFile(sff, gc)

    }

    fun addSuggestionFile(sff : ArrayList<Suggestion>, gc : Context) {

        try {
            val f = File(gc.filesDir, "suggeston")
            if (f.exists()) {
                f.delete()
            }
            val fos : FileOutputStream = gc.openFileOutput("suggestion", Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            val armd = ArrayList<Suggestion>()
            for (doc in sff) {
                armd.add(doc)
            }
            oos.writeObject(armd)
        } catch(e : java.lang.Exception) {}

    }

    fun createBlankMealDocument(gc : Context) {

        try {
            val fos : FileOutputStream = gc.openFileOutput("mealplanner", Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            var mealType = ""
            var notiTime = "00:00"

            val armd : ArrayList<MealDocument> = ArrayList<MealDocument>()
            for (x in 0..6) {
                for (y in 0..2) {

                    when (y) {

                        0 -> {
                            mealType = "Breakfast"
                            notiTime = "09:00"
                        }

                        1 -> {
                            mealType = "Lunch"
                            notiTime = "12:00"
                        }

                        2 -> {
                            mealType = "Dinner"
                            notiTime = "18:00"
                        }

                    }

                    when (x) {

                        0 -> {
                            val md = MealDocument("Monday",mealType,notiTime,"-1",true)
                            armd.add(md)
                        }

                        1 -> {
                            val md = MealDocument("Tuesday",mealType,notiTime,"-1",true)
                            armd.add(md)
                        }

                        2 -> {
                            val md = MealDocument("Wednesday",mealType,notiTime,"-1",true)
                            armd.add(md)
                        }

                        3 -> {
                            val md = MealDocument("Thursday",mealType,notiTime,"-1",true)
                            armd.add(md)
                        }

                        4 -> {
                            val md = MealDocument("Friday",mealType,notiTime,"-1",true)
                            armd.add(md)
                        }

                        5 -> {
                            val md = MealDocument("Saturday",mealType,notiTime,"-1",true)
                            armd.add(md)
                        }

                        6 -> {
                            val md = MealDocument("Sunday",mealType,notiTime,"-1",true)
                            armd.add(md)
                        }

                    }
                }
            }

            oos.writeObject(armd)

            oos.close()
            fos.close()

        } catch (e : java.lang.Exception) {}


    }

    fun addMealDocument(md : ArrayList<MealDocument>, gc : Context) {

        try {
            val f = File(gc.filesDir, "mealplanner")

            if (f.exists()) {
                f.delete()
            }
            val fos : FileOutputStream = gc.openFileOutput("mealplanner", Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            val armd = ArrayList<MealDocument>()
            for (doc in md) {
                armd.add(doc)
            }
            oos.writeObject(armd)
        } catch (e : java.lang.Exception) {}

    }

    fun updateMealDocument(md : MealDocument, gc : Context) {
        val fh = FileHandler()
        val mdd = fh.getMealDocument(gc)
        var index = 0
        var mult = 0
        when (md.day) {
            "Monday" -> {
                mult = 0
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 0
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 1
                     }

                    "Dinner" -> {
                        index = (mult * 3) + 2
                    }

                }
            }

            "Tuesday" -> {
                mult = 1
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 0
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 1
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 2
                    }

                }
            }

            "Wednesday" -> {
                mult = 2
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 0
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 1
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 2
                    }

                }
            }

            "Thursday" -> {
                mult = 3
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 0
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 1
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 2
                    }

                }
            }

            "Friday" -> {
                mult = 4
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 0
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 1
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 2
                    }

                }
            }

            "Saturday" -> {
                mult = 5
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 0
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 1
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 2
                    }

                }
            }

            "Sunday" -> {
                mult = 6
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 0
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 1
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 2
                    }

                }
            }
        }
        mdd[index] = md
        fh.addMealDocument(mdd, gc)
    }

    fun getMealDocument(gc : Context) : ArrayList<MealDocument> {

        val md = ArrayList<MealDocument>()
        try {

            val file = File(gc.filesDir, "mealplanner")
            val fis = FileInputStream(file)
            val ois = ObjectInputStream(fis)

            val armd = ois.readObject() as ArrayList<*>

            for (doc in armd) {
                md.add(doc as MealDocument)
            }

        } catch (e : java.lang.Exception) {}

        return md

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