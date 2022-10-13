package com.example.pictureoftheday.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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