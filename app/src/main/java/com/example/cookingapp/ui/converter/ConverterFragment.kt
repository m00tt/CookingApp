package com.example.cookingapp.ui.converter

import android.content.res.loader.ResourcesLoader
import android.graphics.PointF.length
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cookingapp.R
import android.widget.Toast
import com.example.cookingapp.MainActivity
import com.example.cookingapp.ui.converter.ConverterViewModel
import kotlinx.android.synthetic.main.fragment_converter.*

class ConverterFragment : Fragment() {

    private lateinit var converterViewModel: ConverterViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        converterViewModel =
                ViewModelProvider(this).get(ConverterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_converter, container, false)
        //al textView: TextView = root.findViewById(R.id.text_converter)
        converterViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
        return root
    }

    override fun onStart() {
        super.onStart()
        converter_change_values.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val myResources = getResources()  //Verifica della resource table
                if (converter_tv_gr.text.equals(myResources.getText(R.string.converter_gr_text_title))){
                    converter_tv_gr.text = myResources.getText(R.string.converter_grassiveg_text_title)
                    converter_tv_spoon.text = myResources.getText(R.string.converter_grassianm_text_title)
                }
                else{
                    converter_tv_gr.text = myResources.getText(R.string.converter_gr_text_title)
                    converter_tv_spoon.text = myResources.getText(R.string.converter_spoon_text_title)
                }
            }
        })
    }



}