package com.example.cookingapp.ui.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cookingapp.R
import com.example.cookingapp.ui.cookbook.ConverterViewModel

class ConverterFragment : Fragment() {

    private lateinit var converterViewModel: ConverterViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        converterViewModel =
                ViewModelProvider(this).get(ConverterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cookbook, container, false)
        val textView: TextView = root.findViewById(R.id.text_cookbook)
        converterViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}