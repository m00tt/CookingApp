package com.example.cookingapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import com.example.cookingapp.Recipe
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() , PopupMenu.OnMenuItemClickListener{

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

        //aggiungo il listener all'imageButton per il filtro
        img_filter.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val popup = PopupMenu(context as MainActivity,v)
                popup.setOnMenuItemClickListener(this@HomeFragment)
                popup.inflate(R.menu.popup_menu)
                popup.show()

            }
        })
    }

    //metodo che gestisce l'evento click degli elementi del popup_menu
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId){
            //gestione difficoltà
            R.id.Facile -> {
                //Toast.makeText(context as MainActivity, "hai selezionato difficolta' Facile", Toast.LENGTH_SHORT).show()
                searchBar.text.clear()
                adapter1?.filter?.filter("Facile")
                return true}
            R.id.Media -> {
                //Toast.makeText(context as MainActivity, "hai selezionato difficolta' Media", Toast.LENGTH_SHORT).show()
                searchBar.text.clear()
                adapter1?.filter?.filter("Media")
                return true}
            R.id.Difficile -> {
                //Toast.makeText(context as MainActivity, "hai selezionato difficolta' Difficile", Toast.LENGTH_SHORT).show()
                searchBar.text.clear()
                adapter1?.filter?.filter("Difficile")
                return true}

            //gestione durata
            R.id.Veloce -> {
                searchBar.text.clear()
                adapter1?.filter?.filter("Veloce")
                return true}
            R.id.Media_durata -> {
                searchBar.text.clear()
                adapter1?.filter?.filter("Media_durata")
                return true}
            R.id.Lunga -> {
                searchBar.text.clear()
                adapter1?.filter?.filter("Lunga")
                return true}

            //gestione portata
            R.id.Antipasto -> {
                searchBar.text.clear()
                adapter1?.filter?.filter("Antipasto")
                return true}
            R.id.Primo -> {
                searchBar.text.clear()
                adapter1?.filter?.filter("Primo")
                return true}
            R.id.Secondo -> {
                searchBar.text.clear()
                adapter1?.filter?.filter("Secondo")
                return true}
            R.id.Dessert -> {
                searchBar.text.clear()
                adapter1?.filter?.filter("Dessert")
                return true}

            //tutte le ricette
            R.id.Tutte -> {
                searchBar.text.clear()
                adapter1?.filter?.filter("Tutte")
                return true
            }

            else -> return false
        }
    }

}
