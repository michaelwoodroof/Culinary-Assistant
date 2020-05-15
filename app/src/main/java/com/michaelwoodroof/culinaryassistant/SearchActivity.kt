package com.michaelwoodroof.culinaryassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.collect.Sets
import com.google.android.material.chip.Chip
import com.michaelwoodroof.culinaryassistant.adapters.MainMenuAdapter
import com.michaelwoodroof.culinaryassistant.adapters.SearchAdapter
import com.michaelwoodroof.culinaryassistant.helper.Conversions
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import com.michaelwoodroof.culinaryassistant.structure.SearchContent
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import kotlinx.android.synthetic.main.activity_search.*
import org.bson.Document
import org.bson.types.Decimal128
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

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

                            // Create Ingredient Search Array
                            var ingredients : List<String>
                            var cIngredient : MutableList<String> = mutableListOf()
                            if (clIngredient.visibility == View.VISIBLE) {
                                ingredients = txtIngredients.text.toString().toLowerCase().trim().replace("\\s".toRegex(), "").split(",")
                                cIngredient = ingredients.toMutableList()
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


                            result.forEach {
                                val r : Recipe = Conversions.convertDocumenttoRecipe(it)
                                var modtitle = r.title
                                var isValid = true
                                var lStr = ""

                                // Convert
                                // Check if Title is Viable
                                val q = ".*$uquery.*"
                                val reg = q.toRegex()

                                if (!modtitle.matches(reg)) {
                                    isValid = false
                                }

                                // Now Check Ingredients
                                if (clIngredient.visibility == View.VISIBLE) {
                                    var simpleIngredients : MutableList<String> = mutableListOf()
                                    for (i in r.ingredients) {
                                        simpleIngredients.add(i.name.toLowerCase().replace("\\s".toRegex(), ""))
                                    }

                                    simpleIngredients.sort()
                                    cIngredient.sort()

                                    if (!simpleIngredients.equals(cIngredient)) {

                                        // Check if Valid
                                        val sum = simpleIngredients.intersect(cIngredient)
                                        var givensize : Int
                                        if (simpleIngredients.size >= 9) {
                                            givensize = simpleIngredients.size - 3
                                        } else if (simpleIngredients.size >= 5) {
                                            givensize = simpleIngredients.size - 1
                                        } else {
                                            givensize = simpleIngredients.size
                                        }
                                        if (sum.size >= givensize) {
                                            isValid = false
                                        }


                                    } else {
                                        // Have Same Ingredients
                                        lStr = " Shares ${simpleIngredients.size} of ${cIngredient.size} Ingredients"
                                    }


                                }

                                val si = SearchContent.SearchItem(modtitle,
                                    r.id, lStr)

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

    fun showFilters(view : View) {

        if (view.tag == "noclick") {
            clIngredient.visibility = View.VISIBLE
            view.tag = "click"
        } else {
            clIngredient.visibility = View.GONE
            view.tag = "noclick"
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
