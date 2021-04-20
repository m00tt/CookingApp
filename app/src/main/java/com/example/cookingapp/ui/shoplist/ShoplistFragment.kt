package com.example.cookingapp.ui.shoplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cookingapp.R

class ShoplistFragment : Fragment() {

    private lateinit var shoplistViewModel: ShoplistViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        shoplistViewModel =
                ViewModelProvider(this).get(ShoplistViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shoplist, container, false)
        val textView: TextView = root.findViewById(R.id.text_shoplist)
        shoplistViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}