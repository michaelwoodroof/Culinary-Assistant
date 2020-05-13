package com.michaelwoodroof.culinaryassistant.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.structure.StepContent
import kotlinx.android.synthetic.main.step_layout.view.*

class StepAdapter(private val mValues: ArrayList<StepContent.StepItem>
) : RecyclerView.Adapter<StepAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.step_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mStepNumber.text = item.stepNumber
        holder.mStepDesc.text = item.description
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        // Add Values
        val mStepNumber : TextView = mView.tvStepNumber
        val mStepDesc : TextView = mView.tvDesc

        override fun toString(): String {
            return super.toString() + " '"
        }
    }

}