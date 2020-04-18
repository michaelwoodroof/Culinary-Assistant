package com.michaelwoodroof.culinaryassistant

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.michaelwoodroof.culinaryassistant.createRecipe.CreateRecipeS1
import com.michaelwoodroof.culinaryassistant.helper.*
import com.michaelwoodroof.culinaryassistant.login.AccountInfo
import com.michaelwoodroof.culinaryassistant.login.Login
import com.michaelwoodroof.culinaryassistant.overviews.Categories
import com.michaelwoodroof.culinaryassistant.overviews.RecipeOverview
import com.michaelwoodroof.culinaryassistant.structure.*
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_main_nav.*
import org.bson.Document
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var loggedIn = false
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nav)

        btnMenu.setOnClickListener{
            dlMain.openDrawer(Gravity.LEFT)
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

        val sn = ScheduleNotification()
        sn.createAlarm(this, 20, 45, (1000 * 60 * 60 * 24), "Demo")

    }

    // Used to Fill Main Menu with Info can be called with Sync Button if application is offline
    private fun loadMainScreen() {

        Handler().postDelayed(
            {
                loadSuggested()
            },
            10
        )

        Handler().postDelayed(
            {
                loadCommunityFavourites()
            },
            10
        )

        Handler().postDelayed(
            {
                loadCategories()
            },
            10
        )

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
                            0.0, prevID)

                        RenderCard.renderFiller(this, clCategories, tempID, 440, 80)
                    } else {
                        RenderCard.makeHorizontalCard(this, clCategories, it["categoryTitle"] as String,
                            it["imagePath"] as String, false, it["categoryTitle"] as String, it["categoryTitle"] as String,
                            0.0, prevID)
                    }

                    counter ++

                }
            }.addOnFailureListener {
                loadCategories()
            }

        }
    }

    private fun loadSuggested() {
        // Needed Variables
        var prevID = clSuggested.id
        val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Suggested Recipes
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            // Read from File for Suggested Recipes
            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("recipes")
            val query = coll.find().sort(Document("reviewScore", -1)).limit(10)
            val result = mutableListOf<Document>()
            var counter = 0
            var tempID : Int

            query.into(result).addOnSuccessListener {
                result.forEach {

                    prevID = if (counter == result.size - 1) {
                        tempID = RenderCard.makeHorizontalCard(this, clSuggested, it["title"] as String,
                            it["imagePath"] as String, true, it["uid"] as String, it["cuisine"] as String,
                            it["reviewScore"] as Double, prevID)

                        RenderCard.renderFiller(this, clSuggested, tempID, 440, 80)
                    } else {
                        RenderCard.makeHorizontalCard(this, clSuggested, it["title"] as String,
                            it["imagePath"] as String, true, it["uid"] as String, it["cuisine"] as String,
                            it["reviewScore"] as Double, prevID)
                    }

                    counter++

                }
            }.addOnFailureListener {
                loadSuggested()
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
                    prevID = if (counter == result.size - 1) {
                        tempID = RenderCard.makeHorizontalCard(this, clCommunity, it["title"] as String,
                            it["imagePath"] as String, true, it["uid"] as String, it["cuisine"] as String,
                            it["reviewScore"] as Double, prevID)

                        RenderCard.renderFiller(this, clCommunity, tempID, 440, 80)
                    } else {
                        RenderCard.makeHorizontalCard(this, clCommunity, it["title"] as String,
                            it["imagePath"] as String, true, it["uid"] as String, it["cuisine"] as String,
                            it["reviewScore"] as Double, prevID)
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

    fun loadSearch(view : View) {
        // Load Search View
        svMain.visibility = View.VISIBLE
        btnFilter.visibility = View.VISIBLE
        btnMenu.visibility = View.GONE
        btnFilter.visibility = View.VISIBLE
        btnSearchLoad.visibility = View.GONE
        svMain.requestFocus()
    }

    fun loadNavMenu(item : MenuItem) {
        dlMain.closeDrawer(Gravity.LEFT)

        val intent : Intent

        when (item.title) {

            "My Recipes" -> intent = Intent(this, LocalRecipes::class.java)

            "Meal Planner" -> intent = Intent(this, MealPlanner::class.java)

            "Settings" -> intent = Intent(this, LocalRecipes::class.java)

            else -> intent = Intent(this, LocalRecipes::class.java)

        }

        startActivity(intent)
    }

}
