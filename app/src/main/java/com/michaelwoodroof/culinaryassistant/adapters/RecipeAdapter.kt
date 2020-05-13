package com.michaelwoodroof.culinaryassistant.adapters

import android.content.Intent
import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.ImageConversions
import com.michaelwoodroof.culinaryassistant.overviews.RecipeDetail

import com.michaelwoodroof.culinaryassistant.structure.RecipeContent

import kotlinx.android.synthetic.main.recipe_layout.view.*

class RecipeAdapter(
    private val mValues: List<RecipeContent.RecipeItem>
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
//        holder.mIdView.text = item.id
//        holder.mContentView.text = item.content

        if (item.imgString != "") {
            val bm : Bitmap = ImageConversions.stringToBitMap(item.imgString)
            holder.mImgRef.setImageBitmap(bm)
        } else {
            holder.mImgRef.setImageResource(R.drawable.rplaceholder)
        }

        with(holder.mView) {
            tag = item
            setOnClickListener {
                val intent = Intent(this.context, RecipeDetail::class.java)
                intent.putExtra("uid", (tag as RecipeContent.RecipeItem).id)
                this.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
//        val mIdView: TextView = mView.item_number
//        val mContentView: TextView = mView.content
        val mImgRef : ImageView = mView.img_ref

        override fun toString(): String {
            return super.toString() + " '"
            //return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
