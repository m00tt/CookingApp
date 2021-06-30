package com.example.cookingapp.ui.home

import android.content.ClipData
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.*
import androidx.core.graphics.component1
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import com.example.cookingapp.Recipe
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() , PopupMenu.OnMenuItemClickListener{

    //private lateinit var homeViewModel: HomeViewModel
    private val mRecipeArrayList = ArrayList<Recipe>()
    private var adapter1: HomeAdapter? = null
    lateinit var menu:Menu
    lateinit var checkati:String

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

        val popup = PopupMenu(context as MainActivity,img_filter)
        popup.setOnMenuItemClickListener(this@HomeFragment)
        popup.inflate(R.menu.popup_menu)
        menu=popup.menu

        //aggiungo il listener all'imageButton per il filtro
        img_filter.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
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
                menu.findItem(R.id.Media).setChecked(false)
                menu.findItem(R.id.Difficile).setChecked(false)
                setCheck(item)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true}
            R.id.Media -> {
                //Toast.makeText(context as MainActivity, "hai selezionato difficolta' Media", Toast.LENGTH_SHORT).show()
                searchBar.text.clear()
                menu.findItem(R.id.Facile).setChecked(false)
                menu.findItem(R.id.Difficile).setChecked(false)
                setCheck(item)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true}
            R.id.Difficile -> {
                //Toast.makeText(context as MainActivity, "hai selezionato difficolta' Difficile", Toast.LENGTH_SHORT).show()
                searchBar.text.clear()
                menu.findItem(R.id.Media).setChecked(false)
                menu.findItem(R.id.Facile).setChecked(false)
                setCheck(item)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true}

            //gestione durata
            R.id.Veloce -> {
                searchBar.text.clear()
                menu.findItem(R.id.Media_durata).setChecked(false)
                menu.findItem(R.id.Lunga).setChecked(false)
                setCheck(item)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true}
            R.id.Media_durata -> {
                menu.findItem(R.id.Veloce).setChecked(false)
                menu.findItem(R.id.Lunga).setChecked(false)
                searchBar.text.clear()
                setCheck(item)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true}
            R.id.Lunga -> {
                menu.findItem(R.id.Media_durata).setChecked(false)
                menu.findItem(R.id.Veloce).setChecked(false)
                searchBar.text.clear()
                setCheck(item)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true}

            //gestione portata
            R.id.Antipasto -> {
                menu.findItem(R.id.Primo).setChecked(false)
                menu.findItem(R.id.Secondo).setChecked(false)
                menu.findItem(R.id.Dessert).setChecked(false)
                searchBar.text.clear()
                setCheck(item)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true}
            R.id.Primo -> {
                menu.findItem(R.id.Antipasto).setChecked(false)
                menu.findItem(R.id.Secondo).setChecked(false)
                menu.findItem(R.id.Dessert).setChecked(false)
                searchBar.text.clear()
                setCheck(item)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true}
            R.id.Secondo -> {
                menu.findItem(R.id.Primo).setChecked(false)
                menu.findItem(R.id.Antipasto).setChecked(false)
                menu.findItem(R.id.Dessert).setChecked(false)
                searchBar.text.clear()
                setCheck(item)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true}
            R.id.Dessert -> {
                menu.findItem(R.id.Primo).setChecked(false)
                menu.findItem(R.id.Secondo).setChecked(false)
                menu.findItem(R.id.Antipasto).setChecked(false)
                searchBar.text.clear()
                setCheck(item)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true}

            //tutte le ricette
            R.id.Tutte -> {
                searchBar.text.clear()
                menu.findItem(R.id.Facile).setChecked(false)
                menu.findItem(R.id.Media).setChecked(false)
                menu.findItem(R.id.Difficile).setChecked(false)
                menu.findItem(R.id.Veloce).setChecked(false)
                menu.findItem(R.id.Media_durata).setChecked(false)
                menu.findItem(R.id.Lunga).setChecked(false)
                menu.findItem(R.id.Antipasto).setChecked(false)
                menu.findItem(R.id.Primo).setChecked(false)
                menu.findItem(R.id.Secondo).setChecked(false)
                menu.findItem(R.id.Dessert).setChecked(false)
                checkati=controlCheck()
                adapter1?.filter?.filter(checkati)
                return true
            }

            else -> return false
        }
    }

    private fun setCheck(item:MenuItem?){
        item!!.setChecked(!item.isChecked)
    }

    private fun controlCheck():String{
        var controllo = ""

        if(menu.findItem(R.id.Facile).isChecked)
            controllo+="Facile "
        if(menu.findItem(R.id.Media).isChecked)
            controllo+="Media "
        if(menu.findItem(R.id.Difficile).isChecked)
            controllo+="Difficile "
        if(menu.findItem(R.id.Veloce).isChecked)
            controllo+="Veloce "
        if(menu.findItem(R.id.Media_durata).isChecked)
            controllo+="Media_durata "
        if(menu.findItem(R.id.Lunga).isChecked)
            controllo+="Lunga "
        if(menu.findItem(R.id.Antipasto).isChecked)
            controllo+="Antipasto "
        if(menu.findItem(R.id.Primo).isChecked)
            controllo+="Primo "
        if(menu.findItem(R.id.Secondo).isChecked)
            controllo+="Secondo "
        if(menu.findItem(R.id.Dessert).isChecked)
            controllo+="Dessert "

        if(controllo=="")
            controllo="Tutte"

        Toast.makeText(context as MainActivity, controllo, Toast.LENGTH_SHORT).show()
        return  controllo
    }

}
