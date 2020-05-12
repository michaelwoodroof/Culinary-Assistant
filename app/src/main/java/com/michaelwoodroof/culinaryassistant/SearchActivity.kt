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

                fillRecipes(uquery)

                Thread.sleep(100)

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

    fun fillRecipes(uquery : String) {
        Log.d("searchTest", uquery)

        val stitchAppClient = Stitch.getDefaultAppClient()

        // Call Database for Suggested Recipes
        stitchAppClient.auth.loginWithCredential(AnonymousCredential()).addOnSuccessListener {

            val client = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
            val coll = client.getDatabase("appdata").getCollection("recipes")
            val query = coll.find().sort(Document("reviewScore", 1))
            val result = mutableListOf<Document>()
            val dataSet = ArrayList<SearchContent.SearchItem>()
            var count = 0

            query.into(result).addOnSuccessListener {
                result.forEach {
//                    val squery = uquery.toLowerCase()
                    val pattern = """.*""" + uquery + """.*"""
                    val reg : Regex = pattern.toRegex()

                    val title = it["title"] as String
//                    title.toLowerCase()
                    val found : Boolean = title.matches(reg)

                    // Next Check Filters
                    for (child in cgFilters.children) {
                        val chip : Chip = child as Chip
                        if (chip.isChecked) {
                            // Check each Filter

                        }
                    }

                    if (found) {
                        val sc = SearchContent.SearchItem(it["title"] as String, it["uid"] as String)
                        dataSet.add(sc)
                    }

                    if (count == result.size - 1) {
                        // Set - up Content
                        viewManager = LinearLayoutManager(this)
                        viewAdapter = SearchAdapter(dataSet)

                        recyclerView = findViewById<RecyclerView>(R.id.rvSearchResults).apply {
                            setHasFixedSize(true)
                            layoutManager = viewManager
                            adapter = viewAdapter
                        }
                    }

                    Log.d("searchTest", title)
                    Log.d("searchTest", found.toString())

                    count++

                }
            }
        }
    }

}
