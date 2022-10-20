package com.example.pictureoftheday.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentWikiBinding

class WikiFragment : Fragment() {

    private var _binding: FragmentWikiBinding? = null
    private val binding: FragmentWikiBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWikiBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wikiFindInit()
        wikiAnimInit()
    }

    private fun wikiAnimInit() {
        val constraintSetEnd = ConstraintSet()
        val constraintSetBegin = ConstraintSet()
        constraintSetEnd.clone(requireContext(), R.layout.fragment_wiki_end)
        constraintSetBegin.clone(requireContext(), R.layout.fragment_wiki)
        val changeBounds = ChangeBounds()
        with (changeBounds){
            duration = 1000L
            interpolator = AnticipateOvershootInterpolator(5f)
        }

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //пусто
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TransitionManager.beginDelayedTransition(binding.wikiContainer,changeBounds)
                if (binding.inputEditText.text.toString() == "")
                    constraintSetBegin.applyTo(binding.wikiContainer)
                else
                    constraintSetEnd.applyTo(binding.wikiContainer)
            }

            override fun afterTextChanged(p0: Editable?) {
                //пусто
            }
        })
    }

    private fun wikiFindInit(){
        binding.inputLayout.setEndIconOnClickListener {wikiStartActivity("en")}
        binding.wikiFindButtonEn.setOnClickListener {wikiStartActivity("en")}
        binding.wikiFindButtonRu.setOnClickListener {wikiStartActivity("ru")}
    }

    private fun wikiStartActivity(domain: String){
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://${domain}.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}