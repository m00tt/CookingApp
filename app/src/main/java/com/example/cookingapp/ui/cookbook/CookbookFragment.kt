package com.example.cookingapp.ui.cookbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import com.example.cookingapp.Recipe
import com.example.cookingapp.ui.home.HomeAdapter
import kotlinx.android.synthetic.main.fragment_cookbook.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.list_view


class CookbookFragment : Fragment() {
    private val mRecipeArrayList = ArrayList<Recipe>()
    private var adapter1: CookbookAdapter? = null
    private lateinit var cookbookViewModel: CookbookViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        /*cookbookViewModel =
                ViewModelProvider(this).get(CookbookViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cookbook, container, false)
        val textView: TextView = root.findViewById(R.id.text_cookbook)
        cookbookViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        //apertura activty ricetta

        return root*/
        return inflater.inflate(R.layout.fragment_cookbook, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Ricettario"

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
        adapter1 = CookbookAdapter(context as MainActivity, mRecipeArrayList)
        list_view.adapter = adapter1

        //aggiungo i listener per i 3 bottoni (per filtrare)
        btn_mieRicette.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //si filtrano solo le ricette create dall'utente
                //adapter1?.filter?.filter("MieRicette")
            }
        })

        btn_preferite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //si filtrano solo le ricette preferite dall'utente
                //adapter1?.filter?.filter("Preferite")
            }
        })

        btn_tutte.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //si filtrano tutte le ricette dell'utente
                //adapter1?.filter?.filter("Tutte")
            }
        })

        //aggiungo il listener per il fab dell'aggiunta ricetta
        fab_add.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //si apre l'activity di creazione ricetta

            }
        })

    }
}