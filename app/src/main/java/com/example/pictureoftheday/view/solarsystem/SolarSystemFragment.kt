package com.example.pictureoftheday.view.solarsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentSolarSystemBinding
import com.example.pictureoftheday.utils.makeList
import java.util.*

class SolarSystemFragment : Fragment() {

    private var _binding: FragmentSolarSystemBinding? = null
    private val binding: FragmentSolarSystemBinding
        get() {
            return _binding!!
        }

    lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolarSystemBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = makeList()

        val adapter = SolarSystemRecyclerAdapter(
            data,
            object : OnPlanetClickListener {
                override fun onClick(pos: Int) {
                    data[pos] = data[pos].let {
                        it.first to !it.second
                    }
                    binding.recycleViewSolarSystem.adapter?.notifyItemChanged(pos)
                }
            },
            object : OnPlanetMoveListener {
                override fun onMove(fromPosition: Int, toPosition: Int) {
                    Collections.swap(data,fromPosition,toPosition)
                    binding.recycleViewSolarSystem.adapter?.notifyItemMoved(fromPosition, toPosition)
                    if (checkCorrect(data)){
                        Toast.makeText(requireContext(),"Поздравляю! Собрали!",Toast.LENGTH_LONG).show()
                        binding.recycleViewSolarSystem.alpha = 0f
                        binding.recycleViewSolarSystem.animate().alpha(1f).duration = 2000L
                    }
                }
            }
        )

        binding.recycleViewSolarSystem.adapter = adapter
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recycleViewSolarSystem)

        AlertDialog.Builder(requireContext())
            .setTitle("Задание")
            .setMessage(getString(R.string.text_question_solar_system))
            .setPositiveButton("Понятно",null)
            .setIcon(R.drawable.ic_galaxy_menu)
            .show()
    }

    fun checkCorrect(data: MutableList<Pair<Planet, Boolean>>):Boolean{//проверка что планеты выстроены верно
        for (i in 1 until data.size)
            if (data[i].first.id < data[i-1].first.id)
                return false
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}