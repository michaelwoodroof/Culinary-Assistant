package com.michaelwoodroof.culinaryassistant

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.michaelwoodroof.culinaryassistant.helper.FileHandler
import com.michaelwoodroof.culinaryassistant.helper.RenderCard
import com.michaelwoodroof.culinaryassistant.structure.*
import kotlinx.android.synthetic.main.activity_local_recipes.*

class LocalRecipes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_recipes)

        // Get Local File
        val fh = FileHandler()

        val ingredients = ArrayList<Ingredient>()
        ingredients.add(Ingredient("Carrot","200","100","ML"))
        val keywords = ArrayList<String>()
        keywords.add("Vegan")
        val steps = ArrayList<Section>()
        steps.add(Section(1, "A Step"))

        var newRecipe = Recipe("","10-20","5-10",0,0,"uid444","Michael","Other","Other",
            "A Local Recipes","","2","Local","200","Ze",ArrayList<Dietary>(),ingredients,
            ArrayList<Nutrition>(), ArrayList<ExtSection>(), ArrayList<ExtSection>(), keywords, steps,
            "")

        fh.addRecipe(newRecipe, this)

        newRecipe = Recipe("","10-20","5-10",0,0,"uid322","Michael","Other","Other",
            "A Local Recipes","","2","Local","200","Paella D",ArrayList<Dietary>(),ingredients,
            ArrayList<Nutrition>(), ArrayList<ExtSection>(), ArrayList<ExtSection>(), keywords, steps,
            "")

        fh.addRecipe(newRecipe, this)

        val localRecipes = fh.getLocalRecipes(this)

        var counter = 0
        var tempInt : Int
        var prevID = clLocalRecipes.id
//        localRecipes.forEach {
//            // Create Card for Each Recipe
//            prevID = if (counter == localRecipes.size - 1) {
//                tempInt = RenderCard.makeVerticalCard(this, clLocalRecipes)
//                RenderCard.renderFiller(this, clLocalRecipes, tempInt, 40, 10)
//
//            } else {
//                RenderCard.makeVerticalCard(this, clLocalRecipes)
//            }
//
//            counter++
//
//        }

    }


}