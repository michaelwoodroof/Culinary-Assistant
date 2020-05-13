package com.michaelwoodroof.culinaryassistant.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.CategoryColor
import com.michaelwoodroof.culinaryassistant.helper.FileHandler
import com.michaelwoodroof.culinaryassistant.helper.ImageConversions
import com.michaelwoodroof.culinaryassistant.overviews.RecipeDetail
import com.michaelwoodroof.culinaryassistant.overviews.RecipeOverview
import com.michaelwoodroof.culinaryassistant.structure.MainMenuContent
import com.michaelwoodroof.culinaryassistant.structure.SearchContent
import kotlinx.android.synthetic.main.main_menu_layout.view.*

class MainMenuAdapter (private val mValues : List<MainMenuContent.MainMenuItem>
) : RecyclerView.Adapter<MainMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_menu_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTitle.text = item.title
        if (item.imgStr != "") {
            val bm : Bitmap = ImageConversions.stringToBitMap(item.imgStr)
            holder.mImg.setImageBitmap(bm)
        }
        if (!item.hasReview) {
            holder.mReview.visibility = View.GONE
            holder.mStar.visibility = View.GONE
        }
        holder.mReview.text = item.reviewScore.toString()
        holder.mCard.setCardBackgroundColor(Color.parseColor(CategoryColor.getColor(item.cuisine)))

        with(holder.mView) {
            tag = item
            if (item.hasReview) {
                setOnClickListener {
                    val intent = Intent(this.context, RecipeDetail::class.java)
                    intent.putExtra("uid", (tag as MainMenuContent.MainMenuItem).uid)
                    intent.putExtra("isOnline", "Yes")
                    val fh = FileHandler()
                    intent.putExtra("r", fh.getRecipe(this.context, (tag as MainMenuContent.MainMenuItem).uid, true) as Parcelable)

                    this.context.startActivity(intent)
                }
            } else {
                setOnClickListener {
                    val intent = Intent(this.context, RecipeOverview::class.java)
                    intent.putExtra("type", "category")
                    intent.putExtra("category", item.uid)
                    this.context.startActivity(intent)
                }
            }

        }

    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        // Add Values
        val mCard : CardView = mView.cvMain
        val mTitle : TextView = mView.tvMMLT
        val mImg : ImageView = mView.imgMMRef
        val mReview : TextView = mView.tvReview
        val mStar : ImageView = mView.imgStar

        override fun toString(): String {
            return super.toString() + " '"
        }
    }

}