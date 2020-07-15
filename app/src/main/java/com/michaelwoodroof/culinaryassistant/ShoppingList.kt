package com.michaelwoodroof.culinaryassistant

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michaelwoodroof.culinaryassistant.adapters.IngredientAdapter
import com.michaelwoodroof.culinaryassistant.helper.FileHandler
import com.michaelwoodroof.culinaryassistant.structure.IngredientContent
import com.michaelwoodroof.culinaryassistant.structure.MealDocument
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import java.lang.Exception

class ShoppingList : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        val fh = FileHandler()

        val f : ArrayList<MealDocument> = fh.getMealDocument(this)

        val dataSet = mutableMapOf<String, IngredientContent.IngredientItem>()

        f.forEach {

            // Get File relating to UID
            Log.d("testData", it.uid)
            val ff : Recipe? = fh.getRecipe(this, it.uid, false)

            // Loop through Ingredients
            ff?.ingredients?.forEach { ig ->
                // Set Map
                if (dataSet.containsKey(ig.name)) {
                    // Update Key
                    var ii = dataSet[ig.name]
                    var amount = 0
                    var orignalamount = 0

                    try {
                        amount = ig.amount.toIntOrNull()!!
                        orignalamount = ii!!.amount.toIntOrNull()!!
                    } catch (e : Exception) {

                    }

                    ii = IngredientContent.IngredientItem((orignalamount + amount).toString(), ig.unit,
                    ig.name, "")

                    dataSet[ig.name] = ii

                } else {
                    // Add Key
                    dataSet[ig.name] = IngredientContent.IngredientItem(ig.amount, ig.unit, ig.name, "")
                }
            }

            var convertedDataSet = ArrayList<IngredientContent.IngredientItem>()
            dataSet.forEach { (_, ingredientItem) ->
                convertedDataSet.add(ingredientItem)
            }

            convertedDataSet.sortBy { igp -> igp.ingredientName }

            viewManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            viewAdapter =
                IngredientAdapter(
                    convertedDataSet
                )

            recyclerView = findViewById<RecyclerView>(R.id.rvShoppingList).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }

        }

    }
}
