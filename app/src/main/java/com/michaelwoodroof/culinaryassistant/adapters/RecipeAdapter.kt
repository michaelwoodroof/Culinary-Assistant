package com.michaelwoodroof.culinaryassistant.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import android.os.Parcelable
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getDrawable
import com.google.android.material.resources.MaterialResources.getDrawable
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.FileHandler
import com.michaelwoodroof.culinaryassistant.helper.ImageConversions
import com.michaelwoodroof.culinaryassistant.mealPlanner.MealPlanner
import com.michaelwoodroof.culinaryassistant.overviews.RecipeDetail
import com.michaelwoodroof.culinaryassistant.structure.MealDocument
import com.michaelwoodroof.culinaryassistant.structure.Recipe

import com.michaelwoodroof.culinaryassistant.structure.RecipeContent
import com.michaelwoodroof.culinaryassistant.structure.Suggestion

import kotlinx.android.synthetic.main.recipe_layout.view.*
import org.w3c.dom.Text
import java.lang.StringBuilder

class RecipeAdapter(
    private val mValues: List<RecipeContent.RecipeItem>
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTitle.text = item.title
        holder.mDesc.text = item.desc
        if (item.hasRating) {
            holder.mRating.text = item.reviewScore.toString()
        } else {
            holder.mRating.visibility = View.GONE
            holder.mStar.visibility = View.GONE
        }

        // Adjust Spice Accordingly
        val res : Resources
        with (holder.mView) {
            res = this.context.resources
        }

        if (item.spiceLevel as Int == 0) {
            holder.mSpice1.visibility = View.INVISIBLE
            holder.mSpice2.visibility = View.INVISIBLE
            holder.mSpice3.visibility = View.INVISIBLE
            holder.mSpice4.visibility = View.INVISIBLE
            holder.mSpice5.visibility = View.INVISIBLE
        } else {
            if (item.spiceLevel as Int == 1) {
                holder.mSpice1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice1.setColorFilter(Color.RED)
                holder.mSpice2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                holder.mSpice3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                holder.mSpice4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                holder.mSpice5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (item.spiceLevel as Int == 2) {
                holder.mSpice1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice1.setColorFilter(Color.RED)
                holder.mSpice2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice2.setColorFilter(Color.RED)
                holder.mSpice3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                holder.mSpice4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                holder.mSpice5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (item.spiceLevel as Int == 3) {
                holder.mSpice1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice1.setColorFilter(Color.RED)
                holder.mSpice2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice2.setColorFilter(Color.RED)
                holder.mSpice3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice3.setColorFilter(Color.RED)
                holder.mSpice4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
                holder.mSpice5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (item.spiceLevel as Int == 4) {
                holder.mSpice1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice1.setColorFilter(Color.RED)
                holder.mSpice2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice2.setColorFilter(Color.RED)
                holder.mSpice3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice3.setColorFilter(Color.RED)
                holder.mSpice4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice4.setColorFilter(Color.RED)
                holder.mSpice5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyoff))
            } else if (item.spiceLevel as Int == 5) {
                holder.mSpice1.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice1.setColorFilter(Color.RED)
                holder.mSpice2.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice2.setColorFilter(Color.RED)
                holder.mSpice3.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice3.setColorFilter(Color.RED)
                holder.mSpice4.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice4.setColorFilter(Color.RED)
                holder.mSpice5.setImageDrawable(res.getDrawable(R.drawable.ic_spicyon))
                holder.mSpice5.setColorFilter(Color.RED)
            }
        }


        if (item.imgString != "") {
            val bm : Bitmap = ImageConversions.stringToBitMap(item.imgString)
            holder.mImgRef.setImageBitmap(bm)
        } else {
            holder.mImgRef.setImageResource(R.drawable.rplaceholder)
        }

        if (item.diff  == 0) {
            holder.tvDiff.text = "Novice"
        } else if (item.diff == 1) {
            holder.tvDiff.text = "Intermediate"
        } else if (item.diff == 2) {
            holder.tvDiff.text = "Expert"
        }

        val sb : StringBuilder = StringBuilder()
        var counter = 0
        for (tag in item.keyWords) {
            if (counter == item.keyWords.size - 1 || item.keyWords.size == 1) {
                sb.append("#$tag")
            } else {
                sb.append("#$tag, ")
            }

            counter++
        }
        holder.mTags.text = sb

        with(holder.mView) {
            tag = item
            val fh = FileHandler()
            if (item.mode == 0) { // OFFLINE
                setOnClickListener {
                    val intent = Intent(this.context, RecipeDetail::class.java)
                    intent.putExtra("uid", (tag as RecipeContent.RecipeItem).id)
                    intent.putExtra("r", fh.getRecipe(this.context,
                        (tag as RecipeContent.RecipeItem).id, false) as Parcelable)
                    intent.putExtra("isOnline", "No")
                    this.context.startActivity(intent)
                }
            } else if (item.mode == 1) { // ONLINE
                setOnClickListener {
                    val intent = Intent(this.context, RecipeDetail::class.java)
                    intent.putExtra("uid", (tag as RecipeContent.RecipeItem).id)

                    val r : Recipe = fh.getRecipe(this.context, (tag as RecipeContent.RecipeItem).id, false)!!
                    intent.putExtra("r", r as Parcelable)
                    intent.putExtra("isOnline", "Yes")

                    this.context.startActivity(intent)
                }
            } else if (item.mode == 2) { // MEAL PLANNER
                setOnClickListener {
                    val intent = Intent(this.context, MealPlanner::class.java)
                    val md : MealDocument = item.mpMode
                    md.uid = item.id
                    fh.updateMealDocument(md, this.context)
                    intent.putExtra("md", md as Parcelable)
                    this.context.startActivity(intent)
                }
            }

        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitle: TextView = mView.tv_local_title
        val mDesc: TextView = mView.tvDescR
        val mImgRef : ImageView = mView.img_ref
        val mSpice1 : ImageView = mView.imgS1
        val mSpice2 : ImageView = mView.imgS2
        val mSpice3 : ImageView = mView.imgS3
        val mSpice4 : ImageView = mView.imgS4
        val mSpice5 : ImageView = mView.imgS5
        val mTags : TextView = mView.tvTags
        val mRating : TextView = mView.tvRating
        val mStar : ImageView = mView.imgStar
        val tvDiff : TextView = mView.tvDiff

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}
