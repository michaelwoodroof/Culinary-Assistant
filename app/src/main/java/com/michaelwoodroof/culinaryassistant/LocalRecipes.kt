package com.michaelwoodroof.culinaryassistant

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michaelwoodroof.culinaryassistant.adapters.RecipeAdapter
import com.michaelwoodroof.culinaryassistant.helper.FileHandler
import com.michaelwoodroof.culinaryassistant.helper.RenderCard
import com.michaelwoodroof.culinaryassistant.structure.*
import kotlinx.android.synthetic.main.activity_local_recipes.*
import org.bson.types.Decimal128

class LocalRecipes : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_recipes)

        // Get Local File
        val fh = FileHandler()

        val localRecipes = fh.getLocalRecipes(this)
        val dataSet = ArrayList<RecipeContent.RecipeItem>()

        localRecipes.forEach {

            val bundle : Bundle? = intent.extras
            if (bundle != null) {
                if (bundle.containsKey("mpMode")) {
                    val ri = RecipeContent.RecipeItem(it.id, it.title, it.description, it.imgReference,
                        it.spice, Decimal128(0), false, it.keywords, 2, intent.getParcelableExtra("mpMode"), it.difficulty)
                    dataSet.add(ri)
                }
            } else {
                val ri = RecipeContent.RecipeItem(it.id, it.title, it.description, it.imgReference,
                    it.spice, Decimal128(0), false, it.keywords, 0, MealDocument("", "", "", "", false), it.difficulty)
                dataSet.add(ri)
            }



            viewManager = LinearLayoutManager(this)
            viewAdapter =
                RecipeAdapter(
                    dataSet
                )

            recyclerView = findViewById<RecyclerView>(R.id.rvLRecipes).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }

        }

    }

}