package com.michaelwoodroof.culinaryassistant

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.michaelwoodroof.culinaryassistant.createRecipe.CreateRecipeS1
import com.michaelwoodroof.culinaryassistant.helper.*
import com.michaelwoodroof.culinaryassistant.login.AccountInfo
import com.michaelwoodroof.culinaryassistant.login.Login
import com.michaelwoodroof.culinaryassistant.mealPlanner.MealPlanner
import com.michaelwoodroof.culinaryassistant.overviews.Categories
import com.michaelwoodroof.culinaryassistant.overviews.RecipeOverview
import com.michaelwoodroof.culinaryassistant.structure.*
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_main_nav.*
import org.bson.Document
import org.bson.types.Decimal128
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var loggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nav)

        btnMenu.setOnClickListener{
            dlMain.openDrawer(Gravity.LEFT)
        }

        // Initialise Search
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also {
                query -> performSearch(query)
            }
        }

        // Attempts Database Connection
        if (!true) { // UPDATE
            // Load Offline Mode // UPDATE
            println("OFFLINE")
        } else {
            // Check User Login Status
            //
            // METHOD HERE
            //
            var fileName = "token.txt"
            loggedIn = true
            loadMainScreen()
        }

        // Ensure that App has Notification Channel
        NotificationHandler.createNotificationChannel(this)

//        Log.d("testData", "startConvert")
//        Conversions.convertUIDtoRecipe("uid00000001")

        val sn = ScheduleNotification()
        sn.createAlarm(this, 21, 41, (1000 * 60 * 60 * 24), "Demo")

        val sn1 = ScheduleNotification()
        sn1.createAlarm(this, 21, 42, (1000 * 60 * 60 * 24), "Demo2")


        // Demo for Changing Temperatures
        val er = Recipe(
            isFreezable = "NO", cookTime = "", prepTime = "",
            difficulty = -1,
            spice = 0,
            id = "",
            author = "",
            courseType = "",
            cuisine = "",
            description = "",
            imgReference = "",
            numOfServings = "",
            source = "",
            temperature = "200CC",
            title = "",
            dietary = ArrayList<Dietary>(),
            ingredients = ArrayList<Ingredient>(),
            nutrition = ArrayList<Nutrition>(),
            ingredientSection = ArrayList<ExtSection>(),
            stepsSection = ArrayList<ExtSection>(),
            keywords = ArrayList<String>(),
            steps = ArrayList<Section>(),
            uriRef = ""
        )

        //val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Community Favouritesl
//        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {
//
//            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
//            val coll = client.getDatabase("appdata").getCollection("recipes")
//
//            val str = """.*""" + Pattern.quote("Med") + """.*"""
//            val doc = Document().append("\$regex", str.toRegex())
//            doc.append("\$options", "i")
//            val docQuery = Document().append("title", doc)
//            Log.d("searchTest", docQuery.toString())
//
//            val query = coll.find(docQuery).limit(10)
//            val result = mutableListOf<Document>()
//
//            query.into(result).addOnSuccessListener {
//
//                result.forEach {
//                    Log.d("searchTest", it["title"] as String)
//                }
//
//            }
//        }


        val fah = er.convertTemperature("FF")
        Log.d("convertData", fah)

        val gm = er.convertTemperature("GM")
        Log.d("convertData", gm)

        val fo = er.convertTemperature("FO")
        Log.d("convertData", fo)

    }

    private fun performSearch(query: String) {
        // Handles Search Query
        Log.d("testData", query)
    }

    fun loadSearch(view : View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    // Used to Fill Main Menu with Info can be called with Sync Button if application is offline
    private fun loadMainScreen() {

        val r = loadSuggested(
            object : RecipeCallback {
                override fun getRecipe(result: MutableList<Document>) {
                    var prevID = clSuggested.id
                    var counter = 0
                    var tempID : Int
                    result.forEach {
                        Conversions.convertUIDtoRecipe(it["uid"] as String, baseContext)
                        val data = it["uid"] as String
                        Log.d("cacheData", "Created file: $data")

                            prevID = if (counter == result.size - 1) {
                                tempID = RenderCard.makeHorizontalCard(baseContext, clSuggested, it["title"] as String,
                                    it["imagePath"] as String, true, it["uid"] as String, it["cuisine"] as String,
                                    it["reviewScore"] as Decimal128, prevID)

                                RenderCard.renderFiller(baseContext, clSuggested, tempID, 440, 80)
                            } else {
                                RenderCard.makeHorizontalCard(baseContext, clSuggested, it["title"] as String,
                                    it["imagePath"] as String, true, it["uid"] as String, it["cuisine"] as String,
                                    it["reviewScore"] as Decimal128, prevID)
                            }

                        counter++
                    }
                }
            }
        )

        loadCommunityFavourites()

        loadCategories()

    }

    private fun loadCategories() {
        // Needed Variables
        var prevID = clCategories.id
        val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Categories

        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            // Get Categories
            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("categories")
            val query = coll.find().sort(Document("categoryTitle", 1)).limit(10)
            val result = mutableListOf<Document>()
            var counter = 0
            var tempID: Int



            query.into(result).addOnSuccessListener {
                result.forEach {
                    prevID = if (counter == result.size - 1) {
                        tempID = RenderCard.makeHorizontalCard(this, clCategories, it["categoryTitle"] as String,
                            it["imagePath"] as String, false, it["categoryTitle"] as String, it["categoryTitle"] as String,
                            Decimal128(0), prevID)
                        RenderCard.renderFiller(this, clCategories, tempID, 440, 80)
                    } else {
                        RenderCard.makeHorizontalCard(this, clCategories, it["categoryTitle"] as String,
                            it["imagePath"] as String, false, it["categoryTitle"] as String, it["categoryTitle"] as String,
                            Decimal128(0), prevID)
                    }

                    counter ++

                }
            }.addOnFailureListener {
                loadCategories()
            }

        }

    }

    private fun loadSuggested(rc : RecipeCallback) {

        val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Suggested Recipes
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            // Read from File for Suggested Recipes
            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("recipes")
            val query = coll.find().sort(Document("reviewScore", 1)).limit(10)
            val result = mutableListOf<Document>()

            query.into(result).addOnSuccessListener {
                rc.getRecipe(result)
            }.addOnFailureListener {
                // @TODO
            }

        }

    }

    private fun loadCommunityFavourites() {
        // Needed Variables
        var prevID = clCommunity.id
        val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Community Favourites
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("recipes")
            val query = coll.find().sort(Document("reviewScore", -1)).limit(10)
            val result = mutableListOf<Document>()
            var counter = 0
            var tempID : Int

            query.into(result).addOnSuccessListener {

                result.forEach {

                    Conversions.convertUIDtoRecipe(it["uid"] as String, baseContext)

                    prevID = if (counter == result.size - 1) {
                        tempID = RenderCard.makeHorizontalCard(this, clCommunity, it["title"] as String,
                            it["imagePath"] as String, true, it["uid"] as String, it["cuisine"] as String,
                            it["reviewScore"] as Decimal128, prevID)

                        RenderCard.renderFiller(this, clCommunity, tempID, 440, 80)
                    } else {
                        RenderCard.makeHorizontalCard(this, clCommunity, it["title"] as String,
                            it["imagePath"] as String, true, it["uid"] as String, it["cuisine"] as String,
                            it["reviewScore"] as Decimal128, prevID)
                    }

                    counter++

                }
            }.addOnFailureListener {
                loadCommunityFavourites()
            }

        }

    }

    fun openCreateMenu(view: View) {
        val er = Recipe(
            isFreezable = "NO", cookTime = "", prepTime = "",
            difficulty = -1,
            spice = 0,
            id = "",
            author = "",
            courseType = "",
            cuisine = "",
            description = "",
            imgReference = "",
            numOfServings = "",
            source = "",
            temperature = "",
            title = "",
            dietary = ArrayList<Dietary>(),
            ingredients = ArrayList<Ingredient>(),
            nutrition = ArrayList<Nutrition>(),
            ingredientSection = ArrayList<ExtSection>(),
            stepsSection = ArrayList<ExtSection>(),
            keywords = ArrayList<String>(),
            steps = ArrayList<Section>(),
            uriRef = ""
        )
        val intent = Intent(this, CreateRecipeS1::class.java)
        intent.putExtra("partialrecipe", er as Parcelable)
        startActivity(intent)
    }

    fun loadAllSuggested(view: View) {
        // Expands Scroll into Full Menu
        val intent = Intent(this, RecipeOverview::class.java)
        intent.putExtra("type", "suggested")
        startActivity(intent)
    }

    fun loadAllCommunityFavourites(view: View) {
        // Expands Scroll into Full Menu
        val intent = Intent(this, RecipeOverview::class.java)
        intent.putExtra("type", "community")
        startActivity(intent)
    }

    fun loadAllCategories(view: View) {
        // Expands Scroll into Full Menu
        val intent = Intent(this, Categories::class.java)
        startActivity(intent)
    }

    fun loadAccount(view: View) {
        // Check if Already logged in
        if (loggedIn) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, AccountInfo::class.java)
            startActivity(intent)
        }
    }

    fun loadNavMenu(item : MenuItem) {
        dlMain.closeDrawer(Gravity.LEFT)

        val intent : Intent

        when (item.title) {

            "My Recipes" -> intent = Intent(this, LocalRecipes::class.java)

            "Meal Planner" -> intent = Intent(this, MealPlanner::class.java)

            "Settings" -> intent = Intent(this, LocalRecipes::class.java)

            "Shopping List" -> intent = Intent(this, ShoppingList::class.java)

            "Debug" -> intent = Intent(this, DebugMenu::class.java)


            else -> intent = Intent(this, LocalRecipes::class.java)

        }

        startActivity(intent)
    }

}
