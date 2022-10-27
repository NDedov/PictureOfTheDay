package com.example.pictureoftheday.view.solarsystem

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureoftheday.R

class SolarSystemRecyclerAdapter(
    private val data: List<Pair<Planet, Boolean>>,
    private val onPlanetClickListener: OnPlanetClickListener,
    private val onPlanetMoveListener: OnPlanetMoveListener
) :
    RecyclerView.Adapter<PlanetCommonViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetCommonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Planet.TYPE_EARTH -> EarthViewHolder(inflater.inflate(R.layout.fragment_solar_system_item_earth, parent, false) as View)
            Planet.TYPE_PLANET -> PlanetOthersViewHolder(inflater.inflate(R.layout.fragment_solar_system_item_planet, parent, false) as View)
            Planet.TYPE_SUN -> SunViewHolder(inflater.inflate(R.layout.fragment_solar_system_item_sun, parent, false) as View)
            else -> PlanetOthersViewHolder(inflater.inflate(R.layout.fragment_solar_system_item_planet, parent,false) as View)
        }
    }

    override fun onBindViewHolder(holder: PlanetCommonViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].first.type
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        onPlanetMoveListener.onMove(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        //не требуется
    }

    inner class EarthViewHolder(view: View) : PlanetCommonViewHolder(view),
        ItemTouchHelperViewHolder {
        override fun bind(data: Pair<Planet, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                with(itemView.findViewById<TextView>(R.id.descriptionTextView)) {
                    text = data.first.description
                    visibility = if (data.second)
                        View.VISIBLE
                    else
                        View.GONE
                }
                with(itemView.findViewById<ImageView>(R.id.planetImageView)) {
                    setImageResource(data.first.img)
                    setOnClickListener {
                        onPlanetClickListener.onClick(layoutPosition)
                    }
                }
                with(itemView.findViewById<TextView>(R.id.text_planet)){
                    text = data.first.name
                    typeface = Typeface.createFromAsset(itemView.context.assets,"fonts/Doubleplus.ttf")
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.venus_blue_very_dark))
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.earth_background_color))
        }
    }

    inner class SunViewHolder(view: View) : PlanetCommonViewHolder(view),
        ItemTouchHelperViewHolder {
        override fun bind(data: Pair<Planet, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                with(itemView.findViewById<TextView>(R.id.descriptionTextView)) {
                    text = data.first.description
                    visibility = if (data.second)
                        View.VISIBLE
                    else
                        View.GONE
                }
                with(itemView.findViewById<ImageView>(R.id.planetImageView)) {
                    setImageResource(data.first.img)
                    setOnClickListener {
                        onPlanetClickListener.onClick(layoutPosition)
                    }
                }
                with(itemView.findViewById<TextView>(R.id.text_planet)){
                    text = data.first.name
                    typeface = Typeface.createFromAsset(itemView.context.assets,"fonts/Doubleplus.ttf")
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.sun_background_color))
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.sun_background_color))
        }
    }

    inner class PlanetOthersViewHolder(view: View) : PlanetCommonViewHolder(view),
        ItemTouchHelperViewHolder {
        override fun bind(data: Pair<Planet, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                with(itemView.findViewById<TextView>(R.id.descriptionTextView)) {
                    text = data.first.description
                    visibility = if (data.second)
                        View.VISIBLE
                    else
                        View.GONE
                }
                with(itemView.findViewById<ImageView>(R.id.planetImageView)) {
                    setImageResource(data.first.img)
                    setOnClickListener {
                        onPlanetClickListener.onClick(layoutPosition)
                    }
                }
                with(itemView.findViewById<TextView>(R.id.text_planet)){
                    text = data.first.name
                    typeface = Typeface.createFromAsset(itemView.context.assets,"fonts/Doubleplus.ttf")
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.DKGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.planet_background_color))
        }
    }
}
