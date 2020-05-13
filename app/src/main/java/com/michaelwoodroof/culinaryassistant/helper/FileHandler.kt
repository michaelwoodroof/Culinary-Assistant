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
            if (file.name != "mealplanner.txt") {
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

    fun createBlankMealDocument(gc : Context) {

        try {
            val fos : FileOutputStream = gc.openFileOutput("mealplanner.txt", Context.MODE_PRIVATE)
            val out : OutputStreamWriter = OutputStreamWriter(fos)
            var mealType = ""
            var notiTime = "00:00"

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
                            out.write("Monday,$mealType,$notiTime,-1,true")
                            out.write("\n\r")
                        }

                        1 -> {
                            out.write("Tuesday,$mealType,$notiTime,-1,true")
                            out.write("\n\r")
                        }

                        2 -> {
                            out.write("Wednesday,$mealType,$notiTime,-1,true")
                            out.write("\n\r")
                        }

                        3 -> {
                            out.write("Thursday,$mealType,$notiTime,-1,true")
                            out.write("\n\r")
                        }

                        4 -> {
                            out.write("Friday,$mealType,$notiTime,-1,true")
                            out.write("\n\r")
                        }

                        5 -> {
                            out.write("Saturday,$mealType,$notiTime,-1,true")
                            out.write("\n\r")
                        }

                        6 -> {
                            out.write("Sunday,$mealType,$notiTime,-1,true")
                            out.write("\n\r")
                        }

                    }
                }
            }

        } catch (e : java.lang.Exception) {}

    }

    fun addMealDocument(md : ArrayList<MealDocument>, gc : Context) {

        try {
            val fos : FileOutputStream = gc.openFileOutput("mealplanner.txt", Context.MODE_PRIVATE)
            val out : OutputStreamWriter = OutputStreamWriter(fos)
            for (doc in md) {
                val convertedDoc = doc.day + "," + doc.mealType + "," + doc.time + "," + doc.uid + doc.isNoti.toString()
                out.write(convertedDoc)
                out.write("\n\r")
            }
        } catch (e : java.lang.Exception) {}

    }

    fun updateMealDocument(md : MealDocument, gc : Context) {
        val fh = FileHandler()
        val mdd = fh.getMealDocument()
        var index = 0
        var mult = 0
        when (md.day) {
            "Monday" -> {
                mult = 0
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 1
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 2
                     }

                    "Dinner" -> {
                        index = (mult * 3) + 3
                    }

                }
            }

            "Tuesday" -> {
                mult = 1
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 1
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 2
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 3
                    }

                }
            }

            "Wednesday" -> {
                mult = 2
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 1
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 2
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 3
                    }

                }
            }

            "Thursday" -> {
                mult = 3
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 1
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 2
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 3
                    }

                }
            }

            "Friday" -> {
                mult = 4
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 1
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 2
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 3
                    }

                }
            }

            "Saturday" -> {
                mult = 5
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 1
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 2
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 3
                    }

                }
            }

            "Sunday" -> {
                mult = 6
                when (md.mealType) {

                    "Breakfast" -> {
                        index = (mult * 3) + 1
                    }

                    "Lunch" -> {
                        index = (mult * 3) + 2
                    }

                    "Dinner" -> {
                        index = (mult * 3) + 3
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
            val ins : FileInputStream = gc.openFileInput("mealplanner.txt", Context.MODE_PRIVATE)
        } catch (e : java.lang.Exception) {}



        val ins : InputStream = File("mealplanner.txt").inputStream()
        val ll = mutableListOf<String>()

        ins.bufferedReader().useLines { lines ->
            lines.forEach {
                val converted = it.split(",")
                Log.d("mdTest", converted.toString())
                md.add(MealDocument(converted[0], converted[1], converted[2], converted[3], converted[4].toBoolean()))
            }
        }

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