package com.michaelwoodroof.culinaryassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.michaelwoodroof.culinaryassistant.adapters.MainMenuAdapter
import com.michaelwoodroof.culinaryassistant.adapters.SearchAdapter
import com.michaelwoodroof.culinaryassistant.structure.SearchContent
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_search.*
import org.bson.Document

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sv : SearchView = svSearch
        sv.queryHint = "Search for Recipes"

        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(uquery: String): Boolean {

//                fillRecipes(uquery)

                return true
            }

            override fun onQueryTextSubmit(uquery: String): Boolean {
                // Perform Search

                val r = getRecipes(
                    object : RecipeCallback {
                        override fun getRecipe(result: MutableList<Document>) {
                            val dataSet = ArrayList<SearchContent.SearchItem>()
                            result.forEach {
                                val title = it["title"] as String
                                var isValid = true
                                val si = SearchContent.SearchItem(title,
                                    it["uid"] as String)

                                // Check if Title is Viable
                                val q = """.*""" + uquery + """.*"""
                                val reg = q.toRegex()

                                if (!title.matches(reg)) {
                                    isValid = false
                                }

                                if (isValid) {
                                    dataSet.add(si)
                                }

                            }
                            viewManager = LinearLayoutManager(baseContext)
                            viewAdapter =
                                SearchAdapter(
                                    dataSet
                                )

                            recyclerView = findViewById<RecyclerView>(R.id.rvSearchResults).apply {
                                setHasFixedSize(true)
                                layoutManager = viewManager
                                adapter = viewAdapter
                            }
                        }
                    }
                )

                return true
            }

        })

    }

    fun showFilters(view : View) { //@TODO EXPAND TO DO 1 SUB GOAL 1

        if (cgFilters.visibility == View.VISIBLE) {
            cgFilters.visibility == View.GONE
        } else {
            cgFilters.visibility == View.VISIBLE
        }


    }

    fun getRecipes(rc : RecipeCallback) {

        val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Suggested Recipes
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("recipes")
            val query = coll.find().sort(Document("reviewScore", 1))
            val result = mutableListOf<Document>()

            query.into(result).addOnSuccessListener {
                rc.getRecipe(result)
            }
        }
    }

}
