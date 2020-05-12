package com.michaelwoodroof.culinaryassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michaelwoodroof.culinaryassistant.structure.RecipeContent
import java.util.ArrayList

class DebugMenu : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_menu)

        val myDataset = ArrayList<RecipeContent.RecipeItem>()

        val imgTest = ""

        var ri = RecipeContent.RecipeItem("Test", "Pizza", "Time", imgTest)
        myDataset.add(0, ri)

        ri = RecipeContent.RecipeItem("Cheeky", "Yorkshire", "Time", imgTest)
        myDataset.add(1, ri)

        viewManager = LinearLayoutManager(this)
        viewAdapter = RecipeAdapter(myDataset)

        recyclerView = findViewById<RecyclerView>(R.id.rvResults).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

}
