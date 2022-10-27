package com.example.pictureoftheday.view.pod

import android.annotation.SuppressLint

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import com.example.pictureoftheday.model.domain.PODData
import com.example.pictureoftheday.utils.IMAGE_VIEW_SCALE_BEGIN
import com.example.pictureoftheday.utils.IMAGE_VIEW_SCALE_END
import com.example.pictureoftheday.utils.scaleView
import com.example.pictureoftheday.utils.toast
import com.example.pictureoftheday.view.podpager.Days
import com.example.pictureoftheday.viewmodel.PictureOfTheDayAppState
import com.example.pictureoftheday.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*
import kotlin.properties.Delegates


class PictureOfTheDayFragment(private val day: Days = Days.Today) : Fragment(), OnImageViewAnimationEnd {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding: FragmentPictureOfTheDayBinding
        get() {
            return _binding!!
        }

    private var isExpanded = false
    private var pivotX by Delegates.notNull<Float>()
    private var pivotY by Delegates.notNull<Float>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    companion object {
        fun newInstance(day: Days) = PictureOfTheDayFragment(day)
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
        setBottomSheetBehavior(view)
        initImageViewAnim()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initImageViewAnim() {
        binding.imageView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (!isExpanded) {
                    pivotX = (event.x - v.left) / (v.right - v.left)// расчет центра для приближения
                    pivotY = (event.y - v.top) / (v.bottom - v.top)
                    v.scaleView(IMAGE_VIEW_SCALE_BEGIN, IMAGE_VIEW_SCALE_END, pivotX, pivotY,this)
                } else
                    v.scaleView(IMAGE_VIEW_SCALE_END, IMAGE_VIEW_SCALE_BEGIN, pivotX, pivotY, this)
            }
            true
        }
    }

    private fun viewModelInit() {
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        val dayShift = when (day) {
            Days.Today -> 0
            Days.Yesterday -> -1
            Days.TDBY -> -2
        }
        viewModel.getData(GregorianCalendar().apply {
            add(Calendar.DATE, dayShift)
        }.time)
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
        val string  = serverResponseData.title
        val spannableString = SpannableString(string)
        val bitmap  = ContextCompat.getDrawable(requireContext(),R.drawable.ic_o_planet)!!.toBitmap()
        if (string != null) {
            for (i in string.indices)
                if (string[i] == 'o'){
                    spannableString.setSpan(ImageSpan(requireContext(),bitmap),i,i+1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            binding.bottomSheetLayout.bottomSheetDescriptionHeader.text = spannableString
        }
        binding.bottomSheetLayout.bottomSheetDescription.text = serverResponseData.explanation
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onEnd() {
        isExpanded = !isExpanded
    }
}