package com.michaelwoodroof.culinaryassistant.overviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.michaelwoodroof.culinaryassistant.R
import kotlinx.android.synthetic.main.activity_recipe_detail.*

class RecipeDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)
        clReview.visibility = View.GONE
    }

}
