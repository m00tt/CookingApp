package com.example.cookingapp.ui.cookbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cookingapp.R
import com.example.cookingapp.ui.recipe.RecipeFragment
import kotlinx.android.synthetic.main.fragment_cookbook.*


class CookbookFragment : Fragment() {

    private lateinit var cookbookViewModel: CookbookViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        cookbookViewModel =
                ViewModelProvider(this).get(CookbookViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cookbook, container, false)
        val textView: TextView = root.findViewById(R.id.text_cookbook)
        cookbookViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        //apertura activty ricetta

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Ricettario"

    }
    override fun onStart() {
        super.onStart()

    }
}