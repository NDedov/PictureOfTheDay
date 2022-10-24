package com.example.pictureoftheday.view.solarsystem

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class PlanetCommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(data: Pair<Planet, Boolean>)
}