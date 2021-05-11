package com.example.cookingapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import com.example.cookingapp.Recipe
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    //private lateinit var homeViewModel: HomeViewModel
    private val mRecipeArrayList = ArrayList<Recipe>()
    private var adapter1: HomeAdapter? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        /*homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val listView: ListView = root.findViewById(R.id.list_view)
        homeViewModel.ricette.observe(viewLifecycleOwner, Observer {
            listView.adapter=HomeAdapter(context as MainActivity, it)
        })
        return root*/
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onStart() {
        super.onStart()

        //aggiungo le ricette
        if(mRecipeArrayList.isEmpty()) {
            mRecipeArrayList.add(Recipe("Ricetta 1", "Facile", "15 minuti", "Antipasto"))
            mRecipeArrayList.add(Recipe("Ricetta 2", "Media", "20 minuti", "Primo"))
            mRecipeArrayList.add(Recipe("Ricetta 3", "Facile", "10 minuti", "Secondo"))
            mRecipeArrayList.add(Recipe("Ricetta 4", "Difficile", "30 minuti", "Dessert"))
            mRecipeArrayList.add(Recipe("Ricetta 5", "Facile", "7 minuti", "Antipasto"))
            mRecipeArrayList.add(Recipe("Ricetta 6", "Media", "25 minuti", "Primo"))
            mRecipeArrayList.add(Recipe("Ricetta 7", "Facile", "15 minuti", "Dessert"))
            mRecipeArrayList.add(Recipe("Ricetta 8", "Difficile", "60 minuti", "Antipasto"))
            mRecipeArrayList.add(Recipe("Ricetta 9", "Facile", "7 minuti", "Secondo"))
            mRecipeArrayList.add(Recipe("Ricetta 10", "Difficile", "40 minuti", "Antipasto"))
        }

        //carico le ricette nella listView
        adapter1 = HomeAdapter(context as MainActivity, mRecipeArrayList)
        list_view.adapter = adapter1

        //aggiungo il listener alla edit_text che fa da searchBar
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Call back the Adapter with current character to Filter
                adapter1?.filter?.filter(s.toString())

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

}
