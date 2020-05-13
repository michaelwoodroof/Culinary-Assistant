package com.michaelwoodroof.culinaryassistant.adapters

import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.FileHandler
import com.michaelwoodroof.culinaryassistant.overviews.RecipeDetail
import com.michaelwoodroof.culinaryassistant.structure.SearchContent
import kotlinx.android.synthetic.main.recipe_layout.view.*
import kotlinx.android.synthetic.main.search_layout.view.*

class SearchAdapter(private val mValues : List<SearchContent.SearchItem>
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int  = mValues.size

    inner class ViewHolder(val mView : View) : RecyclerView.ViewHolder(mView) {

        val mTextRef : TextView = mView.tv_search_title

        override fun toString() : String {
            return super.toString() + " "
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTextRef.text = item.title

        with(holder.mView) {
            tag = item
            setOnClickListener {
                val intent = Intent(this.context, RecipeDetail::class.java)
                intent.putExtra("uid", (tag as SearchContent.SearchItem).uid)
                intent.putExtra("isOnline", "Yes")
                val fh = FileHandler()
                intent.putExtra("r", fh.getRecipe(this.context, (tag as SearchContent.SearchItem).uid, true) as Parcelable)

                this.context.startActivity(intent)
            }
        }
    }

}