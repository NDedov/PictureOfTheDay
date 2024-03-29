package com.example.pictureoftheday.view.solarsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        showTask()
        val data = makeList()
        initAdapter(data)
    }

    private fun initAdapter(data: MutableList<Pair<Planet, Boolean>>) {
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
                    if (checkCorrect(data))
                        showCongratulation()
                }
            }
        )
        binding.recycleViewSolarSystem.adapter = adapter
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recycleViewSolarSystem)
    }

    private fun showCongratulation() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.congratulation_title))
            .setMessage(getString(R.string.congratulation_message))
            .setPositiveButton(getString(R.string.congratulation_positive_button),null)
            .setIcon(R.drawable.ic_galaxy_menu)
            .show()
        binding.recycleViewSolarSystem.alpha = 0f
        binding.recycleViewSolarSystem.animate().alpha(1f).duration = 2000L
    }

    private fun showTask() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.task_title))
            .setMessage(getString(R.string.text_question_solar_system))
            .setPositiveButton(getString(R.string.task_positive_button),null)
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