package com.example.pictureoftheday.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.example.pictureoftheday.Days
import com.example.pictureoftheday.MainActivity
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import com.example.pictureoftheday.model.domain.PODData
import com.example.pictureoftheday.utils.BOTTOM_NAVIGATION_DRAWER_FRAGMENT
import com.example.pictureoftheday.utils.BOTTOM_SETTINGS_FRAGMENT_TAG
import com.example.pictureoftheday.utils.toast
import com.example.pictureoftheday.viewmodel.PictureOfTheDayAppState
import com.example.pictureoftheday.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class PictureOfTheDayFragment(val day:Days) : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding: FragmentPictureOfTheDayBinding
        get() {
            return _binding!!
        }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    companion object {
        fun newInstance(day: Days) = PictureOfTheDayFragment(day)
        private var isMain = true
    }

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelInit()
        wikiFindInit()
        setBottomAppBar(view)
        setBottomSheetBehavior(view)
    }

    private fun viewModelInit(){
        viewModel.getLiveData().observe(viewLifecycleOwner) {renderData(it)}
        val dayShift = when (day){
            Days.Today -> 0
            Days.Yesterday -> -1
            Days.TDBY -> -2
        }
        viewModel.getData(GregorianCalendar().apply {
            add(Calendar.DATE,dayShift) }.time)
    }

    private fun wikiFindInit(){
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun renderData(appState: PictureOfTheDayAppState) {
        when (appState) {
            is PictureOfTheDayAppState.Success -> {
                val serverResponseData = appState.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    showImageAndDescription(url, serverResponseData)
                }
            }
            is PictureOfTheDayAppState.Loading -> {
                binding.imageView.loadGif(R.drawable.loading_mod)
            }
            is PictureOfTheDayAppState.Error -> {
                toast(appState.error.message)
            }
        }
    }

    private fun showImageAndDescription(
        url: String?,
        serverResponseData: PODData
    ) {
        binding.imageView.load(url) {
            lifecycle(this@PictureOfTheDayFragment)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.loading_mod)
            crossfade(500)
        }
        binding.bottomSheetLayout.bottomSheetDescription.text =
            serverResponseData.explanation
        binding.bottomSheetLayout.bottomSheetDescriptionHeader.text =
            serverResponseData.title
    }

    private fun ImageView.loadGif(img: Int) {
        this.load(img, ImageLoader.Builder(requireContext())
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder(requireContext()))
                } else {
                    add(GifDecoder())
                }
            }
            .build())
    }

    private fun setBottomSheetBehavior(view: View) {
        val bottomSheet = view.findViewById<ConstraintLayout>(R.id.bottom_sheet_layout)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> toast("Favourite")
            R.id.app_bar_settings -> {
                activity?.let {
                    BottomSettingsFragment().show(it.supportFragmentManager, BOTTOM_SETTINGS_FRAGMENT_TAG)
                }
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, BOTTOM_NAVIGATION_DRAWER_FRAGMENT)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}