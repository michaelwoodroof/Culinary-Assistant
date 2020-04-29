package com.michaelwoodroof.culinaryassistant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.michaelwoodroof.culinaryassistant.structure.IngredientContent
import kotlinx.android.synthetic.main.ingredient_layout.view.*

class IngredientAdapter (private val mValues: List<IngredientContent.IngredientItem>
) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mAmount.text = item.amount
        holder.mUnit.text = item.unit
        holder.mIngredientName.text = item.ingredientName
        holder.mNotes.text = item.notes
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        // Add Values
        val mAmount : TextView = mView.tvAmount
        val mUnit : TextView = mView.tvUnit
        val mIngredientName : TextView = mView.tvIngredientName
        val mNotes : TextView = mView.tvNotes

        override fun toString(): String {
            return super.toString() + " '"
        }
    }

}