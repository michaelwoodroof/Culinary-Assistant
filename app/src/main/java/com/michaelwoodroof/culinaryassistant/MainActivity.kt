package com.michaelwoodroof.culinaryassistant

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michaelwoodroof.culinaryassistant.adapters.MainMenuAdapter
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
import java.io.File
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var loggedIn = false
    private lateinit var recyclerView : RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

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
            loadMainScreen(this)
        }

    }

    private fun performSearch(query: String) {
        // Handles Search Query

    }

    fun loadSearch(view : View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    // Used to Fill Main Menu with Info can be called with Sync Button if application is offline
    private fun loadMainScreen(gc : Context) {

        var r = loadSuggested(
            object : RecipeCallback {
                override fun getRecipe(result: MutableList<Document>) {
                    val dataSet = ArrayList<MainMenuContent.MainMenuItem>()
                    val addedRecipes = ArrayList<String>()
                    val limit = 9
                    var counter = 0
                    val f = File(gc.filesDir, "suggestion")
                    val fh = FileHandler()
                    if (!f.exists()) {
                        fh.createBlankSuggestionFile(gc)
                    }
                    if (result.size <= limit + 1) { // Add Regardlessly then
                        Log.d("testData", "adding in regardless")
                        result.forEach {
                            Conversions.convertUIDtoRecipe(it["uid"] as String, gc)
                            val mmc = MainMenuContent.MainMenuItem(it["title"] as String,
                                it["imagePath"] as String, it["uid"] as String,
                                it["reviewScore"] as Decimal128, it["cuisine"] as String, true)
                            dataSet.add(mmc)
                        }
                    } else {
                        val sf : ArrayList<Suggestion> = fh.getSuggestionFile(gc)
                        sf.removeAt(0) // Remove no Tag
                        sf.sortBy { it.amount }
                        if (sf.isEmpty() || sf.size < 5) {
                            result.forEach {
                                Conversions.convertUIDtoRecipe(it["uid"] as String, gc)
                                if (counter <= limit) {
                                    val mmc = MainMenuContent.MainMenuItem(it["title"] as String,
                                        it["imagePath"] as String, it["uid"] as String,
                                        it["reviewScore"] as Decimal128, it["cuisine"] as String, true)
                                    dataSet.add(mmc)
                                }
                                counter++
                            }
                        } else {
                            var passes = 0
                            counter = 0

                            while (counter <= limit && passes < 20) {
                                passes += 1
                                // First Six are based on Top Tags of the User
                                when {
                                    counter < 6 -> result.forEach {
                                        val cwords = Conversions.convertDocumenttoRecipe(it)
                                        val words = cwords.keywords
                                        if (words.contains(sf[0].tag) || words.contains(sf[1].tag) || words.contains(sf[2].tag) && !addedRecipes.contains(it["uid"] as String)) {
                                            val mmc = MainMenuContent.MainMenuItem(it["title"] as String,
                                                it["imagePath"] as String, it["uid"] as String,
                                                it["reviewScore"] as Decimal128, it["cuisine"] as String, true)
                                            dataSet.add(mmc)
                                            addedRecipes.add(it["uid"] as String)
                                            counter++
                                        }
                                    }
                                    counter in 6..7 -> {
                                        result.forEach {
                                            val cwords = Conversions.convertDocumenttoRecipe(it)
                                            val words = cwords.keywords
                                            if (words.contains(sf[3].tag) || words.contains(sf[4].tag) && !addedRecipes.contains(it["uid"] as String)) {
                                                val mmc = MainMenuContent.MainMenuItem(it["title"] as String,
                                                    it["imagePath"] as String, it["uid"] as String,
                                                    it["reviewScore"] as Decimal128, it["cuisine"] as String, true)
                                                dataSet.add(mmc)
                                                addedRecipes.add(it["uid"] as String)
                                                counter++
                                            }
                                        }
                                    }
                                    else -> {
                                        // Wildcard Recipes
                                        result.forEach {
                                            if (!addedRecipes.contains(it["uid"] as String)) {
                                                val mmc = MainMenuContent.MainMenuItem(it["title"] as String,
                                                    it["imagePath"] as String, it["uid"] as String,
                                                    it["reviewScore"] as Decimal128, it["cuisine"] as String, true)
                                                dataSet.add(mmc)
                                                addedRecipes.add(it["uid"] as String)
                                                counter++
                                            }
                                        }
                                    }
                                }

                            }
                        }


                    }

                    viewManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
                    viewAdapter =
                        MainMenuAdapter(
                            dataSet
                        )

                    recyclerView = findViewById<RecyclerView>(R.id.rvSuggested).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }

                }
            }
        )

        r = loadCommunityFavourites(
            object : RecipeCallback {
                override fun getRecipe(result: MutableList<Document>) {
                    val dataSet = ArrayList<MainMenuContent.MainMenuItem>()
                    var counter = 0
                    val limit = 9
                    result.forEach {
                        Conversions.convertUIDtoRecipe(it["uid"] as String, baseContext)
                        if (counter <= limit) {
                            val mmc = MainMenuContent.MainMenuItem(it["title"] as String,
                                it["imagePath"] as String, it["uid"] as String,
                                it["reviewScore"] as Decimal128, it["cuisine"] as String, true)
                            dataSet.add(mmc)
                        }
                        counter++
                    }

                    viewManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
                    viewAdapter =
                        MainMenuAdapter(
                            dataSet
                        )

                    recyclerView = findViewById<RecyclerView>(R.id.rvCommunityFavourites).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }

                }
            }
        )

        r = loadCategories(
            object : RecipeCallback {
                override fun getRecipe(result: MutableList<Document>) {
                    val dataSet = ArrayList<MainMenuContent.MainMenuItem>()
                    var limit = 9
                    if (result.size < 10) {
                        limit = result.size
                    }
                    var counter = 0
                    result.forEach {

                        // Modify
                        val isMet : Boolean = true

                        if (counter != limit && isMet) {
                            val mmc = MainMenuContent.MainMenuItem(it["categoryTitle"] as String,
                                it["imagePath"] as String, it["categoryTitle"] as String,
                                Decimal128(0), it["categoryTitle"] as String, false)
                            dataSet.add(mmc)
                        }


                        counter++
                    }

                    viewManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
                    viewAdapter =
                        MainMenuAdapter(
                            dataSet
                        )

                    recyclerView = findViewById<RecyclerView>(R.id.rvCategories).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }

                }
            }
        )

    }

    private fun loadCategories(rc : RecipeCallback) {
        val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Categories

        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            // Get Categories
            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("categories")
            val query = coll.find().sort(Document("categoryTitle", 1)).limit(10)
            val result = mutableListOf<Document>()

            query.into(result).addOnSuccessListener {
                rc.getRecipe(result)
            }.addOnFailureListener {
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
            val query = coll.find().sort(Document("reviewScore", -1))
            val result = mutableListOf<Document>()

            query.into(result).addOnSuccessListener {
                rc.getRecipe(result)
            }.addOnFailureListener {
                // @TODO
            }

        }

    }

    private fun loadCommunityFavourites(rc : RecipeCallback) {

        val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Community Favourites
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("recipes")
            val query = coll.find().sort(Document("reviewScore", -1))
            val result = mutableListOf<Document>()

            query.into(result).addOnSuccessListener {
                rc.getRecipe(result)
            }.addOnFailureListener {
                // @TODO
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

//            "Debug" -> intent = Intent(this, DebugMenu::class.java)


            else -> intent = Intent(this, LocalRecipes::class.java)

        }

        startActivity(intent)
    }

}
